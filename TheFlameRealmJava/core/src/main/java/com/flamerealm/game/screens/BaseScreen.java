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
        if (update(delta)) {   // se trocou de tela, aborta: nao desenha a tela velha por cima da nova
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

    /**
     * Passo variavel: faz o mundo avancar (logica), sem tocar no SpriteBatch.
     * @return true se game.setScreen foi chamado (frame deve ser abortado).
     * Hook com default vazio: telas sem logica propria nao precisam sobrescrever.
     */
    protected boolean update(float delta) {
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
