package com.flamerealm.game.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.GameConstants;

/**
 * Port de AnimatedSprite.py.
 * @param spriteSheet spritesheet carregada via Assets.texture()
 * @param qtdFrames quantidade de frames
 * @param pixels (largura, altura) em pixels de cada frame
 * @param spritePosition posicao (topo-esquerda) do sprite na tela
 * @param offsetFrames dita a velocidade da animacao
 */
public class AnimatedEntity {
    protected TextureRegion[] frame;
    protected int qtdFrames;
    protected Texture spriteSheet;
    protected GridPoint2 pixels;
    protected float offsetFrames;

    protected float atual;
    protected TextureRegion image;
    protected Vector2 position;

    public AnimatedEntity(Texture spriteSheet, int qtdFrames, GridPoint2 pixels, Vector2 spritePosition, float offsetFrames) {
        this(spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames, new GridPoint2(0, 0));
    }

    /** sheetOffset: canto (em pixels) onde a fileira de frames comeca dentro da sheet - suporta sheets multi-linha. */
    public AnimatedEntity(Texture spriteSheet, int qtdFrames, GridPoint2 pixels, Vector2 spritePosition, float offsetFrames, GridPoint2 sheetOffset) {
        reinitialize(spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames, sheetOffset);
    }

    protected final void reinitialize(Texture spriteSheet, int qtdFrames, GridPoint2 pixels, Vector2 spritePosition, float offsetFrames) {
        reinitialize(spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames, new GridPoint2(0, 0));
    }

    protected final void reinitialize(Texture spriteSheet, int qtdFrames, GridPoint2 pixels, Vector2 spritePosition, float offsetFrames, GridPoint2 sheetOffset) {
        this.spriteSheet = spriteSheet;
        this.qtdFrames = qtdFrames;
        this.pixels = pixels;
        this.offsetFrames = offsetFrames;

        this.frame = new TextureRegion[qtdFrames];
        for (int i = 0; i < qtdFrames; i++) {
            frame[i] = new TextureRegion(spriteSheet, sheetOffset.x + pixels.x * i, sheetOffset.y, pixels.x, pixels.y);
            // Compensa a camera yDown=true (ver FlameRealmGame): sem isso os
            // sprites renderizam de cabeca para baixo.
            frame[i].flip(false, true);
        }

        this.atual = 0;
        this.image = frame[0];
        this.position = spritePosition;
    }

    public void update(float delta) {
        if (atual > getQtdFrames() - 1) {
            atual = 0;
        }
        image = frame[Math.round(atual)];
        atual += getOffsetFrames() * GameConstants.REFERENCE_FPS * delta;
    }

    public TextureRegion[] getFrame() {
        return frame;
    }

    public Texture getSprite() {
        return spriteSheet;
    }

    public int getQtdFrames() {
        return qtdFrames;
    }

    public GridPoint2 getPixels() {
        return pixels;
    }

    public float getOffsetFrames() {
        return offsetFrames;
    }

    public void setOffsetFrames(float s) {
        offsetFrames = s;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 pos) {
        position = pos;
    }

    public void centerOn(float cx, float cy) {
        position.set(
                cx - image.getRegionWidth() / 2f,
                cy - image.getRegionHeight() / 2f);
    }

    public TextureRegion getImage() {
        return image;
    }

    public void setImage(TextureRegion img) {
        image = img;
    }
}
