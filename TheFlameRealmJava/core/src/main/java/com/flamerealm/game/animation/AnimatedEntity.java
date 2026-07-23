package com.flamerealm.game.animation;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
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
    protected GridPoint2 sheetOffset;
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
        this.sheetOffset = sheetOffset;
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

    /**
     * Centro da regiao de pixels nao transparentes, como offset a partir do canto
     * superior-esquerdo do sprite. Une a bounding box de todos os frames (para o
     * ponto nao "pular" entre frames) e ja converte para o espaco de desenho,
     * compensando o flip vertical da camera yDown (ver reinitialize/flip).
     *
     * <p>Serve para ancorar efeitos (ex.: ataques) no centro visual do personagem,
     * em vez do centro geometrico do frame, que inclui o padding transparente.
     * Le um Pixmap fresco da sheet via TextureData (a textura ja subiu para a GPU,
     * entao consumePixmap recarrega do arquivo) - operacao pontual, nao por frame.
     * Fallback: centro do frame, se tudo for transparente.
     *
     * @param alphaThreshold alpha (0-255) minimo para um pixel contar como opaco.
     */
    public Vector2 opaqueCenterOffset(int alphaThreshold) {
        TextureData data = spriteSheet.getTextureData();
        if (!data.isPrepared()) {
            data.prepare();
        }
        Pixmap pm = data.consumePixmap();
        try {
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -1, maxY = -1;
            for (int f = 0; f < qtdFrames; f++) {
                int baseX = sheetOffset.x + pixels.x * f;
                for (int y = 0; y < pixels.y; y++) {
                    for (int x = 0; x < pixels.x; x++) {
                        int alpha = pm.getPixel(baseX + x, sheetOffset.y + y) & 0xFF;
                        if (alpha > alphaThreshold) {
                            if (x < minX) minX = x;
                            if (x > maxX) maxX = x;
                            if (y < minY) minY = y;
                            if (y > maxY) maxY = y;
                        }
                    }
                }
            }
            if (maxX < 0) {
                return new Vector2(pixels.x / 2f, pixels.y / 2f);
            }
            // Mapeamento direto: o flip(false, true) dos frames apenas cancela a
            // camera yDown, deixando o sprite em pe. Logo a linha 0 do pixmap cru
            // (topo do personagem) cai no topo da regiao na tela - sem inverter Y.
            return new Vector2((minX + maxX) / 2f, (minY + maxY) / 2f);
        } finally {
            if (data.disposePixmap()) {
                pm.dispose();
            }
        }
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
