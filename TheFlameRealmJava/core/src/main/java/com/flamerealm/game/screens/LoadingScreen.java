package com.flamerealm.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;

import java.util.function.Supplier;

/**
 * Tela de loading generica e reutilizavel (singleton reaproveitado). Antes de
 * virar a tela ativa, configurar via begin(loadJob, postLoadJob, next):
 * loadJob enfileira os assets do proximo grupo (Command), postLoadJob constroi
 * objetos pos-carga (ex.: um CombatEncounter) e next informa a tela de destino
 * (continuation), ja que esta tela nao tem destino fixo. Uso esperado (ver
 * PlayScreen, que roteia Play -> Combat com carga lazy do encontro):
 *
 *   game.loading.begin(
 *           () -> assets.queue(manifest.descriptors()),
 *           () -> game.combat.setEncounter(manifest.build(assets)),
 *           () -> game.combat);
 *   game.setScreen(game.loading);
 */
public class LoadingScreen extends BaseScreen {

    private static final float BAR_WIDTH = GameConstants.SCREEN_WIDTH * 0.6f;
    private static final float BAR_HEIGHT = 20f;
    private static final float BAR_X = (GameConstants.SCREEN_WIDTH - BAR_WIDTH) / 2f;
    private static final float BAR_Y = GameConstants.SCREEN_HEIGHT / 2f - BAR_HEIGHT / 2f;

    private static final float DEFAULT_MIN_SHOW = 1f;     // carga normal
    private static final float TRANSITION_MIN_SHOW = 0.3f; // fade sem carga

    private final FadeGate fade = new FadeGate();
    private Runnable loadJob;
    private Runnable postLoadJob;
    private Supplier<Screen> next;
    private boolean showBar = true;

    public LoadingScreen(FlameRealmGame game) {
        super(game);
    }

    /** Configura o job de carga + destino e reseta o fade. Chamar antes de game.setScreen(game.loading). */
    public void begin(Runnable loadJob, Supplier<Screen> next) {
        begin(loadJob, () -> { }, next);
    }

    /**
     * Configura o job de carga (Command), um hook pos-carga (ex.: construir um
     * CombatEncounter apos os assets do encontro terminarem de carregar) e o
     * destino (continuation), e reseta o fade. Chamar antes de game.setScreen(game.loading).
     */
    public void begin(Runnable loadJob, Runnable postLoadJob, Supplier<Screen> next) {
        this.loadJob = loadJob;
        this.postLoadJob = postLoadJob;
        this.next = next;
        this.showBar = true;
        fade.setMinShow(DEFAULT_MIN_SHOW);
        fade.reset();
    }

    /** Transição sem carga real (fade + jobs), barra oculta e hold curto. */
    public void beginTransition(Runnable loadJob, Runnable postLoadJob, Supplier<Screen> next) {
        begin(loadJob, postLoadJob, next);
        this.showBar = false;
        fade.setMinShow(TRANSITION_MIN_SHOW);
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
                    postLoadJob.run(); // constroi objetos pos-carga (ex.: CombatEncounter)
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

    @Override
    protected void draw(float delta) {
        if (showBar) {
            batch.setColor(GameConstants.black);
            batch.draw(assets.whitePixel(), BAR_X, BAR_Y, BAR_WIDTH, BAR_HEIGHT);

            batch.setColor(GameConstants.green);
            batch.draw(assets.whitePixel(), BAR_X, BAR_Y, BAR_WIDTH * assets.getProgress(), BAR_HEIGHT);

            batch.setColor(Color.WHITE);
        }

        fade.drawOverlay(batch, assets.whitePixel(), GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    }
}
