# Roadmap de Refatoração — 4 Fases

Documento de implementação consolidado, gerado a partir de uma varredura completa
do projeto. Reúne quatro correções independentes, em ordem de dependência, mais
duas micro-tarefas de limpeza.

**Regra do `CLAUDE.md` vale para todas:** uma tarefa de cada vez. Cada fase compila
(`./gradlew build`), mostra `git diff` e é validada antes de abrir a próxima.
**Não misturar fases num mesmo commit.**

## Ordem obrigatória

| # | Fase | Arquivos | Tipo | Por que nesta posição |
|---|------|----------|------|------------------------|
| 1 | `update` na `PlayScreen` | `PlayScreen` | Estrutural | Nenhuma dependência; desbloqueia a Fase 4 |
| 2 | Estado transiente do `CombatScreen` | `CombatScreen` | Estrutural | Independente |
| 3 | Simetria de refcount de assets | `Assets`, `SpecEncounterManifest`, `CombatEncounter`, `CombatScreen` | Estrutural | Independente |
| 4 | Timing por delta | `AnimatedEntity`, `Attack`, `CombatForm`, `GameConstants`, `PlayScreen`, `CombatScreen` | Comportamental | **Exige as Fases 1 e 2 prontas** (precisa de `delta` disponível nos dois `update`) |
| 5 | Micro: timer do turno no passo certo | `CombatScreen` | Limpeza | Depois da Fase 2 |
| 6 | Micro: remover `CombatEncounter.bossAtks` | `CombatEncounter`, `SpecEncounterManifest` | Limpeza | Depois da Fase 3 |

**Fora de escopo neste documento** (decidido explicitamente): mecânica de mana /
softlock, tela de vitória, detecção de vitória fora do ataque normal (Fury of the
Eye), extração de `MapNavigator`, `State` para navegação no mapa, e a separação
consulta/comando em `GameMap.fightPointTest()`. Todos ficam para depois.

---

# Fase 1 — Passo `update` na `PlayScreen`

## Problema

O `REFACTOR_UPDATE_STEP` foi aplicado só no `CombatScreen`. Na `PlayScreen`, o
método `draw(delta)` ainda:

- move o jogador e muta o `currentMapOffset` do mapa;
- chama `gameMap.fightPointTest()` (que tem efeito colateral no `currentPoint`);
- decide a transição para o combate e chama `game.setScreen(game.loading)`
  **entre `batch.begin()` e `batch.end()`**, e sem o `return true` que o
  `BaseScreen` exige para abortar o frame;
- calcula as direções permitidas.

Isso viola o contrato do Template Method já estabelecido no `BaseScreen`
(`handleInput` → `update` → `draw`) e é o único lugar do projeto onde uma troca de
tela acontece dentro de um batch aberto.

## Design alvo

Mover para `update(float delta)` tudo que não é `batch.draw`. **Nenhuma classe
nova, nenhum campo novo além de um único booleano.**

O acoplamento que hoje força a mistura é que os botões de direção só são
desenhados dentro do ramo `if (fightPointTest())`. Os campos
`moveUp/moveDown/moveLeft/moveRight` já existem e já carregam essa informação —
falta apenas um campo `atFightPoint` para o `draw` saber se deve desenhá-los.

## Implementação

### 1.1 — Adicionar o campo

Em `PlayScreen`, junto dos demais campos de estado:

```java
private boolean atFightPoint;
```

### 1.2 — Criar `update(float delta)`

Sobrescrever o hook do `BaseScreen`. Conteúdo, **nesta ordem** (é a ordem do
`draw` atual, apenas sem os `batch.draw`):

