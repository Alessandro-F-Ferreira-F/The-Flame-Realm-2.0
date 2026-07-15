package com.flamerealm.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.utils.Array;

/** Um conjunto de assets a ser enfileirado/descarregado em bloco (nucleo, encontro, etc). */
public interface AssetGroup {
    Array<AssetDescriptor<?>> descriptors();
}
