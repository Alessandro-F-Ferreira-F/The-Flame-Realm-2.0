# Refatoração: alinhamento/centralização de texto (`TextAlign`)

## Objetivo

Centralizar os textos do jogo — na tela ou dentro dos botões — e acabar com o
vazamento dos parágrafos pela borda direita (visível em History e Tutorial).
Hoje as coordenadas de texto são percentuais "calibrados no olho" para as fontes
antigas; com as fontes novas (mais largas) o alinhamento quebrou.

Introduzir um `TextAlign` (padrão **Strategy**, implementado como `enum` — algoritmos
de posicionamento intercambiáveis, sem classe-Deus nem classe por tela) e medir o
texto com `GlyphLayout`. Esta refatoração é **visual/estrutural** e é independente
da refatoração de Template Method já feita nas telas.

## Contexto técnico crítico (não errar a matemática)

No `Assets`, as fontes são geradas com `parameter.flip = true`, e a câmera é
`camera.setToOrtho(true, ...)` (yDown, (0,0) no topo-esquerdo, Y cresce para baixo).
Com essa combinação, `font.draw(batch, texto, x, y)` posiciona **(x, y) no
topo-esquerdo** do bloco de texto e desenha para baixo.

Portanto:
- Centralização horizontal: `x = rectX + (rectW - layout.width) / 2f`
- Centralização vertical: `y = rectY + (rectH - layout.height) / 2f`  ← **menos**, não mais
  (o `+ layout.height` só valeria num setup yUp/baseline; aqui NÃO é o caso).

`GlyphLayout.width` = largura da linha mais larga; `GlyphLayout.height` = altura
total do bloco (todas as linhas). Ambos positivos, independentes do flip.

## Design alvo

### `ui/TextAlign.java` (novo)

```java
package com.flamerealm.game.ui;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public enum TextAlign {
    /** Comportamento atual: desenha a partir do canto (x, y). */
    TOP_LEFT {
        public float x(GlyphLayout l, float rx, float rw) { return rx; }
        public float y(GlyphLayout l, float ry, float rh) { return ry; }
    },
    /** Centraliza só na horizontal; y = topo do retângulo. */
    CENTER_X {
        public float x(GlyphLayout l, float rx, float rw) { return rx + (rw - l.width) / 2f; }
        public float y(GlyphLayout l, float ry, float rh) { return ry; }
    },
    /** Centraliza nos dois eixos dentro do retângulo. */
    CENTER {
        public float x(GlyphLayout l, float rx, float rw) { return rx + (rw - l.width) / 2f; }
        public float y(GlyphLayout l, float ry, float rh) { return ry + (rh - l.height) / 2f; }
    };

    public abstract float x(GlyphLayout l, float rx, float rw);
    public abstract float y(GlyphLayout l, float ry, float rh);
}
```

### `ui/GameText.java` (adicionar; NÃO remover o `draw(batch, x, y)` existente)

Campo `GlyphLayout` reaproveitado (evita alocar por frame) + duas sobrecargas:

```java
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

private final GlyphLayout layout = new GlyphLayout();

/** Centraliza (ou ancora) o texto dentro do retângulo (rectX, rectY, rectW, rectH). */
public void draw(Batch batch, TextAlign align, float rectX, float rectY, float rectW, float rectH) {
    font.setColor(color);
    layout.setText(font, message);
    font.draw(batch, layout, align.x(layout, rectX, rectW), align.y(layout, rectY, rectH));
}

/** Desenha com quebra automática dentro de targetWidth; cada linha alinhada por 'align'.
    (blockX, blockY) = topo-esquerdo do bloco. */
public void drawWrapped(Batch batch, TextAlign align, float blockX, float blockY, float targetWidth) {
    font.setColor(color);
    int halign = (align == TextAlign.TOP_LEFT) ? Align.left : Align.center;
    layout.setText(font, message, color, targetWidth, halign, true);
    font.draw(batch, layout, blockX, blockY);
}
```

Obs.: a medição usa `font` (o campo atual). Como `TextButton` troca a fonte
(regular/bold) ANTES de chamar `draw`, a medição sai correta no hover sozinha.

### `ui/TextButton.java` (centralizar rótulo no próprio botão, opcional por botão)

