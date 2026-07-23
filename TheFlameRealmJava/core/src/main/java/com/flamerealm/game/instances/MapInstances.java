package com.flamerealm.game.instances;

import com.badlogic.gdx.graphics.Texture;
import com.flamerealm.game.characters.CharacterEntity;
import com.flamerealm.game.gamemap.FightPoint;
import com.flamerealm.game.gamemap.GameMap;

/**
 * Holder do dominio "mapa e player no mundo": so agrupa objetos ja construidos
 * por GameInstances, sem logica propria.
 */
public class MapInstances {
    public final GameMap gameMap;
    public final Texture gameMapImage;

    public final FightPoint startPoint;
    public final FightPoint centralFightPoint;
    public final FightPoint rightFightPoint;
    public final FightPoint leftFightPoint;
    public final FightPoint highFightPoint;

    public final CharacterEntity playerObject;
    public final Texture playerSpriteUp;
    public final Texture playerSpriteDown;
    public final Texture playerSpriteRight;
    public final Texture playerSpriteLeft;

    public MapInstances(GameMap gameMap, Texture gameMapImage,
            FightPoint startPoint, FightPoint centralFightPoint, FightPoint rightFightPoint,
            FightPoint leftFightPoint, FightPoint highFightPoint,
            CharacterEntity playerObject, Texture playerSpriteUp, Texture playerSpriteDown,
            Texture playerSpriteRight, Texture playerSpriteLeft) {
        this.gameMap = gameMap;
        this.gameMapImage = gameMapImage;
        this.startPoint = startPoint;
        this.centralFightPoint = centralFightPoint;
        this.rightFightPoint = rightFightPoint;
        this.leftFightPoint = leftFightPoint;
        this.highFightPoint = highFightPoint;
        this.playerObject = playerObject;
        this.playerSpriteUp = playerSpriteUp;
        this.playerSpriteDown = playerSpriteDown;
        this.playerSpriteRight = playerSpriteRight;
        this.playerSpriteLeft = playerSpriteLeft;
    }
}
