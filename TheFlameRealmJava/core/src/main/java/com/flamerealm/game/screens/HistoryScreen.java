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
        if (!Gdx.input.justTouched()) {
            return false;
        }
        if (instances.returnToMainMenuButton.mouseInButton()) {
            game.setScreen(game.mainMenu);
            return true;
        }
        if (instances.secretButton.mouseInButton()) {
            secretReveal = !secretReveal;
        }
        return false;
    }

    @Override
    protected void draw(float delta) {
        instances.historyText.drawWrapped(batch, TextAlign.CENTER,
                GameConstants.SCREEN_WIDTH * 0.05f, GameConstants.SCREEN_HEIGHT * 0.1f, GameConstants.SCREEN_WIDTH * 0.9f);

        instances.secretButton.getText().setColor(secretReveal ? GameConstants.red : GameConstants.black);
        if (instances.secretButton.mouseInButton()) {
            instances.historySecretText.drawWrapped(batch, TextAlign.CENTER,
                    GameConstants.SCREEN_WIDTH * 0.05f, GameConstants.secretTextPosition.y, GameConstants.SCREEN_WIDTH * 0.9f);
        }

        instances.returnToMainMenuButton.draw(batch);
    }
}