```java
@Override
protected boolean update(float delta) {
    if (instances.playerObject.getMoving()) {
        instances.playerObject.update();
        if (verticalMoving) {
            instances.gameMap.getCurrentPoint().setPoint(new Vector2(
                    instances.gameMap.getCurrentPoint().getPoint().x,
                    instances.gameMap.getCurrentPoint().getPoint().y + currentMapOffset));
        } else {
            instances.gameMap.getCurrentPoint().setPoint(new Vector2(
                    instances.gameMap.getCurrentPoint().getPoint().x + currentMapOffset,
                    instances.gameMap.getCurrentPoint().getPoint().y));
        }
    } else {
        instances.playerObject.setImage(instances.playerObject.getFrame()[1]);
    }

    atFightPoint = instances.gameMap.fightPointTest();
    if (!atFightPoint) {
        return false;
    }

    moveUp = false;
    moveDown = false;
    moveLeft = false;
    moveRight = false;
    instances.playerObject.setMoving(false);

    if (instances.gameMap.getCurrentPoint().getActivated()) {
        EncounterManifest manifest = instances.gameMap.getCurrentPoint().getManifest();
        game.loading.begin(
                () -> assets.queue(manifest.descriptors()),
                () -> game.combat.setEncounter(manifest.build(assets)),
                () -> game.combat);
        game.setScreen(game.loading);
        return true; // aborta o frame: nao desenha por cima da tela nova
    }

    instances.gameMap.setPreviousPoint(instances.gameMap.getCurrentPoint());

    List<String> allowedDirections = instances.gameMap.getCurrentPoint().getAllowedDirections();
    moveUp = allowedDirections.contains("UP");
    moveDown = allowedDirections.contains("DOWN");
    moveRight = allowedDirections.contains("RIGHT");
    moveLeft = allowedDirections.contains("LEFT");

    return false;
}
```

### 1.3 — Enxugar `draw(float delta)`

Fica apenas com desenho:

```java
@Override
protected void draw(float delta) {
    Vector2 currentPos = instances.gameMap.getCurrentPoint().getPoint();
    TextureRegion mapRegion = new TextureRegion(instances.gameMap.getImage(),
            Math.round(currentPos.x), Math.round(currentPos.y),
            GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    mapRegion.flip(false, true);
    batch.draw(mapRegion, 0, 0);

    batch.draw(instances.playerObject.getImage(),
            instances.playerObject.getPosition().x, instances.playerObject.getPosition().y);

    instances.pauseButton.draw(batch);

    if (!atFightPoint) {
        return;
    }
    if (moveUp)    instances.moveUpButton.draw(batch);
    if (moveDown)  instances.moveDownButton.draw(batch);
    if (moveRight) instances.moveRightButton.draw(batch);
    if (moveLeft)  instances.moveLeftButton.draw(batch);
}
```

### 1.4 — Imports

`java.util.List` e `com.flamerealm.game.assets.EncounterManifest` migram de uso
para o `update`, mas continuam necessários no arquivo. `TextureRegion` e `Vector2`
permanecem.

## Mudanças de comportamento a registrar no commit

1. **Um frame de diferença.** Hoje o mapa é desenhado com a posição *anterior* ao
   movimento daquele frame. Depois da refatoração é desenhado com a posição já
   atualizada. Imperceptível a olho nu, mas é uma mudança real de fidelidade —
   mencionar na mensagem de commit.
2. **Botões de direção não são mais desenhados no frame da transição.** Hoje,
   quando o ponto é ativado, o código chama `setScreen` e **continua** desenhando
   os botões sobre um batch de uma tela que está sendo substituída. Com o
   `return true` isso deixa de acontecer. Não há impacto visível: ao voltar do
   combate o ponto está desativado e os flags são recalculados no frame seguinte.

## Critério de aceite

- `./gradlew build` limpo.
- Andar pelo mapa nas 4 direções funciona igual.
- Chegar num ponto ativado entra em combate com o mesmo fade.
- Voltar do combate (vitória) mostra os botões de direção do ponto corretamente.
- Morrer volta ao ponto anterior e os botões aparecem corretamente.
- Pausar no meio de um movimento e retomar preserva o movimento (o estado do
  singleton continua intacto).

---

# Fase 2 — Estado transiente do `CombatScreen`

## Problema

