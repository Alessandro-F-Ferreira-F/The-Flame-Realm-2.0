package com.flamerealm.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.assets.EncounterManifest;

import java.util.List;

/**
 * Port do bloco "Play screen" de main.py (movimento no mapa por FightPoints).
 * PlayScreen e um singleton reaproveitado (ver FlameRealmGame), entao pausar no
 * meio de um movimento e retomar preserva corretamente currentMapOffset e
 * verticalMoving - exatamente como as variaveis locais do loop unico em Python.
 */
public class PlayScreen extends BaseScreen {
    private float currentMapOffset = GameConstants.mapSpeed;
    private boolean verticalMoving;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    public PlayScreen(FlameRealmGame game) {
        super(game);
    }

    @Override
    protected boolean handleInput() {
        if (!Gdx.input.justTouched()) {
            return false;
        }

        // Fiel ao original: este botao nunca e desenhado na tela de jogo,
        // mas o clique ainda e verificado (quirk preservado do main.py).
        if (instances.returnToMainMenuButton.mouseInButton()) {
            game.setScreen(game.mainMenu);
            return true;
        }

        if (instances.pauseButton.mouseInButton()) {
            game.setScreen(game.pause);
            return true;
        }

        if ((instances.moveUpButton.mouseInButton() || instances.moveRightButton.mouseInButton()
                || instances.moveLeftButton.mouseInButton() || instances.moveDownButton.mouseInButton())
                && !instances.playerObject.getMoving()) {

            if ((instances.moveRightButton.mouseInButton() && moveRight)
                    || (instances.moveLeftButton.mouseInButton() && moveLeft)) {

                instances.playerObject.setMoving(true);
                verticalMoving = false;

                if (instances.moveRightButton.mouseInButton()) {
                    currentMapOffset = GameConstants.mapSpeed;
                    instances.playerObject.setSprite(instances.playerSpriteRight);
                } else {
                    currentMapOffset = -GameConstants.mapSpeed;
                    instances.playerObject.setSprite(instances.playerSpriteLeft);
                }
            } else if ((instances.moveUpButton.mouseInButton() && moveUp)
                    || (instances.moveDownButton.mouseInButton() && moveDown)) {

                instances.playerObject.setMoving(true);
                verticalMoving = true;

                if (instances.moveUpButton.mouseInButton()) {
                    currentMapOffset = -GameConstants.mapSpeed;
                    instances.playerObject.setSprite(instances.playerSpriteUp);
                } else {
                    currentMapOffset = GameConstants.mapSpeed;
                    instances.playerObject.setSprite(instances.playerSpriteDown);
                }
            }
        }
        return false;
    }

    @Override
    protected void draw(float delta) {
        Vector2 currentPos = instances.gameMap.getCurrentPoint().getPoint();
        TextureRegion mapRegion = new TextureRegion(instances.gameMap.getImage(),
                Math.round(currentPos.x), Math.round(currentPos.y),
                GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        mapRegion.flip(false, true);
        batch.draw(mapRegion, 0, 0);

        batch.draw(instances.playerObject.getImage(),
                instances.playerObject.getPosition().x, instances.playerObject.getPosition().y);

        instances.pauseButton.draw(batch);

        if (instances.playerObject.getMoving()) {
            instances.playerObject.update();
            if (verticalMoving) {
                instances.gameMap.getCurrentPoint().setPoint(new Vector2(
                        instances.gameMap.getCurrentPoint().getPoint().x,
                        instances.gameMap.getCurrentPoint().getPoint().y + currentMapOffset));
            } else {
                instances.gameMap.getCurrentPoint().setPoint(new Vector2(
                        instances.gameMap.getCurrentPoint().getPoint().x + currentMapOffset,
                        instances.gameMap.getCurrentPoint().getPoint().y));
            }
        } else {
            instances.playerObject.setImage(instances.playerObject.getFrame()[1]);
        }

        if (instances.gameMap.fightPointTest()) {
            moveUp = false;
            moveDown = false;
            moveLeft = false;
            moveRight = false;
            instances.playerObject.setMoving(false);

            if (instances.gameMap.getCurrentPoint().getActivated()) {
                EncounterManifest manifest = instances.gameMap.getCurrentPoint().getManifest();
                game.loading.begin(
                        () -> assets.queue(manifest.descriptors()),
                        () -> game.combat.setEncounter(manifest.build(assets)),
                        () -> game.combat);
                game.setScreen(game.loading);
            } else {
                instances.gameMap.setPreviousPoint(instances.gameMap.getCurrentPoint());
            }

            List<String> allowedDirections = instances.gameMap.getCurrentPoint().getAllowedDirections();

            if (allowedDirections.contains("UP")) {
                instances.moveUpButton.draw(batch);
                moveUp = true;
            }
            if (allowedDirections.contains("DOWN")) {
                instances.moveDownButton.draw(batch);
                moveDown = true;
            }
            if (allowedDirections.contains("RIGHT")) {
                instances.moveRightButton.draw(batch);
                moveRight = true;
            }
            if (allowedDirections.contains("LEFT")) {
                instances.moveLeftButton.draw(batch);
                moveLeft = true;
            }
        }
    }
}
