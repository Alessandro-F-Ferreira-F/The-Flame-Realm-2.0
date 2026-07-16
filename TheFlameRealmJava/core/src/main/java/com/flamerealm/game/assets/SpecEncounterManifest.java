package com.flamerealm.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.flamerealm.game.Assets;
import com.flamerealm.game.animation.AnimState;
import com.flamerealm.game.animation.AnimatedEntity;
import com.flamerealm.game.animation.AnimationSpec;
import com.flamerealm.game.attacks.BossAttack;
import com.flamerealm.game.attacks.BossAttackSpec;
import com.flamerealm.game.characters.CombatForm;
import com.flamerealm.game.characters.EnemySpec;
import com.flamerealm.game.instances.CombatEncounter;
import com.flamerealm.game.ui.GameText;
import com.flamerealm.game.ui.UiFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.flamerealm.game.GameConstants.combatScreenFont;
import static com.flamerealm.game.GameConstants.combatScreenTextSize;
import static com.flamerealm.game.GameConstants.white;

/**
 * Manifesto de encontro generico, dirigido por um EnemySpec do Bestiary. Um
 * unico manifesto serve qualquer inimigo - adicionar um novo inimigo passa a
 * ser declarar um EnemySpec, sem criar classe.
 */
public final class SpecEncounterManifest implements EncounterManifest {
    private final EnemySpec spec;
    private final Array<AssetDescriptor<?>> descriptors;

    public SpecEncounterManifest(EnemySpec spec) {
        this.spec = spec;
        this.descriptors = new Array<>();
        for (AnimationSpec clip : spec.body().states().values()) {
            descriptors.add(new AssetDescriptor<>(clip.sheetPath(), Texture.class));
        }
        for (BossAttackSpec atk : spec.attacks()) {
            descriptors.add(new AssetDescriptor<>(atk.clip().sheetPath(), Texture.class));
        }
    }

    @Override
    public Array<AssetDescriptor<?>> descriptors() {
        return descriptors;
    }

    @Override
    public CombatEncounter build(Assets assets) {
        List<BossAttack> atks = new ArrayList<>();
        for (BossAttackSpec atkSpec : spec.attacks()) {
            atks.add(atkSpec.build(assets));
        }

        Map<AnimState, AnimatedEntity> clips = spec.body().buildClips(assets);
        CombatForm<BossAttack> boss = new CombatForm<>(spec.body().position(), clips, spec.maxHp(), atks);

        GameText bossHpText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white, "HP: " + spec.maxHp());

        return new CombatEncounter(boss, atks, bossHpText, spec.maxHp(), descriptors);
    }
}
