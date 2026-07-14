package com.flamerealm.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

/**
 * Port de Text.py. setMessage/setColor sao efetivos no proximo draw() sem
 * precisar "reconstruir" nada, ja que BitmapFont.draw desenha o texto direto
 * do atlas a cada chamada (diferente do Pygame, que pre-renderiza a Surface).
 */
public class GameText {
    private BitmapFont font;
    private Color color;
    private String message;
    private final GlyphLayout layout = new GlyphLayout();

    public GameText(BitmapFont font, Color color, String message) {
        this.font = font;
        this.color = color;
        this.message = message;
    }

    public void draw(Batch batch, float x, float y) {
        font.setColor(color);
        font.draw(batch, message, x, y);
    }

    /** Centraliza (ou ancora) o texto dentro do retangulo (rectX, rectY, rectW, rectH). */
    public void draw(Batch batch, TextAlign align, float rectX, float rectY, float rectW, float rectH) {
        font.setColor(color);
        layout.setText(font, message);
        font.draw(batch, layout, align.x(layout, rectX, rectW), align.y(layout, rectY, rectH));
    }

    /** Desenha com quebra automatica dentro de targetWidth; cada linha alinhada por 'align'.
        (blockX, blockY) = topo-esquerdo do bloco. */
    public void drawWrapped(Batch batch, TextAlign align, float blockX, float blockY, float targetWidth) {
        font.setColor(color);
        int halign = (align == TextAlign.TOP_LEFT) ? Align.left : Align.center;
        layout.setText(font, message, color, targetWidth, halign, true);
        font.draw(batch, layout, blockX, blockY);
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
