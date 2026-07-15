package com.flamerealm.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;

/**
 * Tela minima exibida enquanto o grupo nucleo de assets carrega de forma
 * assincrona. Sem GameInstances (ainda nao existe); usa o mesmo fade
 * dip-to-black da LoadingScreen generica (FadeGate), mas com job/destino
 * fixos (nao precisa do Command+continuation - ver LoadingScreen).
 */
public class BootLoadingScreen extends BaseScreen {

    private static final float BAR_WIDTH = GameConstants.SCREEN_WIDTH * 0.6f;
    private static final float BAR_HEIGHT = 20f;
    private static final float BAR_X = (GameConstants.SCREEN_WIDTH - BAR_WIDTH) / 2f;
    private static final float BAR_Y = GameConstants.SCREEN_HEIGHT / 2f - BAR_HEIGHT / 2f;

    private final FadeGate fade = new FadeGate();

    public BootLoadingScreen(FlameRealmGame game) {
        super(game);
    }

    @Override
    protected boolean update(float delta) {
        switch (fade.phase()) {
            case FADE_IN:
                fade.tickFadeIn(delta); // job ja enfileirado em FlameRealmGame.create()
                break;
            case LOADING:
                boolean done = assets.updateLoading();
                fade.tickLoading(delta, done);
                break;
            case FADE_OUT:
                if (fade.tickFadeOut(delta)) {
                    game.onCoreLoaded(); // constroi instances/telas e chama setScreen(mainMenu)
                    return true;
                }
                break;
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

        fade.drawOverlay(batch, assets.whitePixel(), GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    }
}
