package com.flamerealm.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;

import java.util.function.Supplier;

/**
 * Tela de loading generica e reutilizavel (singleton reaproveitado). Antes de
 * virar a tela ativa, configurar via begin(loadJob, next): loadJob enfileira
 * os assets do proximo grupo (Command) e next informa a tela de destino
 * (continuation), ja que esta tela nao tem destino fixo. Uso esperado:
 *
 *   game.loading.begin(bossManifest, () -> game.combat);
 *   game.setScreen(game.loading);
 *
 * Fase 1: nenhum call site chama begin() ainda - fica pronta para a Fase 2
 * rotear Play -> Combat com carga lazy do encontro (via onLoaded()).
 */
public class LoadingScreen extends BaseScreen {

    private static final float BAR_WIDTH = GameConstants.SCREEN_WIDTH * 0.6f;
    private static final float BAR_HEIGHT = 20f;
    private static final float BAR_X = (GameConstants.SCREEN_WIDTH - BAR_WIDTH) / 2f;
    private static final float BAR_Y = GameConstants.SCREEN_HEIGHT / 2f - BAR_HEIGHT / 2f;

    private final FadeGate fade = new FadeGate();
    private Runnable loadJob;
    private Supplier<Screen> next;

    public LoadingScreen(FlameRealmGame game) {
        super(game);
    }

    /** Configura o job de carga + destino e reseta o fade. Chamar antes de game.setScreen(game.loading). */
    public void begin(Runnable loadJob, Supplier<Screen> next) {
        this.loadJob = loadJob;
        this.next = next;
        fade.reset();
    }

    @Override
    protected boolean update(float delta) {
        switch (fade.phase()) {
            case FADE_IN:
                if (fade.tickFadeIn(delta)) {
                    loadJob.run(); // enfileira o proximo grupo assim que o preto cobriu a tela
                }
                break;
            case LOADING:
                boolean done = assets.updateLoading();
                if (fade.tickLoading(delta, done)) {
                    onLoaded();
                }
                break;
            case FADE_OUT:
                if (fade.tickFadeOut(delta)) {
                    game.setScreen(next.get());
                    return true; // aborta o frame: nao desenha por cima da tela nova
                }
                break;
        }
        return false;
    }

    /** Hook: construir objetos pos-carga (ex.: CombatEncounter na Fase 2). Vazio nesta fase. */
    protected void onLoaded() {
    }

    @Override
    protected void draw(float delta) {
        batch.setColor(GameConstants.black);
        batch.draw(assets.whitePixel(), BAR_X, BAR_Y, BAR_WIDTH, BAR_HEIGHT);

        batch.setColor(GameConstants.green);
        batch.draw(assets.whitePixel(), BAR_X, BAR_Y, BAR_WIDTH * assets.getProgress(), BAR_HEIGHT);

        batch.setColor(Color.WHITE);

        fade.drawOverlay(batch, assets.whitePixel(), GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    }
}
