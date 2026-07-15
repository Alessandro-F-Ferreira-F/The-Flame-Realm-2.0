# Refatoração: Loading assíncrono + carga de assets híbrida lazy

## Objetivo

Trocar a carga de assets **bloqueante e eager** de hoje por um pipeline
**assíncrono** baseado no `AssetManager` do libGDX, e introduzir uma
`LoadingScreen` reutilizável (com efeito visual de transição) para mascarar a
carga sob demanda. A estratégia de assets passa a ser **híbrida lazy**: um
núcleo fica sempre residente em memória e cada encontro/região é carregado só
quando necessário — preparando a expansão futura de mapas e inimigos sem
estourar o tempo de boot nem a memória.

Esta é uma refatoração **estrutural + visual** dividida em fases. Regra do
`CLAUDE.md`: **uma tarefa de cada vez**. Cada fase compila (`./gradlew build`),
mostra `git diff` e é validada antes de abrir a próxima. Não misturar fases.

## Estado atual (ponto de partida)

- O `AssetManager` existe mas está **neutralizado**: `Assets.texture()` faz
  `assetManager.load(path)` seguido de `assetManager.finishLoadingAsset(path)`,
  que **bloqueia** até o asset carregar. Na prática é síncrono.
- `GameInstances` é um composition root que constrói **todos** os objetos do
  jogo no construtor (todos os campos `final`), chamando `assets.texture(...)`.
  É invocado uma única vez no `FlameRealmGame.create()`.
- Resultado: hoje já existe um "loading invisível" no boot — a janela só aparece
  depois que 100% dos assets estão em memória; daí em diante toda troca de tela é
  instantânea porque nada mais carrega.
- As 7 telas são singletons reaproveitados; `BaseScreen` já implementa o
  Template Method `handleInput → update → draw` (o passo `update(delta)` é onde a
  `LoadingScreen` vai dirigir o `assetManager.update()`).

## Contexto técnico crítico (não errar)

### Mecânica do AssetManager assíncrono

- `assetManager.load(path, Type.class)` **enfileira** (não bloqueia).
- `assetManager.update()` processa um naco de trabalho e retorna `true` quando a
  fila esvaziou. **Deve ser chamado uma vez por frame** (no passo `update` da
  `LoadingScreen`).
- `assetManager.getProgress()` retorna `0f..1f` para a barra.
- `assetManager.get(path, Type.class)` só é válido **depois** que o asset
  carregou. Nada de `finishLoadingAsset` no caminho assíncrono.
- `AssetDescriptor<T>` é a unidade declarativa de um asset (path + tipo). É o que
  os manifestos vão listar.

### Sequência de boot muda

Como a carga vira assíncrona, **não dá para construir o `GameInstances` no
`create()`** — os assets ainda não estão em memória. A nova sequência:

1. `create()`: monta `Assets`, `batch`, `camera` (yDown), enfileira o **grupo
   núcleo**, e entra numa tela de boot.
2. A tela de boot dirige `assetManager.update()` a cada frame; ao terminar,
   **então** constrói `GameInstances` (agora `get(...)` é instantâneo), constrói
   as telas e vai para o menu.

A tela de boot só pode depender de assets triviais (o `whitePixel` para a barra e,
no máximo, uma fonte gerada na hora), nunca de algo que ainda está na fila.

### Restrições herdadas (preservar sempre)

- **Câmera yDown** (`setToOrtho(true, ...)`) e fontes com `flip = true`. Não
  "consertar" para yUp. A `LoadingScreen` desenha no mesmo esquema.
- Todo desenho passa pelo `SpriteBatch` compartilhado, entre `begin()`/`end()`.
- Preservar a referência compartilhada de `GameText` entre texto e botão
  (`setMessage` reflete nos dois) — não recriar esses objetos.
- Preservar os quirks comentados (ex.: `returnToMainMenuButton` verificado mas
  nunca desenhado no `PlayScreen`).
- Preservar o estado transiente dos singletons (turno/timer do Combat, offset do
  mapa no Play).

## Divisão de assets (alvo da estratégia híbrida lazy)

