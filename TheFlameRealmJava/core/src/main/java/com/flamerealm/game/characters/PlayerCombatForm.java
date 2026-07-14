package com.flamerealm.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.attacks.PlayerAttack;

import java.util.List;

/**
 * Port de PlayerCombatForm (dentro de CombatForm.py).
 */
public class PlayerCombatForm extends CombatForm<PlayerAttack> {
    private int manaPoints;

    public PlayerCombatForm(Texture spriteSheet, int qtdFrames, GridPoint2 pixels, Vector2 spritePosition,
                             float offsetFrames, int healthPoints, List<PlayerAttack> atkList, int manaPoints) {
        super(spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames, healthPoints, atkList);
        this.manaPoints = manaPoints;
    }

    public PlayerAttack atkCheck() {
        for (PlayerAttack attack : getAtkList()) {
            if (attack.getAtkButton().mouseInButton()) {
                return attack;
            }
        }
        return null;
    }

    public int getManaPoints() {
        return manaPoints;
    }

    public void setManaPoints(int manaPoints) {
        this.manaPoints = manaPoints;
    }
}
