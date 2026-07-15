package com.flamerealm.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Maquina de estados do fade dip-to-black (FADE_IN -> LOADING -> FADE_OUT),
 * compartilhada entre BootLoadingScreen e LoadingScreen para nao duplicar a
 * aritmetica do fade e o tempo minimo de exibicao. Pacote-privada: uso
 * restrito as telas de loading.
 */
final class FadeGate {

    enum Phase { FADE_IN, LOADING, FADE_OUT }

    private static final float FADE_TIME = 0.25f; // seg

    private Phase phase = Phase.FADE_IN;
    private float fadeAlpha = 0f;
    private float shownTime = 0f;
    private float minShow = 1f;  // tempo minimo em LOADING

    /** Reinicia a maquina (chamar ao configurar/entrar na tela). */
    void reset() {
        phase = Phase.FADE_IN;
        fadeAlpha = 0f;
        shownTime = 0f;
    }

    /** Define o tempo minimo em LOADING (quem for dono do valor define antes de cada reset). */
    void setMinShow(float seconds) {
        this.minShow = seconds;
    }

    Phase phase() {
        return phase;
    }

    /** Avanca o FADE_IN. Retorna true so no frame em que termina (dispare o job de carga). */
    boolean tickFadeIn(float delta) {
        fadeAlpha = Math.min(1f, fadeAlpha + delta / FADE_TIME);
        if (fadeAlpha >= 1f) {
            phase = Phase.LOADING;
            return true;
        }
        return false;
    }

    /** Avanca o LOADING. Retorna true quando a carga terminou E o tempo minimo passou. */
    boolean tickLoading(float delta, boolean loadingDone) {
        shownTime += delta;
        if (loadingDone && shownTime >= minShow) {
            phase = Phase.FADE_OUT;
            return true;
        }
        return false;
    }

    /** Avanca o FADE_OUT. Retorna true no frame em que o preto sumiu (hora de trocar de tela). */
    boolean tickFadeOut(float delta) {
        fadeAlpha = Math.max(0f, fadeAlpha - delta / FADE_TIME);
        return fadeAlpha <= 0f;
    }

    /** Desenha o overlay preto (dip-to-black) por cima do que ja foi pintado no batch. */
    void drawOverlay(Batch batch, Texture whitePixel, float screenWidth, float screenHeight) {
        batch.setColor(0f, 0f, 0f, fadeAlpha);
        batch.draw(whitePixel, 0, 0, screenWidth, screenHeight);
        batch.setColor(1f, 1f, 1f, 1f);
    }
}
