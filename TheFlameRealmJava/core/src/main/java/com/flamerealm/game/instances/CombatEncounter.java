package com.flamerealm.game.instances;

import com.flamerealm.game.attacks.BossAttack;
import com.flamerealm.game.characters.CombatForm;
import com.flamerealm.game.ui.GameText;

import java.util.List;

/**
 * Objetos de dominio de um encontro especifico (ex.: um chefe), construidos
 * pos-carga pelo EncounterManifest correspondente, apos seus assets terminarem
 * de carregar de forma assincrona.
 */
public class CombatEncounter {
    public final CombatForm<BossAttack> boss;
    public final List<BossAttack> bossAtks;
    public final GameText bossHpText;

    public CombatEncounter(CombatForm<BossAttack> boss, List<BossAttack> bossAtks, GameText bossHpText) {
        this.boss = boss;
        this.bossAtks = bossAtks;
        this.bossHpText = bossHpText;
    }
}
