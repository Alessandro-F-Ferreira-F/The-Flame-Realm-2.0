package com.flamerealm.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.Assets;

/**
 * Metodos de conveniencia para montar os elementos de UI (RectButton, TextButton,
 * SpriteButton, GameText) a partir de Assets + GameConstants, reduzindo boilerplate
 * em GameInstances (equivalente as instancias de ButtonsInstances.py/TextInstances.py).
 */
public final class UiFactory {
    private UiFactory() {}

    public static GameText text(Assets assets, String fontPath, int fontSize, Color color, String message) {
        return new GameText(assets.font(fontPath, fontSize, false), color, message);
    }

    public static RectButton rectButton(Assets assets, Color color, Vector2 coords, GridPoint2 size) {
        return new RectButton(assets.whitePixel(), color, coords, new Vector2(size.x, size.y));
    }

    /**
     * O GameText passado deve ser o mesmo objeto ja criado para a secao de texto
     * correspondente (equivalente a TextInstances.py), para preservar a mesma
     * referencia que main.py usa via getAtkButton().getText().setMessage(...).
     */
    public static TextButton textButton(Assets assets, Color color, Vector2 coords, GridPoint2 size,
                                         GameText text, String fontPath, int fontSize, Vector2 coordsText) {
        return new TextButton(assets.whitePixel(), color, coords, new Vector2(size.x, size.y), text,
                assets.font(fontPath, fontSize, false), assets.font(fontPath, fontSize, true), coordsText);
    }

    public static SpriteButton spriteButton(Assets assets, String normalPath, String alternativePath,
                                             Vector2 coords, GridPoint2 size) {
        return new SpriteButton(assets.flippedRegion(normalPath), assets.flippedRegion(alternativePath),
                coords, new Vector2(size.x, size.y));
    }
}
