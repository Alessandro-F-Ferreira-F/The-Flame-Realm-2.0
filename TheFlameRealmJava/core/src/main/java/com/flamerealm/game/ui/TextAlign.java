package com.flamerealm.game.ui;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public enum TextAlign {
    /** Comportamento atual: desenha a partir do canto (x, y). */
    TOP_LEFT {
        public float x(GlyphLayout l, float rx, float rw) { return rx; }
        public float y(GlyphLayout l, float ry, float rh) { return ry; }
    },
    /** Centraliza so na horizontal; y = topo do retangulo. */
    CENTER_X {
        public float x(GlyphLayout l, float rx, float rw) { return rx + (rw - l.width) / 2f; }
        public float y(GlyphLayout l, float ry, float rh) { return ry; }
    },
    /** Centraliza nos dois eixos dentro do retangulo. */
    CENTER {
        public float x(GlyphLayout l, float rx, float rw) { return rx + (rw - l.width) / 2f; }
        public float y(GlyphLayout l, float ry, float rh) { return ry + (rh - l.height) / 2f; }
    };

    public abstract float x(GlyphLayout l, float rx, float rw);
    public abstract float y(GlyphLayout l, float ry, float rh);
}
