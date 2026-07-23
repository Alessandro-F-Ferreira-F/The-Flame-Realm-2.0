package com.flamerealm.game.screens;

import com.badlogic.gdx.Gdx;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.ui.TextAlign;

/**
 * Port do bloco "3 - History Screen" de main.py, incluindo o easter egg do
 * texto secreto (visivel apenas em hover, cor alterna preto/vermelho ao clicar).
 */
public class HistoryScreen extends BaseScreen {
    private boolean secretReveal;

    public HistoryScreen(FlameRealmGame game) {
        super(game);
    }

    @Override
    public void show() {
        secretReveal = false;
    }

    @Override
    protected boolean handleInput() {
        if (clickFirst(instances.nav.returnToMainMenuButton)) {
            return true;
        }
        if (!Gdx.input.justTouched()) {
            return false;
        }
        if (instances.story.secretButton.mouseInButton()) {
            secretReveal = !secretReveal;
        }
        return false;
    }

    @Override
    protected void draw(float delta) {
        instances.story.historyText.drawWrapped(batch, TextAlign.CENTER,
                GameConstants.SCREEN_WIDTH * 0.05f, GameConstants.SCREEN_HEIGHT * 0.1f, GameConstants.SCREEN_WIDTH * 0.9f);

        instances.story.secretButton.getText().setColor(secretReveal ? GameConstants.red : GameConstants.black);
        if (instances.story.secretButton.mouseInButton()) {
            instances.story.historySecretText.drawWrapped(batch, TextAlign.CENTER,
                    GameConstants.SCREEN_WIDTH * 0.05f, GameConstants.secretTextPosition.y, GameConstants.SCREEN_WIDTH * 0.9f);
        }

        instances.nav.returnToMainMenuButton.draw(batch);
    }
}
