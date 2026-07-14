package com.flamerealm.game.screens;

import com.badlogic.gdx.Gdx;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.ui.TextAlign;

/**
 * Port do bloco "7 - Dying screen" de main.py.
 */
public class DeathScreen extends BaseScreen {

    public DeathScreen(FlameRealmGame game) {
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
        instances.deadText.draw(batch, TextAlign.CENTER, 0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        instances.returnToMainMenuButton.draw(batch);
        instances.returnToGameButton.draw(batch);
    }
}
