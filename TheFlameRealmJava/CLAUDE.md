# The Flame Realm 2.0

Jogo de RPG de turnos feito em **Java + libGDX**. É um port de um jogo original
escrito em Python/Pygame — muitos arquivos são portes diretos, e os comentários
no código citam o arquivo Python de origem (ex.: "Port de Button.py"). Perspectiva
side-view no combate e bird-view fora de combate.

## Stack e build

- Java 17 (`sourceCompatibility`/`targetCompatibility = VERSION_17`)
- libGDX `1.14.2`
- Gradle (wrapper incluso: `./gradlew`)
- Módulos: `core` (todo o jogo) e `desktop` (launcher)

Comandos:

- Compilar: `./gradlew build`
- Rodar: `./gradlew desktop:run`
- Entrada: `desktop/.../DesktopLauncher.java` → `mainClass = com.flamerealm.game.desktop.DesktopLauncher`

## Estrutura (`core/src/main/java/com/flamerealm/game/`)

- `FlameRealmGame` — classe `Game`; guarda `batch`, `assets`, `instances` e as
  instâncias singleton de cada tela (`game.mainMenu`, `game.play`, `game.combat`, etc.).
- `GameConstants` — port literal de `parameters.py` para UI/mapa/player (cores,
  posições, dimensões, fontes, stats do player). Os parâmetros de **inimigos**
  (corpo, animações, ataques) saíram daqui e viraram dados declarativos em
  `characters/Bestiary` — ver abaixo.
- `instances/GameInstances` — monta TODOS os objetos do jogo (textos, botões,
  ataques, personagens, mapa) numa ordem de dependência fixa. É o "composition root".
- `screens/` — `BaseScreen` (base abstrata) + 7 telas: `MainMenuScreen`,
  `HistoryScreen`, `TutorialScreen`, `PlayScreen`, `CombatScreen`, `PauseScreen`,
  `DeathScreen`.
- `ui/` — `GameText`, `GameButton` (abstrata) → `RectButton`, `TextButton`,
  `SpriteButton`; `UiFactory` (helpers para montar UI a partir de Assets+Constants).
- `attacks/`, `characters/`, `gamemap/`, `animation/` — modelo de domínio.
- `Assets` — carregamento de texturas e fontes (BitmapFont via FreeType).

### Inimigos data-driven (`Bestiary` + `SpecEncounterManifest`)

- `characters/Bestiary` — registro estático de `EnemySpec` (um por inimigo:
  nome, HP, `CombatBodySpec` e lista de `BossAttackSpec`). Adicionar um inimigo
  novo é declarar uma constante aqui, sem criar classe nova.
- `assets/SpecEncounterManifest` — `EncounterManifest` genérico dirigido por um
  `EnemySpec`; substitui manifestos por-chefe. Cada `FightPoint` em
  `GameInstances` recebe `new SpecEncounterManifest(Bestiary.X)`.
- `animation/AnimState` (`IDLE/ATTACK/HURT/DEATH`) — estados de animação
  compartilhados entre `CombatForm` (inimigo) e `PlayerCombatForm` (player).
  `CombatForm` guarda um `AnimatedEntity` por estado presente no spec;
  `setState(...)` é no-op se o combatente não tiver clipe para aquele estado
  (ex.: um inimigo só com IDLE). IDLE toca em loop; ATTACK/HURT tocam uma vez e
  voltam a IDLE; DEATH toca uma vez e segura o último frame — `CombatScreen`
  aguarda `isDeathAnimationFinished()` antes de trocar de tela.
- `animation/AnimationSpec` aceita um `sheetOffset` opcional (canto onde a
  fileira do estado começa na sheet), usado por `AnimatedEntity` ao fatiar os
  frames — suporta sheets multi-linha (várias animações numa imagem só), como
  o corpo do Necromancer.

## Convenções importantes

- **Câmera yDown (Y cresce para baixo)**, imitando o Pygame original. Por isso
  `Assets.flippedRegion` inverte sprites verticalmente e as posições de UI usam
  o canto superior-esquerdo como referência. Não "conserte" isso para o padrão
  libGDX (yUp) sem avaliar o impacto em todas as telas.
- Todo desenho passa por um único `SpriteBatch` compartilhado (`game.batch`),
  entre `batch.begin()` e `batch.end()`.
- `GameText.draw(batch, x, y)` desenha a partir de (x, y); hoje as coordenadas
  são percentuais "calibrados no olho" (ex.: `SCREEN_WIDTH * 0.4f`), não
  centralização real medida.
- `GameInstances` mantém referências compartilhadas de `GameText` entre texto e
  botão (o mesmo objeto), para que `setMessage(...)` reflita nos dois. Preservar.
- `PlayScreen` e `CombatScreen` são singletons reaproveitados e guardam estado
  entre frames (offset do mapa, turno, timers). Preservar esse estado ao refatorar.

## Preferências de trabalho

- Fidelidade ao comportamento do original é prioridade; comentários marcam
  "quirks" preservados de propósito (ex.: botão verificado mas nunca desenhado).
- Ao refatorar, rodar `./gradlew build` e mostrar o `git diff` antes de finalizar.
- Uma tarefa de cada vez: não misturar refatoração estrutural com mudança visual
  (ex.: Template Method e alinhamento de texto são passos separados).