`playerLastAtk`, `enemyLastAtk`, `turnoAtual` e `elapsedSinceLastClick` têm
**escopo de encontro**, mas vivem num singleton de escopo de aplicação e não são
reinicializados. Sobrevivem de um combate para o outro. Hoje não explode (o HP do
chefe novo está cheio, o que neutraliza o ramo de vitória), mas é estado morto
atravessando fronteira de encontro — e qualquer mudança na máquina de turnos
transforma isso em bug.

`usedFuryOfTheEye` já é resetado, mas no `show()`.

## Design alvo

Reset em `setEncounter(...)`, **não** em `show()`.

`setEncounter` é literalmente o momento em que um encontro começa; `show()` é um
evento de ciclo de vida da tela que por coincidência acontece junto. Usar o seam
semanticamente correto significa que, ao adicionar um campo de escopo de encontro
no futuro, o lugar de resetá-lo é óbvio.

## Implementação

### 2.1 — Concentrar o reset em `setEncounter`

```java
/**
 * Chamado pela LoadingScreen (postLoadJob) antes desta tela virar ativa.
 * Este e o unico seam de inicio de encontro: todo estado de escopo de encontro
 * (turno, timers, ultimo ataque, uso do Fury) nasce aqui.
 */
public void setEncounter(CombatEncounter encounter) {
    this.encounter = encounter;
    this.turnoAtual = Turn.PLAYER;
    this.playerLastAtk = null;
    this.enemyLastAtk = null;
    this.elapsedSinceLastClick = WAIT_TIME + 1f;
    this.usedFuryOfTheEye = false;
}
```

### 2.2 — Remover o `show()`

O `show()` que só fazia `usedFuryOfTheEye = false` deixa de ser necessário —
remover o override inteiro (o `BaseScreen` já tem o hook vazio).

## Critério de aceite

- `./gradlew build` limpo.
- Vencer um chefe, voltar ao mapa e entrar noutro combate: o turno começa com o
  jogador, sem ataque residual em tela e com o Fury of the Eye disponível de novo.
- Morrer, voltar ao mapa e reentrar: mesmo comportamento.

---

# Fase 3 — Simetria de contagem de referências de assets

## Problema

O `AssetManager` do libGDX faz contagem de referência: `load()` num asset já
carregado **incrementa** o contador. Os guardas em `Assets` sabotam esse
mecanismo:

```java
public void queue(Array<AssetDescriptor<?>> ds) {
    for (...) if (!assetManager.isLoaded(d.fileName)) assetManager.load(d);   // suprime o incremento
}
public void unload(Array<AssetDescriptor<?>> ds) {
    for (...) if (assetManager.isLoaded(d.fileName)) assetManager.unload(...); // NAO suprime o decremento
}
```

Assimetria. Some-se a isso que `SpecEncounterManifest` gera descriptors
duplicados (o Necromancer usa a mesma sheet nos 4 `AnimState` → 4 descriptors
idênticos), e o equilíbrio atual passa a depender de uma coincidência: no momento
do `queue` nada está carregado ainda, então os 4 viram 4 `load()` e depois 4
`unload()`.

Cenários que quebram (todos previstos no roadmap do jogo):

- uma sheet compartilhada entre `CoreAssets` e um encontro → `queue` pula,
  `unload` decrementa → asset do núcleo destruído em uso;
- pré-carregar o próximo encontro com o atual ainda em memória;
- um novo caminho de saída do combate que esqueça de descarregar.

Problema irmão: `Assets.regionCache` nunca é invalidado. Uma `TextureRegion`
cacheada sobrevive à `Texture` disposta.

## Design alvo

Trocar a coincidência por um invariante:

1. **Deduplicar** os descriptors por `fileName` no manifesto → cada grupo conta
   exatamente 1 por asset.
2. **Remover os guardas** de `queue`/`unload` → deixar o refcount do
   `AssetManager` fazer o trabalho, que é para isso que ele existe.
3. **Invalidar o `regionCache`** no `unload`.
4. Tornar a liberação **idempotente** movendo-a para `CombatEncounter.release(...)`,
   para que chamar duas vezes não decremente duas vezes.

