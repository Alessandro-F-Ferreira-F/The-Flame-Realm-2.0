package com.flamerealm.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.assets.EncounterManifest;
import com.flamerealm.game.gamemap.Direction;

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
    private boolean atFightPoint;

    public PlayScreen(FlameRealmGame game) {
        super(game);
    }

    @Override
    protected boolean handleInput() {
        if (!Gdx.input.justTouched()) {
            return false;
        }

        // Fiel ao original: este botao nunca e desenhado na tela de jogo,
        // mas o clique ainda e verificado (quirk preservado do main.py). A ordem
        // de checagem (returnToMainMenu antes de pause antes de movimento) e
        // preservada pelo clickFirst, que testa nessa mesma ordem.
        if (clickFirst(instances.nav.returnToMainMenuButton, instances.nav.pauseButton)) {
            return true;
        }

        if ((instances.movement.moveUpButton.mouseInButton() || instances.movement.moveRightButton.mouseInButton()
                || instances.movement.moveLeftButton.mouseInButton() || instances.movement.moveDownButton.mouseInButton())
                && !instances.map.playerObject.getMoving()) {

            if ((instances.movement.moveRightButton.mouseInButton() && moveRight)
                    || (instances.movement.moveLeftButton.mouseInButton() && moveLeft)) {

                instances.map.playerObject.setMoving(true);
                verticalMoving = false;

                if (instances.movement.moveRightButton.mouseInButton()) {
                    currentMapOffset = GameConstants.mapSpeed;
                    instances.map.playerObject.setSprite(instances.map.playerSpriteRight);
                } else {
                    currentMapOffset = -GameConstants.mapSpeed;
                    instances.map.playerObject.setSprite(instances.map.playerSpriteLeft);
                }
            } else if ((instances.movement.moveUpButton.mouseInButton() && moveUp)
                    || (instances.movement.moveDownButton.mouseInButton() && moveDown)) {

                instances.map.playerObject.setMoving(true);
                verticalMoving = true;

                if (instances.movement.moveUpButton.mouseInButton()) {
                    currentMapOffset = -GameConstants.mapSpeed;
                    instances.map.playerObject.setSprite(instances.map.playerSpriteUp);
                } else {
                    currentMapOffset = GameConstants.mapSpeed;
                    instances.map.playerObject.setSprite(instances.map.playerSpriteDown);
                }
            }
        }
        return false;
    }

    @Override
    protected boolean update(float delta) {
        float dt = Math.min(delta, GameConstants.MAX_DELTA);

        if (instances.map.playerObject.getMoving()) {
            instances.map.playerObject.update(dt);
            float step = currentMapOffset * GameConstants.REFERENCE_FPS * dt;
            if (verticalMoving) {
                instances.map.gameMap.getCurrentPoint().setPoint(new Vector2(
                        instances.map.gameMap.getCurrentPoint().getPoint().x,
                        instances.map.gameMap.getCurrentPoint().getPoint().y + step));
            } else {
                instances.map.gameMap.getCurrentPoint().setPoint(new Vector2(
                        instances.map.gameMap.getCurrentPoint().getPoint().x + step,
                        instances.map.gameMap.getCurrentPoint().getPoint().y));
            }

            // Tolerancia proporcional ao passo real deste frame: com passo variavel, uma
            // janela fixa pode ser atravessada inteira num frame lento e o FightPoint
            // nunca dispararia.
            instances.map.gameMap.setTol(Math.max(GameConstants.tolerance, Math.abs(step) * 0.5f));
        } else {
            instances.map.playerObject.setImage(instances.map.playerObject.getFrame()[1]);
        }

        atFightPoint = instances.map.gameMap.fightPointTest();
        if (!atFightPoint) {
            return false;
        }

        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
        instances.map.playerObject.setMoving(false);

        if (instances.map.gameMap.getCurrentPoint().getActivated()) {
            EncounterManifest manifest = instances.map.gameMap.getCurrentPoint().getManifest();
            game.loading.begin(
                    () -> assets.queue(manifest.descriptors()),
                    () -> game.combat.setEncounter(manifest.build(assets)),
                    () -> game.combat);
            game.setScreen(game.loading);
            return true; // aborta o frame: nao desenha por cima da tela nova
        }

        instances.map.gameMap.setPreviousPoint(instances.map.gameMap.getCurrentPoint());

        List<Direction> allowedDirections = instances.map.gameMap.getCurrentPoint().getAllowedDirections();
        moveUp = allowedDirections.contains(Direction.UP);
        moveDown = allowedDirections.contains(Direction.DOWN);
        moveRight = allowedDirections.contains(Direction.RIGHT);
        moveLeft = allowedDirections.contains(Direction.LEFT);

        return false;
    }

    @Override
    protected void draw(float delta) {
        Vector2 currentPos = instances.map.gameMap.getCurrentPoint().getPoint();
        TextureRegion mapRegion = new TextureRegion(instances.map.gameMap.getImage(),
                Math.round(currentPos.x), Math.round(currentPos.y),
                GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        mapRegion.flip(false, true);
        batch.draw(mapRegion, 0, 0);

        batch.draw(instances.map.playerObject.getImage(),
                instances.map.playerObject.getPosition().x, instances.map.playerObject.getPosition().y);

        instances.nav.pauseButton.draw(batch);

        if (!atFightPoint) {
            return;
        }
        if (moveUp)    instances.movement.moveUpButton.draw(batch);
        if (moveDown)  instances.movement.moveDownButton.draw(batch);
        if (moveRight) instances.movement.moveRightButton.draw(batch);
        if (moveLeft)  instances.movement.moveLeftButton.draw(batch);
    }
}
