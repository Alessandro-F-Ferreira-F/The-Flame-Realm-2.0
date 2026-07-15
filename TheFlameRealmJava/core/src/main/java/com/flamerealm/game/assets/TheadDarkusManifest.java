package com.flamerealm.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.flamerealm.game.Assets;
import com.flamerealm.game.attacks.BossAttack;
import com.flamerealm.game.characters.CombatForm;
import com.flamerealm.game.instances.CombatEncounter;
import com.flamerealm.game.ui.GameText;
import com.flamerealm.game.ui.UiFactory;

import java.util.Arrays;
import java.util.List;

import static com.flamerealm.game.GameConstants.*;

/**
 * Manifesto do encontro com Thead Darkus: spritesheet do chefe + seus 4
 * ataques. Grupo lazy, carregado sob demanda ao entrar em combate (ver
 * PlayScreen). build(assets) reproduz a mesma construcao que antes vivia em
 * GameInstances, so que movida para ca e disparada pos-carga.
 */
public final class TheadDarkusManifest implements EncounterManifest {
    private static final String[] PATHS = {
            "Images/Attacks/Boss/Elden Ring.png",
            "Images/Attacks/Boss/Flamelash.png",
            "Images/Attacks/Boss/Dark Vortex.png",
            "Images/Attacks/Boss/Fire Soul.png",

            "Images/Characters/Boss/Thead Darkus.png",
    };

    public static final TheadDarkusManifest INSTANCE = new TheadDarkusManifest();

    private final Array<AssetDescriptor<?>> descriptors = new Array<>();

    private TheadDarkusManifest() {
        for (String path : PATHS) {
            descriptors.add(new AssetDescriptor<>(path, Texture.class));
        }
    }

    @Override
    public Array<AssetDescriptor<?>> descriptors() {
        return descriptors;
    }

    @Override
    public CombatEncounter build(Assets assets) {
        Texture eldenRingSprite = assets.get("Images/Attacks/Boss/Elden Ring.png", Texture.class);
        BossAttack eldenRing = new BossAttack("Elden Ring", eldenRingDamage, eldenRingSprite, eldenRingLoops, eldenRingFrames,
                eldenRingPixels, eldenRingPosition, eldenRingOffset, eldenRingHitKillChance);

        Texture flamelashSprite = assets.get("Images/Attacks/Boss/Flamelash.png", Texture.class);
        BossAttack flamelash = new BossAttack("Flamelash", flamelashDamage, flamelashSprite, flamelashLoops, flamelashFrames,
                flamelashPixels, flamelashPosition, flamelashOffset, flamelashHitKillChance);

        Texture darkVortexSprite = assets.get("Images/Attacks/Boss/Dark Vortex.png", Texture.class);
        BossAttack darkVortex = new BossAttack("Dark Vortex", darkVortexDamage, darkVortexSprite, darkVortexLoops, darkVortexFrames,
                darkVortexPixels, darkVortexPosition, darkVortexOffset, darkVortexHitKillChance);

        Texture fireSoulSprite = assets.get("Images/Attacks/Boss/Fire Soul.png", Texture.class);
        BossAttack fireSoul = new BossAttack("Fire Soul", fireSoulDamage, fireSoulSprite, fireSoulLoops, fireSoulFrames,
                fireSoulPixels, fireSoulPosition, fireSoulOffset, fireSoulHitKillChance);

        List<BossAttack> theadDarkusAtks = Arrays.asList(flamelash, fireSoul, eldenRing, darkVortex);

        Texture theadDarkusImage = assets.get("Images/Characters/Boss/Thead Darkus.png", Texture.class);
        CombatForm<BossAttack> boss = new CombatForm<>(theadDarkusImage, theadDarkusFrames, theadDarkusSize, theadDarkusPosition,
                theadDarkusOffset, theadDarkusHp, theadDarkusAtks);

        GameText bossHpText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white, "HP: " + theadDarkusHp);

        return new CombatEncounter(boss, theadDarkusAtks, bossHpText);
    }
}