Com isso: se um asset de encontro também estiver no núcleo, o refcount vai a 2 no
`queue` e volta a 1 no `unload` — o núcleo sobrevive. Correto por construção, não
por sorte.

## Implementação

### 3.1 — `SpecEncounterManifest`: deduplicar

No construtor, montar os descriptors evitando repetição de `fileName`:

```java
public SpecEncounterManifest(EnemySpec spec) {
    this.spec = spec;
    this.descriptors = new Array<>();
    ObjectSet<String> seen = new ObjectSet<>();

    for (AnimationSpec clip : spec.body().states().values()) {
        addOnce(seen, clip.sheetPath());
    }
    for (BossAttackSpec atk : spec.attacks()) {
        addOnce(seen, atk.clip().sheetPath());
    }
}

/** Um descriptor por arquivo: o refcount do AssetManager conta 1 por grupo. */
private void addOnce(ObjectSet<String> seen, String path) {
    if (seen.add(path)) {
        descriptors.add(new AssetDescriptor<>(path, Texture.class));
    }
}
```

Import: `com.badlogic.gdx.utils.ObjectSet`.

### 3.2 — `Assets`: remover os guardas e invalidar o cache de regiões

```java
/**
 * Enfileira uma lista de descriptors. SEM guarda de isLoaded: o AssetManager
 * ja faz contagem de referencia, e o guarda quebraria a simetria com unload()
 * (suprimiria o incremento sem suprimir o decremento correspondente).
 * Pre-condicao: cada fileName aparece no maximo uma vez na lista.
 */
public void queue(Array<AssetDescriptor<?>> descriptors) {
    for (AssetDescriptor<?> d : descriptors) {
        assetManager.load(d);
    }
}

/**
 * Descarrega uma lista de descriptors. Simetrico a queue(): um decremento por
 * descriptor. Assets compartilhados com outro grupo sobrevivem, porque o
 * refcount so chega a zero quando o ultimo dono solta.
 */
public void unload(Array<AssetDescriptor<?>> descriptors) {
    for (AssetDescriptor<?> d : descriptors) {
        regionCache.remove(d.fileName); // evita TextureRegion apontando para Texture disposta
        assetManager.unload(d.fileName);
    }
}
```

O overload `queue(String path, Class<?> type)` não é usado por ninguém; pode
ficar como está.

### 3.3 — `CombatEncounter`: liberação idempotente

Adicionar um flag e um método, para que o `CombatScreen` não precise saber de
descriptors nem correr risco de liberar duas vezes:

```java
private boolean released;

/**
 * Devolve os assets do encontro ao AssetManager. Idempotente: chamar duas vezes
 * (ou por dois caminhos de saida diferentes do combate) nao decrementa duas vezes.
 */
public void release(Assets assets) {
    if (released) {
        return;
    }
    released = true;
    assets.unload(descriptors);
}
```

Import: `com.flamerealm.game.Assets`. O campo `descriptors` pode passar de
`public` para `private final`.

### 3.4 — `CombatScreen`: usar `release(...)`

Duas trocas:

```java
// ramo de morte
assets.unload(encounter.descriptors);      →  encounter.release(assets);

// ramo de vitoria (dentro do loadJob da transicao)
() -> assets.unload(enc.descriptors),      →  () -> enc.release(assets),
```

## Critério de aceite

- `./gradlew build` limpo.
- Entrar e sair de combate 3–4 vezes seguidas (misturando vitória e morte) sem
  `GdxRuntimeException` de asset não carregado e sem sprite sumindo/virando preto.
- Lutar contra o Necromancer (sheet única com 4 estados) e vencer: os assets do
  núcleo continuam intactos ao voltar ao mapa.

---

# Fase 4 — Timing por delta

> **Só começar com as Fases 1 e 2 concluídas e validadas.**

## Problema

Toda a animação e o movimento do mapa avançam **por frame renderizado**, não por
tempo:

- `AnimatedEntity.update()`: `atual += offsetFrames`
- `Attack.update()`: idem
- `CombatForm.update()`: `progress += clip.getOffsetFrames()`
- `PlayScreen`: o mapa desloca `mapSpeed` pixels por frame

