package com.flamerealm.game.characters;

import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.animation.AnimState;
import com.flamerealm.game.animation.AnimatedEntity;
import com.flamerealm.game.attacks.PlayerAttack;

import java.util.List;
import java.util.Map;

/**
 * Port de PlayerCombatForm (dentro de CombatForm.py).
 */
public class PlayerCombatForm extends CombatForm<PlayerAttack> {
    private int manaPoints;

    public PlayerCombatForm(Vector2 position, Map<AnimState, AnimatedEntity> clips, int healthPoints,
                             List<PlayerAttack> atkList, int manaPoints) {
        super(position, clips, healthPoints, atkList);
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
