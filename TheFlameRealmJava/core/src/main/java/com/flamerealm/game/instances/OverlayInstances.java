package com.flamerealm.game.instances;

import com.flamerealm.game.ui.GameText;

/**
 * Holder do dominio "overlays de tela cheia" (pausa/morte): so agrupa objetos
 * ja construidos por GameInstances, sem logica propria.
 */
public class OverlayInstances {
    public final GameText pauseText;
    public final GameText deadText;

    public OverlayInstances(GameText pauseText, GameText deadText) {
        this.pauseText = pauseText;
        this.deadText = deadText;
    }
}