Com vsync (padrão do LWJGL3), num monitor de 144 Hz o jogo roda ~2,4× mais rápido
que num de 60 Hz. Só o `WAIT_TIME` do combate usa tempo real.

Isto é **ortogonal aos padrões aplicados** (State, Template Method, Strategy,
Singleton, Command). Nenhuma assinatura conceitual muda de papel; muda a
aritmética interna. Por isso é uma fase isolada, validada visualmente.

## Design alvo

Preservar **exatamente** o comportamento atual a 60 Hz e torná-lo independente do
refresh rate. Os valores de `offsetFrames` em `GameConstants` e no `Bestiary`
significam hoje "frames de animação por frame renderizado"; a 60 fps isso equivale
a `offsetFrames * 60` frames de animação por segundo. Logo:

```
atual += offsetFrames * REFERENCE_FPS * delta       // REFERENCE_FPS = 60
```

Nenhum valor de constante precisa ser recalibrado.

## Implementação

### 4.1 — `GameConstants`: constante de referência e velocidade do mapa

```java
/**
 * FPS de referencia do port original (Pygame/clock.tick 60). Os offsets de
 * animacao foram calibrados como "frames por frame renderizado" a 60 fps;
 * multiplicar por REFERENCE_FPS * delta preserva a velocidade original em
 * qualquer refresh rate.
 */
public static final float REFERENCE_FPS = 60f;

/** Passo maximo de simulacao (evita saltos gigantes apos um stutter/breakpoint). */
public static final float MAX_DELTA = 1f / 30f;

// 6.2 - Speed
public static final float mapSpeed = 6f;                          // px por frame a 60 fps (mantido p/ referencia)
public static final float mapSpeedPerSecond = mapSpeed * REFERENCE_FPS; // 360 px/s
```

**Atenção à `tolerance`:** hoje é `mapSpeed * 0.5f` = 3 px — uma janela de
detecção dimensionada para um passo fixo de 6 px. Com passo variável, um frame
lento (30 fps → 12 px) atravessa a janela inteira e o `FightPoint` **nunca é
detectado**. Ver 4.4.

### 4.2 — `AnimatedEntity`: `update(float delta)`

```java
public void update(float delta) {
    if (atual > getQtdFrames() - 1) {
        atual = 0;
    }
    image = frame[Math.round(atual)];
    atual += getOffsetFrames() * GameConstants.REFERENCE_FPS * delta;
}
```

Trocar a assinatura de `update()` para `update(float delta)` (não manter overload
sem parâmetro — a ideia é que o compilador aponte todos os chamadores).

### 4.3 — `Attack` e `CombatForm`

`Attack.update(float delta)`:

```java
@Override
public void update(float delta) {
    if (loopsCompletos >= limiteLoops) {
        loopsCompletos = 0;
        isOver = true;
    } else {
        image = frame[Math.round(atual)];
        atual += getOffsetFrames() * GameConstants.REFERENCE_FPS * delta;
        if (atual > getQtdFrames() - 1) {
            atual = 0;
            loopsCompletos++;
        }
    }
}
```

`CombatForm.update(float delta)` — os dois pontos onde aparece
`progress += clip.getOffsetFrames()` passam a
`progress += clip.getOffsetFrames() * GameConstants.REFERENCE_FPS * delta`.
A lógica de estados (IDLE em loop, ATTACK/HURT voltando a IDLE, DEATH segurando o
último frame) **não muda**.

`isDeathAnimationFinished()` continua comparando `progress > qtdFrames - 1` —
segue correto, é só a taxa de crescimento de `progress` que mudou.

### 4.4 — `PlayScreen`: movimento em px/s e tolerância adaptativa

No `update(delta)` da Fase 1, aplicar o clamp e converter o passo:

