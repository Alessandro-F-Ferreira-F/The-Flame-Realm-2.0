# Refatoração: reusar a `LoadingScreen` na transição Combat → Play (vitória)

## Objetivo

Suavizar o **corte seco** que hoje acontece quando o jogador vence o chefe e volta
para o mapa. Reaproveitar a `LoadingScreen` (que já mascara a ida Play → Combat com
um fade dip-to-black) também na volta **Combat → Play**, deixando as duas transições
simétricas.

Decisão tomada: **reusar a `LoadingScreen` existente**, e não extrair uma nova
`TransitionScreen`. A `LoadingScreen` já é uma transição genérica
(`begin(loadJob, postLoadJob, next)`); só criaremos uma abstração nova se um segundo
caso de uso de fade-sem-carga aparecer. Não criar abstração antes de ter dois clientes.

Isto é uma refatoração **estrutural + visual**. Regra do `CLAUDE.md`: **uma tarefa de
cada vez**. Fazer só este ramo, compilar (`./gradlew build`), mostrar `git diff` e
validar antes de mexer em qualquer outra coisa.

## Escopo (o que entra e o que NÃO entra)

- **Entra:** apenas o ramo de **vitória** do `CombatScreen` (boss HP == 0), que hoje
  chama `game.setScreen(game.play)` direto.
- **NÃO entra:** o ramo de **morte** (player HP == 0), que vai para `game.death` — é
  um exit distinto, com outra tela e outra lógica (não desativa o `FightPoint`).
- **NÃO entra:** suavizar Death → Play nem a própria tela de morte. Fica para uma
  tarefa futura separada, se desejado.

## Contexto técnico (estado atual)

### Ida Play → Combat (`PlayScreen`, já usa loading)

```java
EncounterManifest manifest = instances.gameMap.getCurrentPoint().getManifest();
game.loading.begin(
        () -> assets.queue(manifest.descriptors()),
        () -> game.combat.setEncounter(manifest.build(assets)),
        () -> game.combat);
game.setScreen(game.loading);
```

### Volta Combat → Play (`CombatScreen.update()`, ramo de vitória — HOJE, síncrono)

```java
} else if (encounter.boss.getHealthPoints() == 0) {
    turnoAtual = Turn.PLAYER;
    instances.playerCombatForm.revive(GameConstants.playerHp);
    instances.playerCombatFormHpText.setMessage("HP: " + instances.playerCombatForm.getHealthPoints());

    instances.playerCombatForm.setManaPoints(GameConstants.playerMana);
    instances.playerCombatFormManaText.setMessage("Mana: " + instances.playerCombatForm.getManaPoints());

    encounter.boss.revive(encounter.maxHp);
    encounter.bossHpText.setMessage("HP: " + encounter.maxHp);

    instances.gameMap.disableCurrentPoint();
    instances.gameMap.setPreviousPoint(instances.gameMap.getCurrentPoint());
    assets.unload(encounter.descriptors);
    encounter = null;
    game.setScreen(game.play);
    return true;
}
```

### Máquina de fade (`FadeGate`)

`FADE_IN` (0,25s) → `LOADING` (mín. `MIN_SHOW = 1s`, espera `assets.updateLoading()`)
→ `FADE_OUT` (0,25s). A `LoadingScreen`:

- roda `loadJob` no **fim do FADE_IN** (tela já preta);
- roda `postLoadJob` no **fim do LOADING**;
- troca para `next` no **fim do FADE_OUT**.

## Design alvo

Reescrever **só** o ramo de vitória para delegar à `LoadingScreen`:

- **loadJob:** `assets.unload(enc.descriptors)` — o descarregamento passa a acontecer
  com a tela já preta (fim do FADE_IN), escondendo o hitch de dispose das texturas do
  chefe. Hoje ele roda no meio de um frame ainda visível.
- **postLoadJob:** todo o reset de estado (revive player + boss, textos de HP/mana,
  `disableCurrentPoint`, `setPreviousPoint`).
- **next:** `() -> game.play`.

A ordem entre fases preserva a sequência atual: `disableCurrentPoint` continua rodando
**antes** de o `PlayScreen` voltar a ficar ativo, então o `fightPointTest` cai no ramo
"else" (ponto já desativado) e **não** re-entra em combate.

### Cuidado obrigatório: capturar `encounter` num local final

A `CombatScreen` é singleton e `encounter` é campo. Os jobs rodam vários frames depois.
Se as lambdas lerem `this.encounter` depois de o campo virar `null`, dá **NPE**.
Capturar uma cópia local **antes** e zerar o campo por último:

```java
} else if (encounter.boss.getHealthPoints() == 0) {
    final CombatEncounter enc = encounter;   // captura antes de zerar o campo
    turnoAtual = Turn.PLAYER;

    game.loading.beginTransition(
        () -> assets.unload(enc.descriptors),                 // loadJob: descarrega no preto
        () -> {                                               // postLoadJob: reset de estado
            instances.playerCombatForm.revive(GameConstants.playerHp);
            instances.playerCombatFormHpText.setMessage("HP: " + instances.playerCombatForm.getHealthPoints());
            instances.playerCombatForm.setManaPoints(GameConstants.playerMana);
            instances.playerCombatFormManaText.setMessage("Mana: " + instances.playerCombatForm.getManaPoints());

            enc.boss.revive(enc.maxHp);
            enc.bossHpText.setMessage("HP: " + enc.maxHp);

            instances.gameMap.disableCurrentPoint();
            instances.gameMap.setPreviousPoint(instances.gameMap.getCurrentPoint());
        },
        () -> game.play);

    encounter = null;            // zera o campo por último
    game.setScreen(game.loading);
    return true;
}
```

> Observação: `revive`, `setMessage`, `maxHp` são operações sobre `int`/estado — não
> tocam textura. Então rodar o reset **depois** do `unload` é seguro (nenhuma textura
> descarregada é lida ou desenhada; a `CombatScreen` já não é a tela ativa).

## Ajustes na `LoadingScreen`

A `LoadingScreen` foi feita para **mascarar carga**. Aqui não há carga — só
descarregar e trocar de cena. Dois problemas visuais surgem e precisam de correção:

### 1. Barra de progresso piscando cheia

Sem nada na fila, `assets.getProgress()` retorna `1.0`, então a `draw()` pinta a barra
verde 100% preenchida, que aparece no FADE_IN, some no preto e reaparece no FADE_OUT.
Fica estranho numa transição sem carga. **Só desenhar a barra quando há carga real.**

### 2. Tempo de preto longo demais na volta

`MIN_SHOW = 1s` + 2×0,25s ≈ **1,5s** de preto. Faz sentido entrando no combate (dá
tempo de a barra aparecer), mas parado 1,5s depois de matar o boss parece artificial.
**Encurtar o min-show para transições sem carga.**

### Design sugerido: overload `beginTransition(...)` + flag `showBar`

Adicionar um caminho "transição pura" sem mexer no comportamento atual de carga:

```java
// LoadingScreen

private boolean showBar = true;

/** Transição sem carga real (fade + jobs), barra oculta e hold curto. */
public void beginTransition(Runnable loadJob, Runnable postLoadJob, Supplier<Screen> next) {
    this.showBar = false;
    begin(loadJob, postLoadJob, next);
    // Se optar por hold configurável, chamar aqui fade.setMinShow(...) com um valor menor.
}

// Nos begin(...) existentes, restaurar showBar = true (carga normal).
```

E na `draw()`:

```java
if (showBar) {
    batch.setColor(GameConstants.black);
    batch.draw(assets.whitePixel(), BAR_X, BAR_Y, BAR_WIDTH, BAR_HEIGHT);
    batch.setColor(GameConstants.green);
    batch.draw(assets.whitePixel(), BAR_X, BAR_Y, BAR_WIDTH * assets.getProgress(), BAR_HEIGHT);
    batch.setColor(Color.WHITE);
}
fade.drawOverlay(batch, assets.whitePixel(), GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
```

Para o hold curto (opcional, mas recomendado), expor um `setMinShow(float)` no
`FadeGate` (hoje `MIN_SHOW` é constante). Se preferir manter simples nesta primeira
iteração, deixe o hold em 1s e ajuste depois — mas **a supressão da barra é
obrigatória**, senão o flash acontece.

## Fases

1. **`FadeGate`:** transformar `MIN_SHOW` em campo com `setMinShow(float)` e default
   1s (só se for parametrizar o hold — pode ser adiado).
2. **`LoadingScreen`:** adicionar campo `showBar`, overload `beginTransition(...)`,
   restaurar `showBar = true` nos `begin(...)` de carga, e condicionar a barra na
   `draw()`.
3. **`CombatScreen`:** reescrever o ramo de vitória para usar `beginTransition(...)`
   com `encounter` capturado em local final, zerando o campo por último.

Cada fase compila e roda. Não misturar com a refatoração de `TextAlign` pendente.

## Critérios de validação

- `./gradlew build` passa.
- Vencer o chefe: fade suave até o mapa, sem barra piscando e sem preto longo demais.
- O `FightPoint` batido fica **desativado** (não re-entra em combate ao voltar).
- Morrer no chefe continua indo para a tela de morte **sem** passar pelo loading
  (ramo de morte intocado).
- Sem NPE ao voltar ao mapa (checa a captura local de `encounter`).
- Entrar no combate a partir do mapa continua igual (barra de carga visível, hold
  normal).
- `git diff` revisado antes de finalizar.
```
