package com.flamerealm.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.ui.TextAlign;

/**
 * Port do bloco "4 - Tutorial screen" de main.py.
 */
public class TutorialScreen extends BaseScreen {

    public TutorialScreen(FlameRealmGame game) {
        super(game);
    }

    @Override
    protected boolean handleInput() {
        return clickFirst(instances.nav.returnToMainMenuButton);
    }

    @Override
    protected void draw(float delta) {
        float mainX = GameConstants.SCREEN_WIDTH * 0.05f;
        instances.story.tutorialMainText.drawWrapped(batch, TextAlign.CENTER,
                mainX, GameConstants.SCREEN_HEIGHT * 0.1f, GameConstants.SCREEN_WIDTH * 0.9f);

        float manaX = GameConstants.SCREEN_WIDTH * 0.2f;
        instances.story.tutorialManaText.drawWrapped(batch, TextAlign.CENTER,
                manaX, GameConstants.SCREEN_HEIGHT * 0.35f, GameConstants.SCREEN_WIDTH - manaX - GameConstants.SCREEN_WIDTH * 0.05f);

        float moveX = GameConstants.SCREEN_WIDTH * 0.3f;
        instances.story.tutorialMoveText.drawWrapped(batch, TextAlign.CENTER,
                moveX, GameConstants.SCREEN_HEIGHT * 0.65f, GameConstants.SCREEN_WIDTH - moveX - GameConstants.SCREEN_WIDTH * 0.05f);

        batch.setColor(GameConstants.blue);
        batch.draw(assets.whitePixel(), GameConstants.SCREEN_WIDTH * 0.05f, GameConstants.SCREEN_HEIGHT * 0.35f, 80, 15);
        batch.setColor(Color.WHITE);

        instances.combat.playerCombatFormManaText.draw(batch, GameConstants.SCREEN_WIDTH * 0.05f, GameConstants.SCREEN_HEIGHT * 0.38f);

        instances.movement.moveUpButton.draw(batch);
        instances.movement.moveDownButton.draw(batch);
        instances.movement.moveRightButton.draw(batch);
        instances.movement.moveLeftButton.draw(batch);
        instances.nav.returnToMainMenuButton.draw(batch);
    }
}
