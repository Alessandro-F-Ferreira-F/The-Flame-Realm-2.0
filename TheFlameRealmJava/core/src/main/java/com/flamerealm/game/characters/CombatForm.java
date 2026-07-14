package com.flamerealm.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.animation.AnimatedEntity;
import com.flamerealm.game.attacks.Attack;

import java.util.List;

/**
 * Port de CombatForm.py. Generico em T para manter, com seguranca de tipos, o
 * mesmo "duck typing" do Python: o boss usa uma CombatForm&lt;BossAttack&gt; e o
 * jogador usa PlayerCombatForm (CombatForm&lt;PlayerAttack&gt;).
 */
public class CombatForm<T extends Attack> extends AnimatedEntity {
    private int healthPoints;
    private List<T> atkList;

    public CombatForm(Texture spriteSheet, int qtdFrames, GridPoint2 pixels, Vector2 spritePosition,
                       float offsetFrames, int healthPoints, List<T> atkList) {
        super(spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames);
        this.healthPoints = healthPoints;
        this.atkList = atkList;
    }

    public T randomizeAtk() {
        int randomizer = MathUtils.random(0, atkList.size() - 1);
        return atkList.get(randomizer);
    }

    public void revive(int hp) {
        setHealthPoints(hp);
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = Math.max(healthPoints, 0);
    }

    public List<T> getAtkList() {
        return atkList;
    }

    public void setAtkList(List<T> atkList) {
        this.atkList = atkList;
    }
}
