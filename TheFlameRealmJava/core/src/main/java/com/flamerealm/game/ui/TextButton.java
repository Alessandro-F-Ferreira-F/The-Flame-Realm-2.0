package com.flamerealm.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Port de ButtonWithText.py: RectButton com um GameText associado, que troca
 * para a fonte em negrito enquanto o mouse esta sobre o botao.
 */
public class TextButton extends RectButton {
    private final GameText text;
    private final BitmapFont regularFont;
    private final BitmapFont boldFont;
    private Vector2 coordsText;
    private TextAlign align = TextAlign.TOP_LEFT;

    public TextButton(Texture whitePixel, Color color, Vector2 coords, Vector2 size,
                       GameText text, BitmapFont regularFont, BitmapFont boldFont, Vector2 coordsText) {
        super(whitePixel, color, coords, size);
        this.text = text;
        this.regularFont = regularFont;
        this.boldFont = boldFont;
        this.coordsText = coordsText;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        text.setFont(mouseInButton() ? boldFont : regularFont);
        if (align == TextAlign.TOP_LEFT) {
            text.draw(batch, coordsText.x, coordsText.y);
        } else {
            text.draw(batch, align, getCoords().x, getCoords().y, getSize().x, getSize().y);
        }
    }

    public GameText getText() {
        return text;
    }

    public Vector2 getCoordsText() {
        return coordsText;
    }

    public void setCoordsText(Vector2 coordsText) {
        this.coordsText = coordsText;
    }

    public TextAlign getAlign() {
        return align;
    }

    public void setAlign(TextAlign align) {
        this.align = align;
    }
}
