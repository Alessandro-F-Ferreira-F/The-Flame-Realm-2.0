package com.flamerealm.game.instances;

import com.flamerealm.game.ui.SpriteButton;

/**
 * Holder do dominio "movimento no mapa": so agrupa objetos ja construidos por
 * GameInstances, sem logica propria.
 */
public class MovementInstances {
    public final SpriteButton moveUpButton;
    public final SpriteButton moveDownButton;
    public final SpriteButton moveRightButton;
    public final SpriteButton moveLeftButton;

    public MovementInstances(SpriteButton moveUpButton, SpriteButton moveDownButton,
            SpriteButton moveRightButton, SpriteButton moveLeftButton) {
        this.moveUpButton = moveUpButton;
        this.moveDownButton = moveDownButton;
        this.moveRightButton = moveRightButton;
        this.moveLeftButton = moveLeftButton;
    }
}
