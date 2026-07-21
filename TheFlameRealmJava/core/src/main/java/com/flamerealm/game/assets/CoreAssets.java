package com.flamerealm.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * Manifesto do grupo nucleo: assets sempre residentes, carregados no boot.
 * O chefe (Thead Darkus) nao entra aqui - e um grupo lazy, carregado sob
 * demanda ao entrar em combate (ver TheadDarkusManifest).
 */
public final class CoreAssets implements AssetGroup {
    private static final String[] PATHS = {
            "Images/Interface/Buttons/MovementButtons/MoveUpButton.png",
            "Images/Interface/Buttons/MovementButtons/WhiteMoveUpButton.png",
            "Images/Interface/Buttons/MovementButtons/MoveDownButton.png",
            "Images/Interface/Buttons/MovementButtons/WhiteMoveDownButton.png",
            "Images/Interface/Buttons/MovementButtons/MoveRightButton.png",
            "Images/Interface/Buttons/MovementButtons/WhiteMoveRightButton.png",
            "Images/Interface/Buttons/MovementButtons/MoveLeftButton.png",
            "Images/Interface/Buttons/MovementButtons/WhiteMoveLeftButton.png",
            "Images/Interface/Buttons/Pause Button/PauseButton.png",
            "Images/Interface/Buttons/Pause Button/WhitePauseButton.png",

            "Images/Attacks/Player/Azuring Impact.png",
            "Images/Attacks/Player/Nebula.png",
            "Images/Attacks/Player/Dual Vortex.png",
            "Images/Attacks/Player/Razor.png",

            "Images/PuzzleElements/Fury of the eye.png",

            "Images/Characters/Player/main character (walking up).png",
            "Images/Characters/Player/main character (walking down).png",
            "Images/Characters/Player/main character (walking right).png",
            "Images/Characters/Player/main character (walking left).png",
            "Images/Characters/Player/BlueMageGuardian.png",
            "Images/Characters/Player/BlueMageMoveset.png",

            "Images/Gamemaps/GameMap.png",

            "Images/Interface/Backgrounds/Boss Gate mockup.png",
            "Images/Interface/Backgrounds/Battle Screen.png",
    };

    public static final CoreAssets INSTANCE = new CoreAssets();

    private final Array<AssetDescriptor<?>> descriptors = new Array<>();

    private CoreAssets() {
        for (String path : PATHS) {
            descriptors.add(new AssetDescriptor<>(path, Texture.class));
        }
    }

    @Override
    public Array<AssetDescriptor<?>> descriptors() {
        return descriptors;
    }
}
