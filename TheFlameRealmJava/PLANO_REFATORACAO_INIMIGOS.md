# Plano de Refatoração — Expansão de Inimigos

Documento de planejamento. Consolida as decisões tomadas até aqui para tornar o
projeto escalável antes de adicionar novos inimigos. **Nenhum código foi escrito
ainda** — este é o desenho aprovado a partir do qual a implementação vai partir.

## Objetivo

Viabilizar a adição de novos inimigos ao combate de forma data-driven, substituindo
uma aparição de Thead Darkus por vez até haver **4 inimigos distintos** no jogo (MVP).
Cada inimigo tem corpo com múltiplos estados de animação (idle, ataque, dano, morte)
e seu próprio conjunto de ataques.

Fora de escopo agora: novas áreas/mapas, tela de vitória, rebalanceamento do
"Fury of the eye", e a mecânica de combate-puzzle (ficam para depois).

## Estado atual (por que a arquitetura já ajuda)

O eixo `FightPoint → EncounterManifest → CombatEncounter → CombatScreen` já é
genérico: `CombatScreen` nunca cita Thead Darkus, `GameMap` propaga o `manifest`
em todos os pontos e `CombatForm.randomizeAtk()` aceita qualquer número de ataques.
O que falta é (a) tirar os parâmetros dos inimigos do `GameConstants` e organizá-los
em dados declarativos, e (b) dar ao corpo do combatente múltiplos estados de animação.

## Decisões tomadas

1. **Modelo data-driven com `record`s de spec.** Os parâmetros de cada inimigo e
   ataque saem do `GameConstants` (seção "5.2 Boss") e viram dados imutáveis.
   Isso **não** quebra o padrão Strategy: `EncounterManifest` segue sendo a
   estratégia; os dados que ela consome apenas passam a morar em objetos próprios.
   Fidelidade ao port de `parameters.py` deixa de ser prioridade — o objetivo agora
   é escalabilidade.

2. **Path do asset vive no spec.** `descriptors()` e `build()` passam a derivar da
   mesma lista de specs (fonte única de verdade), eliminando o array `PATHS`
   paralelo e a duplicação manual de caminhos.

3. **Manifesto genérico dirigido por spec.** Em vez de uma classe por inimigo,
   um único `SpecEncounterManifest` recebe um `EnemySpec`. Adicionar inimigo passa a
   ser declarar um spec novo — sem criar classe. A interface `EncounterManifest`
   continua permitindo um manifesto concreto sob medida caso um inimigo futuro
   precise de um `build()` especial (escape hatch preservado).

4. **Máquina de estados de animação mora no `CombatForm`.** Player e inimigo têm os
   mesmos estados (idle, ataque, dano, morte), e ambos já herdam de `CombatForm`.
   Implementando lá uma vez, os dois lados ganham a capacidade. Uma sprite nova de
   combate para o player virá logo e encaixa no mesmo mecanismo.

5. **`Bestiary` como registro dos specs.** Arquivo separado guardando os `EnemySpec`
   como constantes, substituindo a seção de boss do `GameConstants`. Combina com um
   futuro `PlayerAttackSpec`/`PlayerSpec`.

6. **Sem cópia defensiva de `Vector2`.** Como a posição do corpo não muda entre
   estados nem é mutada in-place pelo combate, passar a referência do spec é seguro.
   (Revisitar apenas se algum dia o combate mover o sprite mutando o `Vector2`.)

## Novos tipos

### Enum de estados (compartilhado player/inimigo)

```java
public enum AnimState { IDLE, ATTACK, HURT, DEATH }
```

### `AnimationSpec` — clipe de animação puro (sem posição)

Reutilizável em corpo, efeitos de ataque e, futuramente, ataques do player.

```java
public record AnimationSpec(
    String     sheetPath,   // caminho do spritesheet — fonte única p/ descriptors e build
    int        frames,      // qtdFrames
    GridPoint2 frameSize,   // pixels de cada frame (pode diferir por estado)
    float      speed        // offsetFrames
) {}
```

### `CombatBodySpec` — corpo do combatente (posição + estados)

Fatorado porque vale para inimigo e player.

```java
public record CombatBodySpec(
    Vector2 position,                       // posição fixa do corpo em combate
    Map<AnimState, AnimationSpec> states    // IDLE obrigatório; demais conforme o combatente
) {
    public CombatBodySpec {
        if (!states.containsKey(AnimState.IDLE))
            throw new IllegalArgumentException("CombatBodySpec exige ao menos IDLE");
        states = Map.copyOf(states);        // imutável
    }
}
```

### `BossAttackSpec` — ataque do inimigo (efeito com posição própria)

```java
public record BossAttackSpec(
    String name,
    int    damage,
    int    hitKillChance,
    int    loops,          // limiteLoops
    AnimationSpec clip,    // sprite de efeito (Flamelash etc.)
    Vector2       position // onde o efeito é desenhado
) {}
```

### `EnemySpec` — o inimigo

```java
public record EnemySpec(
    String  name,
    int     maxHp,
    CombatBodySpec body,
    List<BossAttackSpec> attacks
) {}
```

### Paralelo do player (quando a sprite nova chegar — não agora)

Mesma forma; diverge apenas no que é próprio do player (mana, ataques com botão).

```java
public record PlayerSpec(String name, int maxHp, int maxMana, CombatBodySpec body,
                         List<PlayerAttackSpec> attacks) {}
```

## `SpecEncounterManifest` (genérico)

