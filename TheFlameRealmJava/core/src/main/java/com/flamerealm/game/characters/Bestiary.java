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
    private static final String THEAD_DARKUS_SHEET = "Images/Characters/Boss/Thead Darkus.png";
    private static final String WHITE_NECROMANCER_SHEET = "Images/Characters/Boss/Thead Darkus White.png";
    private static final String PURPLE_NECROMANCER_SHEET = "Images/Characters/Boss/Thead Darkus Purple.png";

    public static final EnemySpec THEAD_DARKUS = new EnemySpec("Thead Darkus", 400,
            new CombatBodySpec(new Vector2(0, SCREEN_HEIGHT * 0.28f), Map.of(
                    AnimState.IDLE, new AnimationSpec(THEAD_DARKUS_SHEET, 8, new GridPoint2(300, 300), 0.12f, new GridPoint2(0, 0)),
                    AnimState.ATTACK, new AnimationSpec(THEAD_DARKUS_SHEET, 13, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 300)),
                    AnimState.HURT, new AnimationSpec(THEAD_DARKUS_SHEET, 5, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 600)),
                    AnimState.DEATH, new AnimationSpec(THEAD_DARKUS_SHEET, 9, new GridPoint2(300, 300), 0.175f, new GridPoint2(0, 900))
            )),
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

    // Recolor branco/azul do Necromancer (sheet remontada em celulas 300x300,
    // 4 fileiras: IDLE/ATTACK/HURT/DEATH). Mesmo moveset do Thead Darkus.
    public static final EnemySpec WHITE_NECROMANCER = new EnemySpec("Frost Darkus", 400,
            new CombatBodySpec(new Vector2(0, SCREEN_HEIGHT * 0.28f), Map.of(
                    AnimState.IDLE, new AnimationSpec(WHITE_NECROMANCER_SHEET, 8, new GridPoint2(300, 300), 0.12f, new GridPoint2(0, 0)),
                    AnimState.ATTACK, new AnimationSpec(WHITE_NECROMANCER_SHEET, 13, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 300)),
                    AnimState.HURT, new AnimationSpec(WHITE_NECROMANCER_SHEET, 5, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 600)),
                    AnimState.DEATH, new AnimationSpec(WHITE_NECROMANCER_SHEET, 9, new GridPoint2(300, 300), 0.175f, new GridPoint2(0, 900))
            )),
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

    // Recolor roxo do Necromancer (base de cor: Thead Darkus.png rotacionada p/ roxo).
    // Mesma sheet 300x300 / 4 fileiras do WHITE_NECROMANCER; mesmo moveset.
    public static final EnemySpec PURPLE_NECROMANCER = new EnemySpec("Void Darkus", 400,
            new CombatBodySpec(new Vector2(0, SCREEN_HEIGHT * 0.28f), Map.of(
                    AnimState.IDLE, new AnimationSpec(PURPLE_NECROMANCER_SHEET, 8, new GridPoint2(300, 300), 0.12f, new GridPoint2(0, 0)),
                    AnimState.ATTACK, new AnimationSpec(PURPLE_NECROMANCER_SHEET, 13, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 300)),
                    AnimState.HURT, new AnimationSpec(PURPLE_NECROMANCER_SHEET, 5, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 600)),
                    AnimState.DEATH, new AnimationSpec(PURPLE_NECROMANCER_SHEET, 9, new GridPoint2(300, 300), 0.175f, new GridPoint2(0, 900))
            )),
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
            new CombatBodySpec(new Vector2(0, SCREEN_HEIGHT * 0.28f), Map.of(
                    AnimState.IDLE, new AnimationSpec(NECROMANCER_SHEET, 8, new GridPoint2(300, 300), 0.12f, new GridPoint2(0, 0)),
                    AnimState.ATTACK, new AnimationSpec(NECROMANCER_SHEET, 13, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 300)),
                    AnimState.HURT, new AnimationSpec(NECROMANCER_SHEET, 5, new GridPoint2(300, 300), 0.2f, new GridPoint2(0, 600)),
                    AnimState.DEATH, new AnimationSpec(NECROMANCER_SHEET, 9, new GridPoint2(300, 300), 0.175f, new GridPoint2(0, 900))
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
