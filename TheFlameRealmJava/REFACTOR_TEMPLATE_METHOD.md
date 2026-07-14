# Refatoração: Template Method no `BaseScreen`

## Objetivo

Eliminar a repetição do invólucro `batch.begin()` / `batch.setColor(WHITE)` /
`...desenho...` / `batch.end()` que aparece no `render()` de todas as 7 telas.
Aplicar o padrão **Template Method**: o `BaseScreen` passa a ser dono do esqueleto
do frame, e cada tela só preenche os passos que variam (tratar input e desenhar).

Isto é uma refatoração **estrutural**. NÃO alterar alinhamento/coordenadas de
texto — isso é uma tarefa separada (será feita depois com `GlyphLayout`/`TextAlign`).

## Design alvo

O template é **mínimo e honesto**: o `BaseScreen` só assume o que é de fato
invariante nas 7 telas (o invólucro `begin/end`) e a regra de "abortar o frame se
a tela mudou". Nada de pipeline rígido `input → update → draw`, porque `PlayScreen`
e `CombatScreen` intercalam update e desenho e não obedecem esse formato.

### `BaseScreen` (usar exatamente este esqueleto)

```java
package com.flamerealm.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flamerealm.game.Assets;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.instances.GameInstances;

public abstract class BaseScreen implements Screen {
    protected final FlameRealmGame game;
    protected final Assets assets;
    protected final GameInstances instances;
    protected final SpriteBatch batch;

    protected BaseScreen(FlameRealmGame game) {
        this.game = game;
        this.assets = game.assets;
        this.instances = game.instances;
        this.batch = game.batch;
    }

    /** TEMPLATE METHOD: esqueleto invariante do frame. final = subclasses nao sobrescrevem. */
    @Override
    public final void render(float delta) {
        if (handleInput()) {   // se trocou de tela, aborta: nao desenha a tela velha por cima da nova
            return;
        }
        batch.begin();
        batch.setColor(Color.WHITE);
        draw(delta);
        batch.end();
    }

    /**
     * Passo variavel: trata cliques/toques.
     * @return true se game.setScreen foi chamado (frame deve ser abortado).
     * Hook com default vazio: telas sem input proprio nao precisam sobrescrever.
     */
    protected boolean handleInput() {
        return false;
    }

    /** Passo variavel: desenha o conteudo, entre batch.begin() e batch.end(). */
    protected abstract void draw(float delta);

    // Hooks de ciclo de vida (vazios; subclasses sobrescrevem so o que precisam)
    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
```

### Piloto de referência: `PauseScreen` migrada

```java
public class PauseScreen extends BaseScreen {

    public PauseScreen(FlameRealmGame game) {
        super(game);
    }

    @Override
    protected boolean handleInput() {
        if (!Gdx.input.justTouched()) {
            return false;
        }
        if (instances.returnToMainMenuButton.mouseInButton()) {
            game.setScreen(game.mainMenu);
            return true;
        }
        if (instances.returnToGameButton.mouseInButton()) {
            game.setScreen(game.play);
            return true;
        }
        return false;
    }

    @Override
    protected void draw(float delta) {
        instances.pauseText.draw(batch, GameConstants.SCREEN_WIDTH * 0.4f, GameConstants.SCREEN_HEIGHT * 0.45f);
        instances.returnToMainMenuButton.draw(batch);
        instances.returnToGameButton.draw(batch);
    }
}
```

## Escopo

Migrar as **7 telas** em `core/src/main/java/com/flamerealm/game/screens/`:
`MainMenuScreen`, `HistoryScreen`, `TutorialScreen`, `PauseScreen`, `DeathScreen`,
`PlayScreen`, `CombatScreen`.

Regra geral de migração:
- Mover o bloco `if (Gdx.input.justTouched()) { ... }` para `handleInput()`,
  retornando `true` sempre que um `game.setScreen(...)` for chamado (substitui os
  `return;` que existem hoje).
- Mover todo o conteúdo entre `batch.begin()` e `batch.end()` para `draw(float delta)`,
  removendo as chamadas `begin()/setColor(WHITE)/end()` (agora feitas pelo template).

## Restrições (ler com atenção)

1. **Não mexer no alinhamento nem nas coordenadas de texto.** Manter os valores
   percentuais atuais idênticos.
2. **`PlayScreen` e `CombatScreen` transicionam de tela DE DENTRO do desenho**
   (não só no input). Ex.: `PlayScreen` chama `game.setScreen(game.combat)` no meio
   do render; `CombatScreen` vai para `death`/`play` durante o desenho. Preservar
   esse comportamento — essas transições continuam dentro de `draw(delta)` e o frame
   completa normalmente até `batch.end()`. NÃO tentar movê-las para `handleInput()`.
3. **Preservar a ordem das chamadas `.update()` e de desenho das entidades** em
   `Play` e `Combat` — elas intercalam desenhar e atualizar; mudar a ordem altera o
   timing visual.
4. **`CombatScreen`: `elapsedSinceLastClick += delta` roda todo frame, ANTES do
   input.** Preservar essa ordem — colocar esse incremento no topo do `handleInput()`
   do Combat (que roda antes do `draw`), NÃO dentro do `draw`. Caso contrário o
   timer é resetado e reincrementado fora de ordem.
5. Preservar os "quirks" comentados no código (ex.: no `PlayScreen`, o
   `returnToMainMenuButton` é verificado mas nunca desenhado — manter).

## Verificação

- Rodar `./gradlew build` e garantir que compila.
- Mostrar o `git diff` de todos os arquivos alterados.
- Conferir manualmente que cada tela: (a) não tem mais `begin()/end()` próprios,
  (b) transições por clique passam por `handleInput()` retornando `true`,
  (c) `Play`/`Combat` mantêm suas transições internas e a ordem de update intacta.
