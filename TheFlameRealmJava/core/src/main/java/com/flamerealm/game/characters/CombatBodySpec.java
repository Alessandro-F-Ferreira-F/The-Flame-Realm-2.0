package com.flamerealm.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.Assets;
import com.flamerealm.game.animation.AnimState;
import com.flamerealm.game.animation.AnimatedEntity;
import com.flamerealm.game.animation.AnimationSpec;

import java.util.EnumMap;
import java.util.Map;

/**
 * Corpo do combatente em combate: posicao fixa + um AnimationSpec por
 * AnimState. IDLE e obrigatorio; os demais estados variam por combatente.
 */
public record CombatBodySpec(Vector2 position, Map<AnimState, AnimationSpec> states) {
    public CombatBodySpec {
        if (!states.containsKey(AnimState.IDLE)) {
            throw new IllegalArgumentException("CombatBodySpec exige ao menos IDLE");
        }
        states = Map.copyOf(states);
    }

    /** Resolve cada AnimationSpec em um AnimatedEntity, prontos para o CombatForm. */
    public Map<AnimState, AnimatedEntity> buildClips(Assets assets) {
        Map<AnimState, AnimatedEntity> clips = new EnumMap<>(AnimState.class);
        for (Map.Entry<AnimState, AnimationSpec> entry : states.entrySet()) {
            AnimationSpec clip = entry.getValue();
            Texture texture = assets.texture(clip.sheetPath());
            clips.put(entry.getKey(), new AnimatedEntity(texture, clip.frames(), clip.frameSize(), position, clip.speed(), clip.sheetOffset()));
        }
        return clips;
    }
}
