package com.flamerealm.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.animation.AnimatedEntity;

/**
 * Port de CharacterSprite.py (personagem no mapa aberto).
 */
public class CharacterEntity extends AnimatedEntity {
    private TextureRegion idle;
    private boolean moving;

    public CharacterEntity(Texture spriteSheet, int qtdFrames, GridPoint2 pixels, Vector2 spritePosition, float offsetFrames) {
        super(spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames);
        this.idle = getFrame()[1];
        this.moving = false;
    }

    public TextureRegion getIdle() {
        return idle;
    }

    public void setIdle(TextureRegion idle) {
        this.idle = idle;
    }

    public boolean getMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setSprite(Texture spriteSheet) {
        reinitialize(spriteSheet, getQtdFrames(), getPixels(), getPosition(), getOffsetFrames());
    }
}
