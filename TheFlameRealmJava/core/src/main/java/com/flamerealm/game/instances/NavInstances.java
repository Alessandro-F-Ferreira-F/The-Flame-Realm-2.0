package com.flamerealm.game.instances;

import com.flamerealm.game.ui.GameText;
import com.flamerealm.game.ui.SpriteButton;
import com.flamerealm.game.ui.TextButton;

/**
 * Holder do dominio "navegacao compartilhada" (botoes de voltar/pausar usados
 * por varias telas): so agrupa objetos ja construidos por GameInstances.
 */
public class NavInstances {
    public final TextButton returnToMainMenuButton;
    public final TextButton returnToGameButton;
    public final SpriteButton pauseButton;

    public final GameText returnToMainMenuText;
    public final GameText returnToGameText;

    public NavInstances(TextButton returnToMainMenuButton, TextButton returnToGameButton, SpriteButton pauseButton,
            GameText returnToMainMenuText, GameText returnToGameText) {
        this.returnToMainMenuButton = returnToMainMenuButton;
        this.returnToGameButton = returnToGameButton;
        this.pauseButton = pauseButton;
        this.returnToMainMenuText = returnToMainMenuText;
        this.returnToGameText = returnToGameText;
    }
}
