package com.flamerealm.game.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.animation.AnimatedEntity;

/**
 * Port de Attacks.py. Ao contrario de AnimatedEntity.update() (que fica em loop
 * continuo), aqui a animacao toca por limiteLoops voltas e entao marca isOver=true.
 */
public class Attack extends AnimatedEntity {
    private final String name;
    private int damage;
    private final int limiteLoops;
    private boolean isOver;
    private int loopsCompletos;

    public Attack(String name, int damage, Texture spriteSheet, int limiteLoops,
                  int qtdFrames, GridPoint2 pixels, Vector2 spritePosition, float offsetFrames) {
        super(spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames);
        this.name = name;
        this.damage = damage;
        this.limiteLoops = limiteLoops;
        this.isOver = true;
        this.loopsCompletos = 0;
    }

    @Override
    public void update(float delta) {
        if (loopsCompletos >= limiteLoops) {
            loopsCompletos = 0;
            isOver = true;
        } else {
            image = frame[Math.round(atual)];
            atual += getOffsetFrames() * GameConstants.REFERENCE_FPS * delta;
            if (atual > getQtdFrames() - 1) {
                atual = 0;
                loopsCompletos++;
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getLimiteLoops() {
        return limiteLoops;
    }

    public boolean getIsOver() {
        return isOver;
    }

    public void setIsOver(boolean isOver) {
        this.isOver = isOver;
    }
}
