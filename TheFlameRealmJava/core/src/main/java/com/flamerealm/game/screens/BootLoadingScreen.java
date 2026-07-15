package com.flamerealm.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;

/**
 * Tela minima exibida enquanto o grupo nucleo de assets carrega de forma
 * assincrona. Sem GameInstances (ainda nao existe) e sem fade (Fase 1).
 */
public class BootLoadingScreen extends BaseScreen {

    private static final float BAR_WIDTH = GameConstants.SCREEN_WIDTH * 0.6f;
    private static final float BAR_HEIGHT = 20f;
    private static final float BAR_X = (GameConstants.SCREEN_WIDTH - BAR_WIDTH) / 2f;
    private static final float BAR_Y = GameConstants.SCREEN_HEIGHT / 2f - BAR_HEIGHT / 2f;

    public BootLoadingScreen(FlameRealmGame game) {
        super(game);
    }

    @Override
    protected boolean update(float delta) {
        if (assets.updateLoading()) {
            game.onCoreLoaded();
            return true;
        }
        return false;
    }

    @Override
    protected void draw(float delta) {
        batch.setColor(GameConstants.black);
        batch.draw(assets.whitePixel(), BAR_X, BAR_Y, BAR_WIDTH, BAR_HEIGHT);

        batch.setColor(GameConstants.green);
        batch.draw(assets.whitePixel(), BAR_X, BAR_Y, BAR_WIDTH * assets.getProgress(), BAR_HEIGHT);

        batch.setColor(Color.WHITE);
    }
}