```java
float dt = Math.min(delta, GameConstants.MAX_DELTA);

if (instances.playerObject.getMoving()) {
    instances.playerObject.update(dt);
    float step = currentMapOffset * GameConstants.REFERENCE_FPS * dt;
    // ... somar 'step' em vez de 'currentMapOffset' nos dois ramos (vertical/horizontal)

    // Tolerancia proporcional ao passo real deste frame: com passo variavel, uma
    // janela fixa de 3px pode ser atravessada inteira num frame lento e o
    // FightPoint nunca dispararia.
    instances.gameMap.setTol(Math.max(GameConstants.tolerance, Math.abs(step) * 0.5f));
}
```

`currentMapOffset` continua sendo `±GameConstants.mapSpeed` (valor por frame de
referência) — a multiplicação por `REFERENCE_FPS * dt` faz a conversão. Alternativa
equivalente: passar `currentMapOffset` a usar `mapSpeedPerSecond` e multiplicar só
por `dt`; escolher **uma** das duas e ser consistente.

### 4.5 — `CombatScreen`: repassar o delta

Todos os `update()` do `update(float delta)` recebem `delta`:

```java
instances.furyOfTheEye.update(delta);
playerLastAtk.update(delta);
enemyLastAtk.update(delta);
encounter.boss.update(delta);
instances.playerCombatForm.update(delta);
```

## Critério de aceite

- `./gradlew build` limpo.
- A 60 Hz, tudo visualmente idêntico ao anterior: velocidade das animações de
  idle/ataque/dano/morte, duração dos efeitos de ataque, velocidade do mapa.
- Forçando `config.useVsync(false)` no `DesktopLauncher` (teste temporário, não
  commitar): o jogo roda na mesma velocidade a centenas de fps.
- Chegar em todos os 5 `FightPoint` continua funcionando, inclusive vindo de longe.

---

# Fase 5 (micro) — Timer do turno no passo certo

Depois da Fase 2. Commit próprio, 2 linhas.

`CombatScreen.handleInput()` começa com:

```java
elapsedSinceLastClick += Gdx.graphics.getDeltaTime();
```

Isto viola o contrato do Template Method: `handleInput` reage ao toque,
`update(delta)` faz o mundo avançar — e um timer de turno é mundo avançando. Além
disso, ignora o `delta` que o template já entrega e vai buscar em `Gdx.graphics`.

**Correção:** remover a linha do `handleInput` e colocá-la como primeira linha do
`update(float delta)`, usando o parâmetro:

```java
elapsedSinceLastClick += delta;
```

**Cuidado de fidelidade:** o `handleInput` roda antes do `update` no template, e o
teste `turnoAtual != Turn.PLAYER` do `handleInput` passa a ver o timer com um
frame de atraso. Como as comparações são contra 11 s e 5,5 s, um frame é
irrelevante — mas validar que um combate completo (ataque do jogador → ataque do
inimigo → volta do turno) continua com a mesma cadência.

---

# Fase 6 (micro) — Remover `CombatEncounter.bossAtks`

Depois da Fase 3. Commit próprio.

`CombatEncounter.bossAtks` é uma segunda fonte de verdade para a mesma lista: o
`CombatForm` do chefe já a guarda em `getAtkList()`. O campo é público e nunca
lido em lugar nenhum.

**Correção:** remover o campo e o parâmetro correspondente do construtor; ajustar
a chamada em `SpecEncounterManifest.build(...)`:

```java
return new CombatEncounter(boss, bossHpText, spec.maxHp(), descriptors);
```

A variável local `atks` continua necessária para construir o `CombatForm`.

---

# Checklist final

- [ ] Fase 1 — `update` na `PlayScreen` (build + `git diff` + validação)
- [ ] Fase 2 — reset em `setEncounter` (build + `git diff` + validação)
- [ ] Fase 3 — simetria de refcount (build + `git diff` + validação)
- [ ] Fase 4 — timing por delta (build + `git diff` + validação a 60 Hz e sem vsync)
- [ ] Fase 5 — timer do turno no `update`
- [ ] Fase 6 — remover `bossAtks`
- [ ] `CLAUDE.md` atualizado se alguma convenção mudou (ex.: "todo avanço de
      animação usa `update(delta)`")
