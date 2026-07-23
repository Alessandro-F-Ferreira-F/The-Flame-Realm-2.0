package com.flamerealm.game.screens;

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
        return clickFirst(instances.menu.quitButton, instances.menu.playButton,
                instances.menu.historyButton, instances.menu.tutorialButton);
    }

    @Override
    protected void draw(float delta) {
        batch.draw(instances.menu.mainMenuScreen, 0, 0);
        instances.menu.playButton.draw(batch);
        instances.menu.historyButton.draw(batch);
        instances.menu.tutorialButton.draw(batch);
        instances.menu.quitButton.draw(batch);
    }
}