Adicionar um campo `TextAlign align` (default `TOP_LEFT`, que preserva o
`coordsText` atual — usado pelos botões cujo rótulo fica AO LADO do botão).
Quando `align != TOP_LEFT`, ignora `coordsText` e mede contra o retângulo do
próprio botão (`getCoords()` + `getSize()`):

```java
@Override
public void draw(SpriteBatch batch) {
    super.draw(batch);
    text.setFont(mouseInButton() ? boldFont : regularFont);
    if (align == TextAlign.TOP_LEFT) {
        text.draw(batch, coordsText.x, coordsText.y);
    } else {
        text.draw(batch, align, getCoords().x, getCoords().y, getSize().x, getSize().y);
    }
}
```

Prever um setter/segundo construtor para definir o `align` sem quebrar as chamadas
existentes de `UiFactory.textButton(...)` (default continua `TOP_LEFT`).

## Ajuste no `instances/GameInstances.java`

Remover os `\n` manuais dos parágrafos longos (viram espaço), deixando a quebra
por conta do `drawWrapped`. Afeta: `historyText`, `historySecretText`,
`tutorialMoveText`. Os títulos de uma linha (`tutorialMainText`, `tutorialManaText`)
ficam como estão. Definir `align = CENTER` para os dois botões de retorno
(`returnToMainMenuButton`, `returnToGameButton`).

## Mapa de aplicação por tela

- **PauseScreen** — `pauseText`: `CENTER` no retângulo da tela cheia
  `(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)`.
- **DeathScreen** — `deadText`: `CENTER` na tela cheia.
- **HistoryScreen** — `historyText`: `drawWrapped(CENTER, SCREEN_WIDTH*0.05f, y, SCREEN_WIDTH*0.9f)`
  (bloco na largura da tela com margem de 5% de cada lado; manter o y de topo atual).
  `historySecretText`: mesmo tratamento na posição do easter egg.
- **TutorialScreen**:
  - `tutorialMainText` (título): `drawWrapped(CENTER, SCREEN_WIDTH*0.05f, y, SCREEN_WIDTH*0.9f)`.
  - `tutorialManaText` e `tutorialMoveText`: `drawWrapped` quebrando dentro da
    REGIÃO À DIREITA dos ícones — `targetWidth` = do x atual do texto até a borda
    direita (ex.: `SCREEN_WIDTH - textoX - margem`). NÃO usar a tela cheia, para
    não passar por cima da barra de mana nem dos botões de movimento.
  - `playerCombatFormManaText` ("Mana: 100"): manter `TOP_LEFT` (colada à barra).
- **CombatScreen** — labels `HP:` / `Mana:` sobre as barras: manter `TOP_LEFT`.
  Botões de ataque (vortex/azuring/nebula/razor): manter `TOP_LEFT` (rótulo ao lado).
- **MainMenuScreen** — botões Play/History/Tutorial/Quit: manter `TOP_LEFT`
  (rótulo ao lado do quadrado; centralizar dentro do quadrado 50×50 cortaria o texto).
- **Botões de retorno** (aparecem em várias telas): `CENTER` no próprio botão.

## Restrições

1. NÃO alterar o padrão Template Method já implementado no `BaseScreen`/telas.
2. Manter a sobrecarga `GameText.draw(batch, x, y)` existente — muitos call sites
   ainda a usam (labels `TOP_LEFT`).
3. Não centralizar dentro de botões cujo rótulo fica AO LADO (menu e ataques):
   cortaria o texto.
4. Preservar a referência compartilhada de `GameText` entre texto e botão
   (`setMessage` reflete nos dois) — não recriar objetos de texto.
5. Preservar a câmera yDown e o `flip = true` das fontes (não "consertar" para yUp).

## Verificação

- Rodar `./gradlew build` e garantir que compila.
- Mostrar o `git diff` de todos os arquivos alterados.
- Rodar `./gradlew desktop:run` e conferir VISUALMENTE cada tela (Pause, Death,
  History, Tutorial, Combat, Menu): nenhum texto vazando a borda, títulos e "Game
  paused"/"YOU DIED!!" centralizados, rótulos dos botões de retorno centrados no botão.
- Alinhamento só o olho valida; não confiar apenas na compilação.
