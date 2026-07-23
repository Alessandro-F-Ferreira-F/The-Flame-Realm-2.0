package com.flamerealm.game.characters;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.animation.AnimState;
import com.flamerealm.game.animation.AnimationSpec;
import com.flamerealm.game.attacks.BossAttackSpec;

import java.util.List;
import java.util.Map;

import static com.flamerealm.game.GameConstants.SCREEN_HEIGHT;
import static com.flamerealm.game.GameConstants.SCREEN_WIDTH;

/** Registro dos EnemySpec do jogo. */
public final class Bestiary {
    private Bestiary() {}

    private static final String NECROMANCER_SHEET = "Images/Characters/Boss/Necromancer_creativekind-Sheet.png";
    private static final String FIRE_PHANTON_SHEET = "Images/Characters/Boss/Fire Phanton.png";
    private static final String ICE_PHANTON_SHEET = "Images/Characters/Boss/Thead Darkus White.png";
    private static final String DARK_PHANTON_SHEET = "Images/Characters/Boss/Thead Darkus Purple.png";

    public static final EnemySpec FIRE_PHANTON = new EnemySpec("Fire Phanton", 400,
            new CombatBodySpec(new Vector2(0, SCREEN_HEIGHT * 0.28f), Map.of(
                    AnimState.IDLE, new AnimationSpec(FIRE_PHANTON_SHEET, 8, new GridPoint2(300, 300), 0.12f, new GridPoint2(0, 0)),
                    AnimState.ATTACK, new AnimationSpec(FIRE_PHANTON_SHEET, 13, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 300)),
                    AnimState.HURT, new AnimationSpec(FIRE_PHANTON_SHEET, 5, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 600)),
                    AnimState.DEATH, new AnimationSpec(FIRE_PHANTON_SHEET, 9, new GridPoint2(300, 300), 0.175f, new GridPoint2(0, 900))
            )),
            List.of(
                    new BossAttackSpec("Flamelash", 10, 70, 3,
                            new AnimationSpec("Images/Attacks/Boss/Fire Phanton/Flamelash.png", 45, new GridPoint2(100, 100), 0.6f),
                            new Vector2(SCREEN_WIDTH * 0.82f, SCREEN_HEIGHT * 0.5f)),
                    new BossAttackSpec("SunBurn", 40, 80, 3,
                            new AnimationSpec("Images/Attacks/Boss/Fire Phanton/SunBurn.png", 25, new GridPoint2(150, 150), 0.7f),
                            new Vector2(SCREEN_WIDTH * 0.84f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("Fire Vortex", 30, 90, 4,
                            new AnimationSpec("Images/Attacks/Boss/Fire Phanton/FireVortex.png", 12, new GridPoint2(64, 64), 0.9f),
                            new Vector2(SCREEN_WIDTH * 0.848f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("Explosion", 100, 100, 1,
                            new AnimationSpec("Images/Attacks/Boss/Fire Phanton/Explosion.png", 52, new GridPoint2(100, 103), 0.2f),
                            new Vector2(SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT * 0.45f))
            ));

    public static final EnemySpec ICE_PHANTON = new EnemySpec("Ice Phanton", 400,
            new CombatBodySpec(new Vector2(0, SCREEN_HEIGHT * 0.28f), Map.of(
                    AnimState.IDLE, new AnimationSpec(ICE_PHANTON_SHEET, 8, new GridPoint2(300, 300), 0.12f, new GridPoint2(0, 0)),
                    AnimState.ATTACK, new AnimationSpec(ICE_PHANTON_SHEET, 13, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 300)),
                    AnimState.HURT, new AnimationSpec(ICE_PHANTON_SHEET, 5, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 600)),
                    AnimState.DEATH, new AnimationSpec(ICE_PHANTON_SHEET, 9, new GridPoint2(300, 300), 0.175f, new GridPoint2(0, 900))
            )),
            List.of(
                    new BossAttackSpec("Nebula", 10, 70, 3,
                            new AnimationSpec("Images/Attacks/Boss/Ice Phanton/Nebula.png", 10, new GridPoint2(64, 64), 0.6f),
                            new Vector2(SCREEN_WIDTH * 0.82f, SCREEN_HEIGHT * 0.5f)),
                    new BossAttackSpec("Ice Vortex", 40, 80, 3,
                            new AnimationSpec("Images/Attacks/Boss/Ice Phanton/IceVortex.png", 30, new GridPoint2(150, 150), 0.7f),
                            new Vector2(SCREEN_WIDTH * 0.84f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("IceCrystal", 30, 90, 4,
                            new AnimationSpec("Images/Attacks/Boss/Ice Phanton/IceCrystal.png", 61, new GridPoint2(40, 40), 0.9f),
                            new Vector2(SCREEN_WIDTH * 0.848f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("Freezing", 100, 100, 1,
                            new AnimationSpec("Images/Attacks/Boss/Ice Phanton/Freezing.png", 86, new GridPoint2(100, 100), 0.2f),
                            new Vector2(SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT * 0.45f))
            ));

    public static final EnemySpec DARK_PHANTON = new EnemySpec("Dark Phanton", 400,
            new CombatBodySpec(new Vector2(0, SCREEN_HEIGHT * 0.28f), Map.of(
                    AnimState.IDLE, new AnimationSpec(DARK_PHANTON_SHEET, 8, new GridPoint2(300, 300), 0.12f, new GridPoint2(0, 0)),
                    AnimState.ATTACK, new AnimationSpec(DARK_PHANTON_SHEET, 13, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 300)),
                    AnimState.HURT, new AnimationSpec(DARK_PHANTON_SHEET, 5, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 600)),
                    AnimState.DEATH, new AnimationSpec(DARK_PHANTON_SHEET, 9, new GridPoint2(300, 300), 0.175f, new GridPoint2(0, 900))
            )),
           
            List.of(
                    new BossAttackSpec("MidnightStar", 10, 70, 3,
                            new AnimationSpec("Images/Attacks/Boss/Dark Phanton/MidnightStar.png", 61, new GridPoint2(100, 100), 0.6f),
                            new Vector2(SCREEN_WIDTH * 0.82f, SCREEN_HEIGHT * 0.5f)),
                    
                    new BossAttackSpec("Dark ray", 40, 80, 3,
                            new AnimationSpec("Images/Attacks/Boss/Dark Phanton/DarkRay.png", 19, new GridPoint2(64, 64), 0.7f),
                            new Vector2(SCREEN_WIDTH * 0.84f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("Phanton", 30, 90, 4,
                            new AnimationSpec("Images/Attacks/Boss/Dark Phanton/Phanton.png", 61, new GridPoint2(100, 100), 0.9f),
                            new Vector2(SCREEN_WIDTH * 0.848f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("GravityVortex", 100, 100, 1,
                            new AnimationSpec("Images/Attacks/Boss/Dark Phanton/GravityVortex.png", 20, new GridPoint2(96, 80), 0.2f),
                            new Vector2(SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT * 0.45f))
            ));

    public static final EnemySpec NECROMANCER = new EnemySpec("Necromancer", 400,
            new CombatBodySpec(new Vector2(0, SCREEN_HEIGHT * 0.28f), Map.of(
                    AnimState.IDLE, new AnimationSpec(NECROMANCER_SHEET, 8, new GridPoint2(300, 300), 0.12f, new GridPoint2(0, 0)),
                    AnimState.ATTACK, new AnimationSpec(NECROMANCER_SHEET, 13, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 300)),
                    AnimState.HURT, new AnimationSpec(NECROMANCER_SHEET, 5, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 600)),
                    AnimState.DEATH, new AnimationSpec(NECROMANCER_SHEET, 9, new GridPoint2(300, 300), 0.175f, new GridPoint2(0, 900))
            )),
            List.of(
                    new BossAttackSpec("Dark Impact", 10, 70, 3,
                            new AnimationSpec("Images/Attacks/Boss/Thead Darkus/DarkImpact.png", 25, new GridPoint2(128, 128), 0.6f),
                            new Vector2(SCREEN_WIDTH * 0.82f, SCREEN_HEIGHT * 0.5f)),
                    new BossAttackSpec("Dark Spirit", 40, 80, 3,
                            new AnimationSpec("Images/Attacks/Boss/Thead Darkus/DarkSpirit.png", 81, new GridPoint2(100, 100), 0.7f),
                            new Vector2(SCREEN_WIDTH * 0.84f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("Dark Vortex", 30, 90, 4,
                            new AnimationSpec("Images/Attacks/Boss/Thead Darkus/DarkVortex.png", 60, new GridPoint2(128, 128), 0.9f),
                            new Vector2(SCREEN_WIDTH * 0.848f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("Dark Skull", 100, 100, 1,
                            new AnimationSpec("Images/Attacks/Boss//Thead Darkus/DarkSkull.png", 15, new GridPoint2(64, 64), 0.2f),
                            new Vector2(SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT * 0.45f))
            ));
}
