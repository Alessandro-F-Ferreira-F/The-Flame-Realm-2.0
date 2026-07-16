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

    public static final EnemySpec THEAD_DARKUS = new EnemySpec("Thead Darkus", 400,
            new CombatBodySpec(new Vector2(0, SCREEN_HEIGHT * 0.3f),
                    Map.of(AnimState.IDLE, new AnimationSpec("Images/Characters/Boss/Thead Darkus.png",
                            15, new GridPoint2(224, 240), 0.18f))),
            List.of(
                    new BossAttackSpec("Flamelash", 10, 70, 3,
                            new AnimationSpec("Images/Attacks/Boss/Flamelash.png", 45, new GridPoint2(100, 100), 0.6f),
                            new Vector2(SCREEN_WIDTH * 0.82f, SCREEN_HEIGHT * 0.5f)),
                    new BossAttackSpec("Fire Soul", 40, 80, 3,
                            new AnimationSpec("Images/Attacks/Boss/Fire Soul.png", 60, new GridPoint2(50, 50), 0.7f),
                            new Vector2(SCREEN_WIDTH * 0.84f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("Elden Ring", 30, 90, 4,
                            new AnimationSpec("Images/Attacks/Boss/Elden Ring.png", 60, new GridPoint2(40, 40), 0.9f),
                            new Vector2(SCREEN_WIDTH * 0.848f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("Dark Vortex", 100, 100, 1,
                            new AnimationSpec("Images/Attacks/Boss/Dark Vortex.png", 61, new GridPoint2(128, 128), 0.2f),
                            new Vector2(SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT * 0.45f))
            ));

    public static final EnemySpec NECROMANCER = new EnemySpec("Necromancer", 400,
            new CombatBodySpec(new Vector2(0, SCREEN_HEIGHT * 0.3f), Map.of(
                    AnimState.IDLE, new AnimationSpec(NECROMANCER_SHEET, 8, new GridPoint2(160, 128), 0.18f, new GridPoint2(0, 0)),
                    AnimState.ATTACK, new AnimationSpec(NECROMANCER_SHEET, 13, new GridPoint2(160, 128), 0.3f, new GridPoint2(0, 256)),
                    AnimState.HURT, new AnimationSpec(NECROMANCER_SHEET, 5, new GridPoint2(160, 128), 0.3f, new GridPoint2(0, 640)),
                    AnimState.DEATH, new AnimationSpec(NECROMANCER_SHEET, 9, new GridPoint2(160, 128), 0.25f, new GridPoint2(0, 768))
            )),
            List.of(
                    new BossAttackSpec("Soul Flame", 10, 70, 3,
                            new AnimationSpec("Images/Attacks/Boss/Flamelash.png", 45, new GridPoint2(100, 100), 0.6f),
                            new Vector2(SCREEN_WIDTH * 0.82f, SCREEN_HEIGHT * 0.5f)),
                    new BossAttackSpec("Withering Soul", 40, 80, 3,
                            new AnimationSpec("Images/Attacks/Boss/Fire Soul.png", 60, new GridPoint2(50, 50), 0.7f),
                            new Vector2(SCREEN_WIDTH * 0.84f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("Bone Circle", 30, 90, 4,
                            new AnimationSpec("Images/Attacks/Boss/Elden Ring.png", 60, new GridPoint2(40, 40), 0.9f),
                            new Vector2(SCREEN_WIDTH * 0.848f, SCREEN_HEIGHT * 0.55f)),
                    new BossAttackSpec("Void Maw", 100, 100, 1,
                            new AnimationSpec("Images/Attacks/Boss/Dark Vortex.png", 61, new GridPoint2(128, 128), 0.2f),
                            new Vector2(SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT * 0.45f))
            ));
}
