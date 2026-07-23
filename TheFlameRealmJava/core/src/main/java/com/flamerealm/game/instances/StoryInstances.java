package com.flamerealm.game.instances;

import com.flamerealm.game.ui.GameText;
import com.flamerealm.game.ui.TextButton;

/**
 * Holder do dominio "telas de historia" (History/Tutorial): so agrupa objetos
 * ja construidos por GameInstances, sem logica propria.
 */
public class StoryInstances {
    public final GameText historyText;
    public final GameText historySecretText;
    public final TextButton secretButton;

    public final GameText tutorialMainText;
    public final GameText tutorialManaText;
    public final GameText tutorialMoveText;

    public StoryInstances(GameText historyText, GameText historySecretText, TextButton secretButton,
            GameText tutorialMainText, GameText tutorialManaText, GameText tutorialMoveText) {
        this.historyText = historyText;
        this.historySecretText = historySecretText;
        this.secretButton = secretButton;
        this.tutorialMainText = tutorialMainText;
        this.tutorialManaText = tutorialManaText;
        this.tutorialMoveText = tutorialMoveText;
    }
}
