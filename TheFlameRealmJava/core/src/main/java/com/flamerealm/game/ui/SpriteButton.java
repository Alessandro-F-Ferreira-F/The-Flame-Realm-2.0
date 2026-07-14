package com.flamerealm.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Port de ButtonWithSprite.py: alterna entre duas texturas conforme o hover.
 * Usa TextureRegion (ja invertida verticalmente por Assets.flippedRegion, ver
 * UiFactory) para compensar a camera yDown=true do jogo.
 */
public class SpriteButton extends GameButton {
    private final TextureRegion normalImage;
    private final TextureRegion alternativeImage;

    public SpriteButton(TextureRegion normalImage, TextureRegion alternativeImage, Vector2 coords, Vector2 size) {
        super(coords, size);
        this.normalImage = normalImage;
        this.alternativeImage = alternativeImage;
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion current = mouseInButton() ? alternativeImage : normalImage;
        batch.draw(current, getCoords().x, getCoords().y, getSize().x, getSize().y);
    }

    public TextureRegion getNormalImage() {
        return normalImage;
    }

    public TextureRegion getAlternativeImage() {
        return alternativeImage;
    }
}
