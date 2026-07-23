package com.flamerealm.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.function.BooleanSupplier;

/**
 * Port de Button.py. Gdx.input.getX()/getY() ja usa (0,0) no canto
 * superior-esquerdo com Y crescendo para baixo, igual ao pg.mouse.get_pos(),
 * entao o teste de bounds e identico ao original.
 */
public abstract class GameButton {
    private Vector2 coords;
    private Vector2 size;
    private BooleanSupplier action;

    protected GameButton(Vector2 coords, Vector2 size) {
        this.coords = coords;
        this.size = size;
    }

    public boolean mouseInButton() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        return coords.x <= mouseX && mouseX <= coords.x + size.x
                && coords.y <= mouseY && mouseY <= coords.y + size.y;
    }

    /** Command opcional de navegacao (ver BaseScreen.clickFirst). */
    public void setAction(BooleanSupplier action) {
        this.action = action;
    }

    /** @return true se a acao trocou de tela (contrato do Template Method). */
    public boolean fire() {
        return action != null && action.getAsBoolean();
    }

    public abstract void draw(SpriteBatch batch);

    public Vector2 getCoords() {
        return coords;
    }

    public void setCoords(Vector2 coords) {
        this.coords = coords;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }
}
