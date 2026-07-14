package com.flamerealm.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Port de BasicButton.py: retangulo colorido que fica branco quando o mouse
 * esta sobre o botao.
 */
public class RectButton extends GameButton {
    private final Texture whitePixel;
    private Color color;

    public RectButton(Texture whitePixel, Color color, Vector2 coords, Vector2 size) {
        super(coords, size);
        this.whitePixel = whitePixel;
        this.color = color;
    }

    @Override
    public void draw(SpriteBatch batch) {
        Color drawColor = mouseInButton() ? Color.WHITE : color;
        batch.setColor(drawColor);
        batch.draw(whitePixel, getCoords().x, getCoords().y, getSize().x, getSize().y);
        batch.setColor(Color.WHITE);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
