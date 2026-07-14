package com.flamerealm.game.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.GameConstants;

/**
 * Port de BossAttack.py (AtkChefe).
 */
public class BossAttack extends Attack {
    private int hitKillChance;

    public BossAttack(String name, int damage, Texture spriteSheet, int limiteLoops,
                       int qtdFrames, GridPoint2 pixels, Vector2 spritePosition, float offsetFrames,
                       int hitKillChance) {
        super(name, damage, spriteSheet, limiteLoops, qtdFrames, pixels, spritePosition, offsetFrames);
        this.hitKillChance = hitKillChance;
    }

    public void randomizeHitKill() {
        int randomizer = MathUtils.random(1, 100);
        if (randomizer <= hitKillChance) {
            setDamage(GameConstants.playerHp);
        }
    }

    public int getHitKillChance() {
        return hitKillChance;
    }

    public void setHitKillChance(int hitKillChance) {
        this.hitKillChance = hitKillChance;
    }
}
