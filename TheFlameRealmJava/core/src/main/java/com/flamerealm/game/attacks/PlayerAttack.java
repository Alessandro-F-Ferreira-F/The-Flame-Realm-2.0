package com.flamerealm.game.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.ui.TextButton;

/**
 * Port de PlayerAttack.py (AtkPersonagem).
 */
public class PlayerAttack extends Attack {
    private int mana;
    private final TextButton atkButton;

    public PlayerAttack(String name, int damage, Texture spriteSheet, int limiteLoops,
                         int qtdFrames, GridPoint2 pixels, Vector2 spritePosition, float offsetFrames,
                         int mana, TextButton atkButton) {
        super(name, damage, spriteSheet, limiteLoops, qtdFrames, pixels, spritePosition, offsetFrames);
        this.mana = mana;
        this.atkButton = atkButton;
    }

    public TextButton getAtkButton() {
        return atkButton;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }
}