```java
public final class SpecEncounterManifest implements EncounterManifest {
    private final EnemySpec spec;
    private final Array<AssetDescriptor<?>> descriptors; // derivados do spec no construtor

    public SpecEncounterManifest(EnemySpec spec) { /* coleta paths do body + ataques */ }

    @Override public Array<AssetDescriptor<?>> descriptors() { return descriptors; }

    @Override public CombatEncounter build(Assets assets) {
        // monta BossAttacks (clip+position), o CombatForm com o mapa de estados,
        // o bossHpText a partir de maxHp, e retorna o CombatEncounter.
    }
}
```

`GameInstances` deixa de referenciar `TheadDarkusManifest.INSTANCE` e passa a usar
`new SpecEncounterManifest(Bestiary.THEAD_DARKUS)` (etc.) nos `FightPoint`s. Trocar
o inimigo de um ponto vira trocar a constante do `Bestiary`.

## `Bestiary`

```java
public final class Bestiary {
    public static final EnemySpec THEAD_DARKUS = new EnemySpec("Thead Darkus", 400,
        new CombatBodySpec(
            new Vector2(0, SCREEN_HEIGHT * 0.3f),
            Map.of(AnimState.IDLE, new AnimationSpec("Images/Characters/Boss/Thead Darkus.png",
                                                     15, new GridPoint2(224, 240), 0.18f)
                   /* ATTACK, HURT, DEATH quando as sprites existirem */)),
        List.of(
            new BossAttackSpec("Flamelash", 10, 70, 3,
                new AnimationSpec("Images/Attacks/Boss/Flamelash.png", 45, new GridPoint2(100,100), 0.6f),
                new Vector2(/* flamelashPosition */)),
            // fireSoul, eldenRing, darkVortex...
        ));
    // NOVO_INIMIGO_1, NOVO_INIMIGO_2, ... aqui.
}
```

## Máquina de estados no `CombatForm` (comportamento — a detalhar)

Não muda a forma dos records; concentra-se em `CombatForm` + `CombatScreen`. Pontos
em aberto para a próxima rodada de desenho:

- **Modo de reprodução por estado:** IDLE em loop; ATTACK/HURT tocam uma vez e voltam
  ao idle; DEATH toca uma vez e segura o último frame. Decidir se é inferido do
  `AnimState` (mais simples) ou um campo explícito por estado (mais flexível).
- **Gatilhos (já existem como pontos no código):** player ataca (ATTACK no player,
  HURT no alvo); inimigo ataca (o inverso); HP zero (DEATH). Locais atuais:
  aplicação de dano em `CombatScreen` (linhas ~61 e ~140) e checagens de morte.
- **Morte espera a animação:** hoje a morte troca de tela na hora (`death`; ou
  transição para `play`). Com DEATH animado, o `setScreen` precisa aguardar a
  animação terminar. É a única parte que toca o fluxo de vitória/derrota existente.
- **Hierarquia:** `CombatForm` hoje "é-um" `AnimatedEntity` (uma animação). Com
  vários clipes, deve passar a **conter** os clipes e expor o do estado atual via
  `getImage()`/`update()`, mantendo a interface que `CombatScreen` já chama (callers
  não mudam).

## Barra de HP/Mana (correção independente)

Item isolado, pode andar em paralelo. Hoje a largura é hardcoded
(`hp * 0.4f` / `mana * 0.8f`) e as constantes `theadDarkusHpBarSize`/`playerHpBarSize`
estão mortas. Alvo:

- Largura **máxima fixa** em pixels por barra (constante).
- Preenchimento normalizado: `(atual / (float) maximo) * larguraMax`.
- Deixar pronto para virar sprite depois: a mesma fração `atual/maximo` controla o
  recorte/escala da sprite da barra.

## Confirmado sem mudança

- **HitKill:** correto como está. O proco seta o dano para `playerHp` (mata e encerra
  a luta no mesmo turno), então nunca "vaza". Comportamento é intencional (o "dado").
- **Contagem de vitórias / fim de jogo:** permanece implícita (4 `FightPoint`s
  ativados desativados um a um). O texto do tutorial ("defeat Thead Darkus four
  times") será revisto junto com conteúdo/narrativa — depois.
- **CLAUDE.md:** desatualizado; será atualizado ao final da refatoração.

## Ordem de execução sugerida

1. **Tipos base:** `AnimState`, `AnimationSpec`, `CombatBodySpec`, `BossAttackSpec`,
   `EnemySpec`. (Só código novo, nada quebra ainda.)
2. **`SpecEncounterManifest`** genérico + **`Bestiary.THEAD_DARKUS`** replicando o
   Thead Darkus atual (só IDLE, como hoje). Trocar o wiring em `GameInstances`.
   Rodar `./gradlew build` e jogar: comportamento deve ser **idêntico** ao atual.
3. **Remover** `TheadDarkusManifest` e a seção "5.2 Boss" do `GameConstants`.
   Build + teste de regressão.
4. **Máquina de estados no `CombatForm`** (idle/ataque/dano/morte), incluindo a espera
   da animação de morte. Validar com o Thead Darkus ainda usando só idle.
5. **Fatia vertical:** um inimigo novo completo (spec + assets + estados + troca em um
   `FightPoint`), ponta a ponta. Validar rodando.
6. **Replicar** para os outros 3 inimigos.
7. Barra de HP/Mana normalizada (independente; encaixar quando conveniente).
8. Atualizar `CLAUDE.md`.

> Regra de trabalho do projeto: rodar `./gradlew build` e revisar `git diff` ao fim de
> cada etapa; uma tarefa de cada vez (não misturar refatoração estrutural com mudança
> visual).