**Núcleo residente** (carregado no boot, nunca descarregado):

- Todas as fontes.
- UI: botões (movimento, pause, retorno), fundos de menu e de batalha.
- Player: sprites de caminhada (up/down/left/right) e combat form.
- **Ataques do player** (Azuring, Nebula, Dual Vortex, Razor) — são do jogador,
  sempre presentes no combate.
- Fury of the eye.
- Mapa base (`GameMap.png`). Quando houver múltiplas regiões, cada região extra
  vira um grupo lazy.

**Grupos lazy** (carregados sob demanda, descarregáveis):

- Encontro (chefe): spritesheet do chefe + seus ataques. Hoje só o Thead Darkus
  (+ Elden Ring, Flamelash, Dark Vortex, Fire Soul), mas o mecanismo deve tornar
  trivial adicionar o chefe nº 2.

---

## Fase 0 — Andaime assíncrono no `Assets` + tela de boot

**Estrutural. Sem mudança visual perceptível além do loading de boot.** Nesta
fase **tudo** continua residente (inclusive o chefe); só trocamos o *como* carrega.

### `Assets` — nova API assíncrona (manter a antiga como fallback)

```java
/** Enfileira um asset (nao bloqueia). */
public void queue(String path, Class<?> type) {
    if (!assetManager.isLoaded(path)) {
        assetManager.load(path, type);
    }
}

/** Enfileira uma lista de descriptors. */
public void queue(Array<AssetDescriptor<?>> descriptors) {
    for (AssetDescriptor<?> d : descriptors) {
        if (!assetManager.isLoaded(d.fileName)) {
            assetManager.load(d);
        }
    }
}

/** Chamado 1x por frame pela LoadingScreen. true = fila vazia. */
public boolean updateLoading() {
    return assetManager.update();
}

public float getProgress() {
    return assetManager.getProgress();
}

/** Obtem um asset ja carregado (sem bloquear). */
public <T> T get(String path, Class<T> type) {
    return assetManager.get(path, type);
}
```

- Manter `texture()`/`flippedRegion()`/`font()`/`whitePixel()` como estão para
  não quebrar call sites; internamente `flippedRegion()` continua usando o cache.
- **`flippedRegion(path)` e qualquer `get(...)` só podem ser chamados após o
  path estar carregado.** No boot isso é garantido pela tela de boot.

### Manifesto do núcleo

Criar `assets/CoreAssets.java` (ou `assets/AssetGroup` + implementação) que
devolve os `AssetDescriptor` do grupo núcleo — todos os textures listados na
seção "Núcleo residente". Fontes continuam geradas via FreeType em `Assets.font`
(baratas; ficam eager para reduzir risco nesta fase).

```java
public interface AssetGroup {
    Array<AssetDescriptor<?>> descriptors();
}
```

### `FlameRealmGame` — nova sequência de boot

```java
@Override
public void create() {
    assets = new Assets();
    batch = new SpriteBatch();
    camera = new OrthographicCamera();
    camera.setToOrtho(true, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    batch.setProjectionMatrix(camera.combined);

    assets.queue(CoreAssets.INSTANCE.descriptors());
    setScreen(new BootLoadingScreen(this)); // constroi instances/telas ao terminar
}

/** Chamado pela BootLoadingScreen quando o nucleo terminou de carregar. */
public void onCoreLoaded() {
    instances = new GameInstances(assets);
    mainMenu = new MainMenuScreen(this);
    history  = new HistoryScreen(this);
    tutorial = new TutorialScreen(this);
    play     = new PlayScreen(this);
    combat   = new CombatScreen(this);
    pause    = new PauseScreen(this);
    death    = new DeathScreen(this);
    loading  = new LoadingScreen(this); // singleton reaproveitado (Fase 1)
    setScreen(mainMenu);
}
```

- `instances` e as telas deixam de ser montadas no `create()` e passam a ser
  montadas em `onCoreLoaded()`. Continuam singletons.
- `GameInstances` **não muda** nesta fase (ainda constrói o chefe). A única
  diferença é que agora roda depois da fila esvaziar, então trocar
  `assets.texture(...)` por `assets.get(...)` internamente é opcional aqui
  (funciona dos dois jeitos), mas recomendado para deixar explícito que não
  bloqueia.

