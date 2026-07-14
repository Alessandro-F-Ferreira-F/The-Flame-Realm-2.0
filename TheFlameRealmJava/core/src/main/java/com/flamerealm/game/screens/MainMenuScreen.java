package com.flamerealm.game.screens;

import com.badlogic.gdx.Gdx;
import com.flamerealm.game.FlameRealmGame;

/**
 * Port do bloco "1 - Main menu" de main.py.
 */
public class MainMenuScreen extends BaseScreen {

    public MainMenuScreen(FlameRealmGame game) {
        super(game);
    }

    @Override
    protected boolean handleInput() {
        if (!Gdx.input.justTouched()) {
            return false;
        }
        if (instances.quitButton.mouseInButton()) {
            Gdx.app.exit();
            return true;
        } else if (instances.playButton.mouseInButton()) {
            game.setScreen(game.play);
            return true;
        } else if (instances.historyButton.mouseInButton()) {
            game.setScreen(game.history);
            return true;
        } else if (instances.tutorialButton.mouseInButton()) {
            game.setScreen(game.tutorial);
            return true;
        }
        return false;
    }

    @Override
    protected void draw(float delta) {
        batch.draw(instances.mainMenuScreen, 0, 0);
        instances.playButton.draw(batch);
        instances.historyButton.draw(batch);
        instances.tutorialButton.draw(batch);
        instances.quitButton.draw(batch);
    }
}
