package com.flamerealm.game.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.animation.AnimState;
import com.flamerealm.game.animation.AnimatedEntity;
import com.flamerealm.game.attacks.Attack;

import java.util.List;
import java.util.Map;

/**
 * Port de CombatForm.py. Generico em T para manter, com seguranca de tipos, o
 * mesmo "duck typing" do Python: o boss usa uma CombatForm&lt;BossAttack&gt; e o
 * jogador usa PlayerCombatForm (CombatForm&lt;PlayerAttack&gt;).
 *
 * <p>Contem um clipe de animacao por AnimState em vez de estender AnimatedEntity
 * diretamente, para poder trocar de clipe conforme o estado de combate (idle,
 * ataque, dano, morte). IDLE toca em loop; ATTACK/HURT tocam uma vez e voltam a
 * IDLE; DEATH toca uma vez e segura o ultimo frame. setState(...) e um no-op
 * quando o combatente nao tem clipe para o estado pedido.
 */
public class CombatForm<T extends Attack> {
    private final Vector2 position;
    private final Map<AnimState, AnimatedEntity> clips;
    private AnimState currentState = AnimState.IDLE;
    private float progress = 0f;
    private int healthPoints;
    private List<T> atkList;

    public CombatForm(Vector2 position, Map<AnimState, AnimatedEntity> clips, int healthPoints, List<T> atkList) {
        if (!clips.containsKey(AnimState.IDLE)) {
            throw new IllegalArgumentException("CombatForm exige ao menos IDLE");
        }
        this.position = position;
        this.clips = clips;
        this.healthPoints = healthPoints;
        this.atkList = atkList;
    }

    public void setState(AnimState state) {
        if (!clips.containsKey(state) || state == currentState) {
            return;
        }
        currentState = state;
        progress = 0f;
    }

    public void update(float delta) {
        AnimatedEntity clip = clips.get(currentState);
        TextureRegion[] frames = clip.getFrame();
        int qtd = clip.getQtdFrames();

        if (currentState == AnimState.IDLE) {
            if (progress > qtd - 1) {
                progress = 0;
            }
            clip.setImage(frames[Math.round(progress)]);
            progress += clip.getOffsetFrames() * GameConstants.REFERENCE_FPS * delta;
            return;
        }

        if (progress > qtd - 1) {
            clip.setImage(frames[qtd - 1]);
            if (currentState != AnimState.DEATH) {
                setState(AnimState.IDLE);
            }
            return;
        }
        clip.setImage(frames[Math.round(progress)]);
        progress += clip.getOffsetFrames() * GameConstants.REFERENCE_FPS * delta;
    }

    public boolean isDeathAnimationFinished() {
        AnimatedEntity death = clips.get(AnimState.DEATH);
        if (death == null) {
            return true;
        }
        return currentState == AnimState.DEATH && progress > death.getQtdFrames() - 1;
    }

    public TextureRegion getImage() {
        return clips.get(currentState).getImage();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getCenter() {
        TextureRegion img = getImage();
        return new Vector2(
                position.x + img.getRegionWidth() / 2f,
                position.y + img.getRegionHeight() / 2f);
    }

    public T randomizeAtk() {
        int randomizer = MathUtils.random(0, atkList.size() - 1);
        return atkList.get(randomizer);
    }

    public void revive(int hp) {
        setHealthPoints(hp);
        currentState = AnimState.IDLE;
        progress = 0f;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = Math.max(healthPoints, 0);
    }

    public List<T> getAtkList() {
        return atkList;
    }

    public void setAtkList(List<T> atkList) {
        this.atkList = atkList;
    }
}