### `BootLoadingScreen` (temporária/mínima nesta fase)

`BaseScreen` simples: `update(delta)` chama `assets.updateLoading()`; quando
`true`, chama `game.onCoreLoaded()` e retorna `true`. `draw` pinta uma barra com
`whitePixel` usando `assets.getProgress()`. Sem fade ainda (vem na Fase 1).

### Validação da Fase 0

- `./gradlew build` passa.
- `git diff`: mudança concentrada em `Assets`, `FlameRealmGame`, novo
  `CoreAssets`/`AssetGroup` e `BootLoadingScreen`.
- Jogo abre com uma barra de progresso curta e cai no menu; todas as telas
  funcionam igual. Sem flicker novo.

---

## Fase 1 — `LoadingScreen` reutilizável com efeito visual (fade)

**Visual/estrutural.** Introduz a tela de loading definitiva, com transição
suave (dip-to-black) para eliminar flicks estranhos na troca. O boot passa a usá-la.

### Padrão: Command (job) + continuation (Supplier<Screen>)

Como as telas são singletons, a `LoadingScreen` **não** pode ter destino fixo.
Ela é configurada antes de virar a tela ativa:

```java
// enfileira o job de carga e diz para onde ir quando terminar
game.loading.begin(bossManifest, () -> game.combat);
game.setScreen(game.loading);
```

### Máquina de estados do fade (mata o flick)

`FADE_IN → LOADING → FADE_OUT → setScreen(next)`. Um tempo **mínimo de exibição**
evita "piscadas" quando a carga é quase instantânea.

```java
public class LoadingScreen extends BaseScreen {
    private enum Phase { FADE_IN, LOADING, FADE_OUT }

    private static final float FADE_TIME = 0.25f;   // seg
    private static final float MIN_SHOW  = 0.35f;   // tempo minimo em LOADING

    private Phase phase;
    private float fadeAlpha;      // 0..1 do overlay preto
    private float shownTime;      // tempo acumulado em LOADING
    private Runnable loadJob;     // enfileira assets (Command)
    private Supplier<Screen> next;
    private boolean jobStarted;

    public LoadingScreen(FlameRealmGame game) { super(game); }

    /** Configura o job + destino e reseta o fade. Chamar antes de setScreen. */
    public void begin(Runnable loadJob, Supplier<Screen> next) {
        this.loadJob = loadJob;
        this.next = next;
        this.phase = Phase.FADE_IN;
        this.fadeAlpha = 0f;
        this.shownTime = 0f;
        this.jobStarted = false;
    }

    @Override
    protected boolean update(float delta) {
        switch (phase) {
            case FADE_IN:
                fadeAlpha = Math.min(1f, fadeAlpha + delta / FADE_TIME);
                if (fadeAlpha >= 1f) {
                    if (!jobStarted) { loadJob.run(); jobStarted = true; } // enfileira aqui
                    phase = Phase.LOADING;
                }
                break;
            case LOADING:
                shownTime += delta;
                boolean done = game.assets.updateLoading();
                if (done && shownTime >= MIN_SHOW) {
                    onLoaded();            // hook: construir objetos do grupo (Fase 2)
                    phase = Phase.FADE_OUT;
                }
                break;
            case FADE_OUT:
                fadeAlpha = Math.max(0f, fadeAlpha - delta / FADE_TIME);
                if (fadeAlpha <= 0f) {
                    game.setScreen(next.get());
                    return true;           // aborta frame: nao desenha por cima da nova tela
                }
                break;
        }
        return false;
    }

    /** Sobrescrito/estendido na Fase 2 para construir o CombatEncounter pos-carga. */
    protected void onLoaded() { }

    @Override
    protected void draw(float delta) {
        // fundo + barra de progresso (whitePixel), respeitando camera yDown
        float p = game.assets.getProgress();
        // ... desenha barra com p ...
        // overlay preto por cima com alpha = fadeAlpha (dip-to-black)
        batch.setColor(0, 0, 0, fadeAlpha);
        batch.draw(assets.whitePixel(), 0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        batch.setColor(Color.WHITE);
    }
}
```

