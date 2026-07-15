package com.flamerealm.game.instances;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.utils.Array;
import com.flamerealm.game.attacks.BossAttack;
import com.flamerealm.game.characters.CombatForm;
import com.flamerealm.game.ui.GameText;

import java.util.List;

/**
 * Objetos de dominio de um encontro especifico (ex.: um chefe), construidos
 * pos-carga pelo EncounterManifest correspondente, apos seus assets terminarem
 * de carregar de forma assincrona. Carrega tambem os proprios descriptors, para
 * que quem descarrega o encontro (CombatScreen) nao precise conhecer o
 * EncounterManifest que o construiu.
 */
public class CombatEncounter {
    public final CombatForm<BossAttack> boss;
    public final List<BossAttack> bossAtks;
    public final GameText bossHpText;
    public final int maxHp;
    public final Array<AssetDescriptor<?>> descriptors;

    public CombatEncounter(CombatForm<BossAttack> boss, List<BossAttack> bossAtks, GameText bossHpText,
            int maxHp, Array<AssetDescriptor<?>> descriptors) {
        this.boss = boss;
        this.bossAtks = bossAtks;
        this.bossHpText = bossHpText;
        this.maxHp = maxHp;
        this.descriptors = descriptors;
    }
}
