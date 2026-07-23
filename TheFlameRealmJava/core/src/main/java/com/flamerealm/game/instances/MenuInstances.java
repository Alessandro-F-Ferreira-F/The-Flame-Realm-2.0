package com.flamerealm.game.instances;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.flamerealm.game.ui.GameText;
import com.flamerealm.game.ui.TextButton;

/**
 * Holder do dominio "menu principal": so agrupa objetos ja construidos por
 * GameInstances, sem logica propria.
 */
public class MenuInstances {
    public final TextButton playButton;
    public final TextButton historyButton;
    public final TextButton tutorialButton;
    public final TextButton quitButton;

    public final GameText playButtonText;
    public final GameText historyButtonText;
    public final GameText tutorialButtonText;
    public final GameText quitButtonText;

    public final TextureRegion mainMenuScreen;

    public MenuInstances(TextButton playButton, TextButton historyButton, TextButton tutorialButton, TextButton quitButton,
            GameText playButtonText, GameText historyButtonText, GameText tutorialButtonText, GameText quitButtonText,
            TextureRegion mainMenuScreen) {
        this.playButton = playButton;
        this.historyButton = historyButton;
        this.tutorialButton = tutorialButton;
        this.quitButton = quitButton;
        this.playButtonText = playButtonText;
        this.historyButtonText = historyButtonText;
        this.tutorialButtonText = tutorialButtonText;
        this.quitButtonText = quitButtonText;
        this.mainMenuScreen = mainMenuScreen;
    }
}