> Nota sobre o alpha: exige blending habilitado (padrão no `SpriteBatch`) e a
> textura `whitePixel` em RGBA. O dip-to-black é auto-contido na `LoadingScreen`
> (não precisa renderizar a tela velha). O cross-fade real entre telas fica para
> a Fase futura (`TransitionScreen` com `FrameBuffer`).

### Boot passa a usar a `LoadingScreen`

`onCoreLoaded()` ainda constrói tudo; o boot pode reusar a `LoadingScreen` com um
`loadJob` que enfileira o núcleo e um `next` que devolve o menu, ganhando o mesmo
fade. (Alternativamente manter `BootLoadingScreen` só para o caso especial do
boot, já que ela roda antes de `instances` existir — decidir na implementação.)

### Validação da Fase 1

- `./gradlew build` passa.
- Boot e (quando existir) qualquer transição via loading fazem fade suave, sem
  flick, com tempo mínimo respeitado.
- Nenhuma regra de jogo mudou.

---

## Fase 2 — Split do `GameInstances` + encontro lazy + Play→Combat

**Estrutural. É aqui que a estratégia híbrida lazy realmente entra.** O chefe
deixa de ser residente e passa a carregar sob demanda ao entrar em combate.

### `GameInstances` → núcleo residente

Remover do `GameInstances` os campos específicos do chefe:
`theadDarkus`, `theadDarkusAtks` (e os `BossAttack` individuais), e o
`theadDarkusHpText` (é específico do chefe). Tudo o mais (player, ataques do
player, mapa, UI, fundos, `furyOfTheEye`, `playerCombatFormHpText/ManaText`)
continua residente e `final`.

### `CombatEncounter` (holder lazy) + factory

```java
/** Objetos de dominio de um encontro especifico, construidos POS-carga. */
public class CombatEncounter {
    public final CombatForm<BossAttack> boss;
    public final List<BossAttack> bossAtks;
    public final GameText bossHpText;

    public CombatEncounter(CombatForm<BossAttack> boss, List<BossAttack> bossAtks, GameText bossHpText) {
        this.boss = boss;
        this.bossAtks = bossAtks;
        this.bossHpText = bossHpText;
    }
}
```

```java
/** Manifesto + construcao de um encontro. Um por chefe/regiao. */
public interface EncounterManifest extends AssetGroup {
    /** So chamar depois que descriptors() terminaram de carregar. */
    CombatEncounter build(Assets assets);
}
```

- `TheadDarkusManifest implements EncounterManifest`: `descriptors()` lista o
  spritesheet do chefe + os 4 ataques; `build(assets)` constrói o `CombatForm`,
  os `BossAttack` (via `assets.get(...)`) e o `bossHpText`. É a mesma lógica que
  hoje está no construtor do `GameInstances`, só que movida para cá e disparada
  pós-carga.

### `CombatScreen` lê o encontro atual

- Novo campo `private CombatEncounter encounter;` + `setEncounter(...)`.
- Trocar todas as referências `instances.theadDarkus` → `encounter.boss`,
  `instances.theadDarkusHpText` → `encounter.bossHpText`, etc.
- `show()` continua resetando `usedFuryOfTheEye`. O `encounter` é setado **antes**
  de a tela virar ativa (pela `LoadingScreen.onLoaded`).

### `LoadingScreen.onLoaded()` constrói o encontro

Estender a `LoadingScreen` (ou passar um segundo Command de "build") para que,
ao terminar a carga, construa o `CombatEncounter` via `manifest.build(assets)` e
faça `game.combat.setEncounter(...)` antes do `FADE_OUT`.

### `PlayScreen` roteia combate pela `LoadingScreen`

No ponto onde hoje faz `game.setScreen(game.combat)` (dentro do `draw`, quirk
preservado), passar a:

```java
game.loading.begin(theadDarkusManifest); // job = enfileira + marca manifest p/ build
game.setScreen(game.loading);
```

