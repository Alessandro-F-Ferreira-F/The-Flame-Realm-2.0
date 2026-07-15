package com.flamerealm.game.assets;

import com.flamerealm.game.Assets;
import com.flamerealm.game.instances.CombatEncounter;

/** Manifesto + construcao de um encontro (chefe/regiao). Um por chefe/regiao. */
public interface EncounterManifest extends AssetGroup {
    /** So chamar depois que descriptors() terminaram de carregar. */
    CombatEncounter build(Assets assets);
}
