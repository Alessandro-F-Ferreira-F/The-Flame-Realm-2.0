package com.flamerealm.game.animation;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Clipe de animacao puro (sem posicao), reutilizavel em corpo de combatente,
 * efeitos de ataque e ataques do player. sheetOffset e o canto (em pixels)
 * onde a fileira desse clipe comeca dentro da sheet - default (0,0) para
 * sheets de fileira unica.
 */
public record AnimationSpec(String sheetPath, int frames, GridPoint2 frameSize, float speed, GridPoint2 sheetOffset) {
    public AnimationSpec(String sheetPath, int frames, GridPoint2 frameSize, float speed) {
        this(sheetPath, frames, frameSize, speed, new GridPoint2(0, 0));
    }
}