com a continuação apontando para `game.combat`. **Preservar** que a transição
parte de dentro do `draw` (restrição do `CLAUDE.md`).

### Pontos críticos a preservar

1. A construção do chefe é **idêntica** à de hoje, só mudou de lugar e de momento
   (pós-carga em vez de boot).
2. Referências compartilhadas de `GameText` do chefe: o `bossHpText` é standalone
   (não tem botão associado), então não há armadilha de referência compartilhada.
3. O `revive`/reset de HP do chefe no `CombatScreen` passa a mexer em
   `encounter.boss` — conferir que o `setMessage` do `bossHpText` continua batendo.

### Validação da Fase 2

- `./gradlew build` passa.
- `git diff`: chefe sai do `GameInstances`; nasce `CombatEncounter` +
  `EncounterManifest` + `TheadDarkusManifest`; `CombatScreen` lê o encontro.
- Jogar os 4 cenários: vencer (→ play), perder (→ death), gastar mana no limite,
  usar fury of the eye. Entrar em combate mostra o fade de loading; o chefe
  aparece corretamente após a carga.

---

## Fase 3 — Unload do encontro + múltiplos encontros/regiões

**Estrutural.** Fecha o ciclo de memória e abre a expansão.

- Ao sair do combate (vitória/derrota), descarregar o grupo do encontro via
  `assetManager.unload(path)` para cada descriptor do manifesto — **cuidando** de
  não descarregar assets compartilhados com o núcleo (nenhum, pela divisão atual,
  mas validar quando surgirem chefes que reusem sprites).
- Generalizar: um `EncounterManifest` por chefe; o `FightPoint`/região escolhe
  qual manifesto carregar. Regiões de mapa extras viram grupos lazy análogos.
- Opcional: contador de refs do `AssetManager` já cuida de assets compartilhados
  entre grupos — preferir `unload` simétrico ao `load`.

### Validação da Fase 3

- `./gradlew build` passa.
- Entrar e sair de combate várias vezes não vaza memória (o grupo do chefe
  aparece/some do `assetManager`). O núcleo nunca é descarregado.

---

## Fase futura (documentada, NÃO implementar agora) — `TransitionScreen` (cross-fade real)

Para transições com **cross-fade verdadeiro** (tela velha some enquanto a nova
aparece), além do dip-to-black da `LoadingScreen`:

- `TransitionScreen extends BaseScreen` que captura a tela de saída e a de entrada
  em `FrameBuffer` (render-to-texture) e mistura as duas com um alpha animado.
- Cuidados: `FrameBuffer` na resolução fixa 900×600; respeitar a câmera yDown ao
  desenhar as texturas capturadas (flip vertical, como `flippedRegion`); `dispose`
  dos framebuffers.
- Encaixa no mesmo padrão Command + continuation da `LoadingScreen`. Pode coexistir
  como uma segunda estratégia de transição (dip-to-black para loadings longos;
  cross-fade para trocas leves de tela).

---

## Resumo das fases

| Fase | Entrega | Tipo | Risco |
|------|---------|------|-------|
| 0 | Pipeline assíncrono no `Assets` + boot loader; sequência de boot nova | Estrutural | Baixo |
| 1 | `LoadingScreen` reutilizável com fade (dip-to-black) + tempo mínimo | Visual/estrutural | Baixo |
| 2 | Split do `GameInstances` + `CombatEncounter`/`EncounterManifest` + Play→Combat lazy | Estrutural | Médio |
| 3 | Unload do encontro + múltiplos encontros/regiões | Estrutural | Médio |
| Futura | `TransitionScreen` (cross-fade via `FrameBuffer`) | Visual | — |

## Verificação geral (toda fase)

- `./gradlew build` compila.
- `./gradlew desktop:run` — conferir visualmente (loading só o olho valida:
  sem flick, fade suave, barra progredindo).
- Mostrar `git diff` de todos os arquivos alterados antes de fechar a fase.
- Preservar: câmera yDown + `flip=true`; `SpriteBatch` único; referências
  compartilhadas de `GameText`; quirks comentados; estado transiente dos
  singletons.
