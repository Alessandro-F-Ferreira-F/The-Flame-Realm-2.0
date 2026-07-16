package com.flamerealm.game.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.Assets;
import com.flamerealm.game.animation.AnimationSpec;

/** Ataque do inimigo: efeito com posicao propria + chance de hit-kill. */
public record BossAttackSpec(String name, int damage, int hitKillChance, int loops, AnimationSpec clip, Vector2 position) {
    public BossAttack build(Assets assets) {
        Texture texture = assets.texture(clip.sheetPath());
        return new BossAttack(name, damage, texture, loops, clip.frames(), clip.frameSize(),
                position, clip.speed(), hitKillChance);
    }
}
