package com.flamerealm.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Carrega texturas (equivalente a pg.image.load) e gera fontes via gdx-freetype
 * a partir dos .ttf abertos que substituem as fontes do Windows do jogo original
 * (ver GameConstants.FONT_*). A variante "bold" usa um contorno fino da mesma cor
 * do preenchimento para simular o negrito que Text.setBold(True) fazia no Pygame
 * quando o mouse passava sobre um ButtonWithText.
 */
public class Assets implements Disposable {
    private final AssetManager assetManager = new AssetManager();
    private final Map<String, FreeTypeFontGenerator> generators = new HashMap<>();
    private final Map<String, BitmapFont> fontCache = new HashMap<>();
    private final Map<String, TextureRegion> regionCache = new HashMap<>();
    private Texture whitePixel;

    /**
     * Textura 1x1 branca usada para desenhar retangulos coloridos (botoes, barras
     * de HP/mana) via SpriteBatch, equivalente a pg.draw.rect no Pygame original.
     */
    public Texture whitePixel() {
        if (whitePixel == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            whitePixel = new Texture(pixmap);
            pixmap.dispose();
        }
        return whitePixel;
    }

    public Texture texture(String path) {
        if (!assetManager.isLoaded(path, Texture.class)) {
            assetManager.load(path, Texture.class);
            assetManager.finishLoadingAsset(path);
        }
        return assetManager.get(path, Texture.class);
    }

    /**
     * TextureRegion com a textura inteira, ja invertida verticalmente para
     * compensar a camera yDown=true (ver FlameRealmGame). Usar para qualquer
     * desenho "cru" de uma imagem completa (fundos, sprites de botao) - as
     * AnimatedEntity ja fazem esse flip internamente nos seus frames.
     */
    public TextureRegion flippedRegion(String path) {
        return regionCache.computeIfAbsent(path, p -> {
            TextureRegion region = new TextureRegion(texture(p));
            region.flip(false, true);
            return region;
        });
    }

    /** Enfileira um asset (nao bloqueia). */
    public void queue(String path, Class<?> type) {
        if (!assetManager.isLoaded(path)) {
            assetManager.load(path, type);
        }
    }

    /** Enfileira uma lista de descriptors. */
    public void queue(Array<AssetDescriptor<?>> descriptors) {
        for (AssetDescriptor<?> d : descriptors) {
            if (!assetManager.isLoaded(d.fileName)) {
                assetManager.load(d);
            }
        }
    }

    /** Descarrega uma lista de descriptors (nao bloqueia; contagem de referencias
     * do AssetManager cuida de assets compartilhados entre grupos). */
    public void unload(Array<AssetDescriptor<?>> descriptors) {
        for (AssetDescriptor<?> d : descriptors) {
            if (assetManager.isLoaded(d.fileName)) {
                assetManager.unload(d.fileName);
            }
        }
    }

    /** Chamado 1x por frame pela BootLoadingScreen. true = fila vazia. */
    public boolean updateLoading() {
        return assetManager.update();
    }

    public float getProgress() {
        return assetManager.getProgress();
    }

    /** Obtem um asset ja carregado (sem bloquear). */
    public <T> T get(String path, Class<T> type) {
        return assetManager.get(path, type);
    }

    public BitmapFont font(String fontPath, int size, boolean bold) {
        String key = fontPath + "|" + size + "|" + bold;
        BitmapFont cached = fontCache.get(key);
        if (cached != null) {
            return cached;
        }

        FreeTypeFontGenerator generator = generators.computeIfAbsent(fontPath,
                path -> new FreeTypeFontGenerator(Gdx.files.internal(path)));

        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        // A camera do jogo usa yDown=true (para reaproveitar as posicoes de
        // GameConstants como no Pygame original); sem isso o texto renderiza
        // de cabeca para baixo, ja que a fonte assume por padrao uma camera Y-up.
        parameter.flip = true;
        if (bold) {
            parameter.borderWidth = Math.max(1f, size * 0.06f);
            parameter.borderColor = Color.WHITE;
            parameter.borderStraight = true;
        }

        BitmapFont font = generator.generateFont(parameter);
        fontCache.put(key, font);
        return font;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        for (FreeTypeFontGenerator generator : generators.values()) {
            generator.dispose();
        }
        for (BitmapFont font : fontCache.values()) {
            font.dispose();
        }
        if (whitePixel != null) {
            whitePixel.dispose();
        }
    }
}
