package com.flamerealm.game.screens;

import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.ui.TextAlign;

/**
 * Port do bloco "6 - Pause" de main.py.
 */
public class PauseScreen extends BaseScreen {

    public PauseScreen(FlameRealmGame game) {
        super(game);
    }

    @Override
    protected boolean handleInput() {
        return clickFirst(instances.nav.returnToMainMenuButton, instances.nav.returnToGameButton);
    }

    @Override
    protected void draw(float delta) {
        instances.overlays.pauseText.draw(batch, TextAlign.CENTER, 0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        instances.nav.returnToMainMenuButton.draw(batch);
        instances.nav.returnToGameButton.draw(batch);
    }
}
