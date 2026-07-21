package com.flamerealm.game.instances;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.utils.Array;
import com.flamerealm.game.Assets;
import com.flamerealm.game.attacks.BossAttack;
import com.flamerealm.game.characters.CombatForm;
import com.flamerealm.game.ui.GameText;

/**
 * Objetos de dominio de um encontro especifico (ex.: um chefe), construidos
 * pos-carga pelo EncounterManifest correspondente, apos seus assets terminarem
 * de carregar de forma assincrona. Carrega tambem os proprios descriptors, para
 * que quem descarrega o encontro (CombatScreen) nao precise conhecer o
 * EncounterManifest que o construiu.
 */
public class CombatEncounter {
    public final CombatForm<BossAttack> boss;
    public final GameText bossHpText;
    public final int maxHp;
    private final Array<AssetDescriptor<?>> descriptors;
    private boolean released;

    public CombatEncounter(CombatForm<BossAttack> boss, GameText bossHpText,
            int maxHp, Array<AssetDescriptor<?>> descriptors) {
        this.boss = boss;
        this.bossHpText = bossHpText;
        this.maxHp = maxHp;
        this.descriptors = descriptors;
    }

    /**
     * Devolve os assets do encontro ao AssetManager. Idempotente: chamar duas vezes
     * (ou por dois caminhos de saida diferentes do combate) nao decrementa duas vezes.
     */
    public void release(Assets assets) {
        if (released) {
            return;
        }
        released = true;
        assets.unload(descriptors);
    }
}
