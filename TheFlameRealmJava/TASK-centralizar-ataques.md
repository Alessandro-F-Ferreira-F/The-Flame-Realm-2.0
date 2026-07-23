# Tarefa: centralizar sprites de ataque no alvo

## Objetivo

Posicionar o sprite de cada ataque (jogador e inimigo) **centralizado no alvo**,
em vez das posições absolutas fixas de hoje. O alvo do jogador é o inimigo
(`encounter.boss`); o alvo do inimigo é o jogador (`instances.playerCombatForm`).

## Estado atual (contexto)

Cada ataque nasce com uma posição fixa e absoluta, definida em `GameConstants`
e injetada no construtor em `GameInstances`, ex.:

```java
// GameConstants.java
public static final Vector2 azuringPosition = new Vector2(SCREEN_WIDTH * 0.07f, SCREEN_HEIGHT * 0.5f);
```

Essa posição vira `AnimatedEntity.position` e **nunca é recalculada**. No
`CombatScreen.draw()` ela é usada como canto superior-esquerdo do sprite:

```java
batch.draw(playerLastAtk.getImage(), playerLastAtk.getPosition().x, playerLastAtk.getPosition().y);
```

Ou seja: o ataque não sabe quem é o alvo. Resultado: os 4 ataques do jogador
ficam todos amontoados na esquerda, independente de onde o inimigo está.

## Regra de centralização

Para desenhar um sprite de tamanho `(w, h)` centralizado no ponto `(cx, cy)`, o
canto superior-esquerdo (que é o que `batch.draw` usa) deve ser:

```
drawX = cx - w/2
drawY = cy - h/2
```

- `w, h` = tamanho do sprite do ataque na tela → usar `getImage().getRegionWidth()`
  / `getImage().getRegionHeight()` (mais fiel ao que aparece do que `getPixels()`).
- `(cx, cy)` = centro do alvo = `alvo.getPosition() + tamanhoDoSpriteDoAlvo / 2`,
  também via `getImage().getRegionWidth()/Height()`.

## Passos de implementação

Manter a convenção do projeto: mutação de estado de mundo (posição) só em
`handleInput`/`update`, nunca em `draw`. Os dois pontos de disparo já existem e
estão fora da fase `draw`.

### 1. `characters/CombatForm.java` — expor o centro do alvo

Adicionar método que devolve o centro geométrico do sprite atualmente desenhado:

```java
public Vector2 getCenter() {
    TextureRegion img = getImage();
    return new Vector2(
            position.x + img.getRegionWidth() / 2f,
            position.y + img.getRegionHeight() / 2f);
}
```

(`TextureRegion` e `Vector2` já estão importados no arquivo.)

### 2. `animation/AnimatedEntity.java` — centralizar num ponto

Adicionar método que posiciona o sprite (attack) com o centro em `(cx, cy)`,
usando o tamanho real do frame desenhado:

```java
public void centerOn(float cx, float cy) {
    position.set(
            cx - image.getRegionWidth() / 2f,
            cy - image.getRegionHeight() / 2f);
}
```

Como `Attack` estende `AnimatedEntity`, ele herda `centerOn` automaticamente.

### 3. `screens/CombatScreen.java` — chamar no disparo de cada ataque

**Ataque do jogador** — em `handleInput()`, dentro do bloco que já faz
`playerLastAtk.setIsOver(false);`, adicionar logo antes/depois:

```java
Vector2 alvo = encounter.boss.getCenter();
playerLastAtk.centerOn(alvo.x, alvo.y);
```

**Ataque do inimigo** — em `update()`, dentro do bloco `if (enemyLastAtk == null)`,
depois de `enemyLastAtk.setIsOver(false);`:

```java
Vector2 alvo = instances.playerCombatForm.getCenter();
enemyLastAtk.centerOn(alvo.x, alvo.y);
```

Importar `com.badlogic.gdx.math.Vector2` no `CombatScreen` se ainda não estiver.

## Fora de escopo (não fazer nesta tarefa)

- **Não remover** as constantes `*Position` de `GameConstants` nem os parâmetros
  de posição dos construtores de ataque ainda. Elas viram fallback morto, mas
  removê-las é uma refatoração separada (uma tarefa de cada vez).
- Não mexer no `furyOfTheEye` (não é um ataque direcionado a um alvo de combate).

## Ponto de atenção / ajuste fino

O "centro" será o centro geométrico da célula do sprite do alvo (ex.: 300×300 do
boss). Se o personagem visível não estiver centralizado dentro da célula, o
ataque vai parecer levemente deslocado. Nesse caso, adicionar um pequeno offset
de ajuste (ex.: um `Vector2` de correção por combatente) — mas só depois de ver o
resultado na tela.

## Validação

- Rodar `./gradlew build`.
- Rodar `./gradlew desktop:run` e conferir visualmente: cada ataque do jogador
  deve aparecer sobre o inimigo, e o ataque do inimigo sobre o jogador.
- Mostrar o `git diff` antes de finalizar.
