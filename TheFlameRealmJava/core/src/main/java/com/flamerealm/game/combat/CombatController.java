package com.flamerealm.game.combat;

import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.Assets;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.animation.AnimState;
import com.flamerealm.game.attacks.BossAttack;
import com.flamerealm.game.attacks.PlayerAttack;
import com.flamerealm.game.instances.CombatEncounter;
import com.flamerealm.game.instances.GameInstances;

/**
 * Regra de combate por turnos, extraida de CombatScreen (que virou uma casca
 * fina em torno deste controller, dono de todo o estado por-encontro).
 * Nao conhece Screen/game.setScreen: devolve um {@link CombatOutcome} e quem
 * decide a navegacao e a tela (ver CombatScreen.update). elapsedSinceLastClick
 * substitui o time.time()-lastClick do Python por um acumulador de delta.
 */
public class CombatController {
    private static final float WAIT_TIME = 11f;

    private enum Turn { PLAYER, ENEMY }

    private final GameInstances instances;
    private final Assets assets;

    private Turn turnoAtual = Turn.PLAYER;
    private PlayerAttack playerLastAtk;
    private BossAttack enemyLastAtk;
    private float elapsedSinceLastClick = WAIT_TIME + 1f;
    private boolean usedFuryOfTheEye;
    private CombatEncounter encounter;
    private CombatEncounter victoryEncounter;

    public CombatController(GameInstances instances, Assets assets) {
        this.instances = instances;
        this.assets = assets;
    }

    /**
     * Chamado pela LoadingScreen (postLoadJob) antes do combate virar ativo.
     * Unico seam de inicio de encontro: todo estado de escopo de encontro
     * (turno, timers, ultimo ataque, uso do Fury) nasce aqui.
     */
    public void setEncounter(CombatEncounter encounter) {
        this.encounter = encounter;
        this.turnoAtual = Turn.PLAYER;
        this.playerLastAtk = null;
        this.enemyLastAtk = null;
        this.elapsedSinceLastClick = WAIT_TIME + 1f;
        this.usedFuryOfTheEye = false;
    }

    /** Trata um clique do player: ataque (se for o turno dele) e o Fury of the Eye. */
    public void handleClick() {
        if (turnoAtual != Turn.PLAYER) {
            return;
        }

        playerLastAtk = instances.combat.playerCombatForm.atkCheck();

        if (playerLastAtk != null && playerLastAtk.getMana() <= instances.combat.playerCombatForm.getManaPoints()) {
            turnoAtual = Turn.ENEMY;
            elapsedSinceLastClick = 0f;
            playerLastAtk.setIsOver(false);
            Vector2 alvo = encounter.boss.getCenter();
            playerLastAtk.centerOn(alvo.x, alvo.y);
            playerLastAtk.getAtkButton().getText().setMessage(playerLastAtk.getName() + "\nDamage/Uses: "
                    + playerLastAtk.getDamage() + "/" + playerLastAtk.getMana());
            instances.combat.playerCombatForm.setManaPoints(instances.combat.playerCombatForm.getManaPoints() - playerLastAtk.getMana());
            encounter.boss.setHealthPoints(encounter.boss.getHealthPoints() - playerLastAtk.getDamage());
            instances.combat.playerCombatForm.setState(AnimState.ATTACK);
            encounter.boss.setState(AnimState.HURT);

            instances.combat.playerCombatFormManaText.setMessage("Mana: " + instances.combat.playerCombatForm.getManaPoints());
            encounter.bossHpText.setMessage("HP: " + encounter.boss.getHealthPoints());
        }

        if (instances.combat.secretEyeButton.mouseInButton() && !usedFuryOfTheEye) {
            encounter.boss.setHealthPoints(encounter.boss.getHealthPoints() - GameConstants.furyOfTheEyeDamage);
            encounter.bossHpText.setMessage("HP: " + encounter.boss.getHealthPoints());
            instances.combat.furyOfTheEye.setIsOver(false);
            usedFuryOfTheEye = true;
        }
    }

    /** Avanca turno/timers/animacoes um frame. Ver CombatOutcome para o contrato de saida. */
    public CombatOutcome update(float delta) {
        elapsedSinceLastClick += delta;
        updateFury(delta);
        if (resolveTurnTimeout(delta)) {
            return CombatOutcome.DEFEAT;
        }
        if (resolvePlayerAttack(delta)) {
            return CombatOutcome.VICTORY;
        }
        resolveEnemyTurn(delta);

        encounter.boss.update(delta);
        instances.combat.playerCombatForm.update(delta);

        return CombatOutcome.ONGOING;
    }

    /** Avanca a animacao do Fury of the Eye enquanto ela estiver em curso. */
    private void updateFury(float delta) {
        if (usedFuryOfTheEye && !instances.combat.furyOfTheEye.getIsOver()) {
            instances.combat.furyOfTheEye.update(delta);
        }
    }

    /**
     * Fim da janela do turno (timeout): devolve o turno ao player e, se o
     * player estiver com HP zerado, toca a sequencia de morte e reseta tudo.
     * Derrota nao passa por LoadingScreen: e sincrona, ao contrario da vitoria.
     */
    private boolean resolveTurnTimeout(float delta) {
        if (elapsedSinceLastClick > WAIT_TIME) {
            turnoAtual = Turn.PLAYER;
            enemyLastAtk = null;

            if (instances.combat.playerCombatForm.getHealthPoints() == 0) {
                instances.combat.playerCombatForm.setState(AnimState.DEATH);
                if (instances.combat.playerCombatForm.isDeathAnimationFinished()) {
                    instances.combat.playerCombatForm.revive(GameConstants.playerHp);
                    instances.combat.playerCombatFormHpText.setMessage("HP: " + instances.combat.playerCombatForm.getHealthPoints());

                    instances.combat.playerCombatForm.setManaPoints(GameConstants.playerMana);
                    instances.combat.playerCombatFormManaText.setMessage("Mana: " + instances.combat.playerCombatForm.getManaPoints());

                    encounter.boss.revive(encounter.maxHp);
                    encounter.bossHpText.setMessage("HP: " + encounter.maxHp);

                    instances.map.gameMap.setCurrentPoint(instances.map.gameMap.getPreviousPoint());
                    encounter.release(assets);
                    encounter = null;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Anima o ultimo ataque do player em curso; se ja terminou e o boss esta
     * com HP zerado, toca a sequencia de morte do boss e captura o encontro
     * para a tela montar a transicao de vitoria via LoadingScreen (ver
     * consumeVictoryEncounter/applyVictoryRewards).
     */
    private boolean resolvePlayerAttack(float delta) {
        if (playerLastAtk != null) {
            if (!playerLastAtk.getIsOver()) {
                playerLastAtk.update(delta);
            } else if (encounter.boss.getHealthPoints() == 0) {
                encounter.boss.setState(AnimState.DEATH);
                if (encounter.boss.isDeathAnimationFinished()) {
                    victoryEncounter = encounter;
                    turnoAtual = Turn.PLAYER;
                    encounter = null;
                    return true;
                }
            }
        }
        return false;
    }

    /** Janela de contra-ataque do inimigo: escolhe o ataque uma vez e anima ate terminar. */
    private void resolveEnemyTurn(float delta) {
        if (isEnemyAttackWindowActive()) {
            if (enemyLastAtk == null) {
                enemyLastAtk = encounter.boss.randomizeAtk();
                enemyLastAtk.randomizeHitKill();
                enemyLastAtk.setIsOver(false);
                Vector2 alvo = instances.combat.playerCombatForm.getCenter();
                enemyLastAtk.centerOn(alvo.x, alvo.y);

                instances.combat.playerCombatForm.setHealthPoints(instances.combat.playerCombatForm.getHealthPoints() - enemyLastAtk.getDamage());
                instances.combat.playerCombatFormHpText.setMessage("HP: " + instances.combat.playerCombatForm.getHealthPoints());
                encounter.boss.setState(AnimState.ATTACK);
                instances.combat.playerCombatForm.setState(AnimState.HURT);
            }

            if (!enemyLastAtk.getIsOver()) {
                enemyLastAtk.update(delta);
            }
        }
    }

    /**
     * Recolhe o encontro capturado no momento da vitoria (ver resolvePlayerAttack),
     * para a tela montar os callbacks de LoadingScreen.beginTransition(...).
     * Chamar uma unica vez por CombatOutcome.VICTORY.
     */
    public CombatEncounter consumeVictoryEncounter() {
        CombatEncounter enc = victoryEncounter;
        victoryEncounter = null;
        return enc;
    }

    /**
     * Efeitos colaterais da vitoria (revive do player e do boss, HP/mana,
     * gameMap). Chamado pela tela dentro do postLoadJob de
     * LoadingScreen.beginTransition(...), para preservar o mesmo timing (meio
     * da transicao, tela apagada) que o codigo original tinha antes da extracao.
     */
    public void applyVictoryRewards(CombatEncounter enc) {
        instances.combat.playerCombatForm.revive(GameConstants.playerHp);
        instances.combat.playerCombatFormHpText.setMessage("HP: " + instances.combat.playerCombatForm.getHealthPoints());
        instances.combat.playerCombatForm.setManaPoints(GameConstants.playerMana);
        instances.combat.playerCombatFormManaText.setMessage("Mana: " + instances.combat.playerCombatForm.getManaPoints());

        enc.boss.revive(enc.maxHp);
        enc.bossHpText.setMessage("HP: " + enc.maxHp);

        instances.map.gameMap.disableCurrentPoint();
        instances.map.gameMap.setPreviousPoint(instances.map.gameMap.getCurrentPoint());
    }

    public CombatEncounter getEncounter() {
        return encounter;
    }

    public PlayerAttack getPlayerLastAtk() {
        return playerLastAtk;
    }

    public BossAttack getEnemyLastAtk() {
        return enemyLastAtk;
    }

    public boolean isUsedFuryOfTheEye() {
        return usedFuryOfTheEye;
    }

    /** true durante a janela em que o contra-ataque do inimigo deve ser desenhado/animado. */
    public boolean isEnemyAttackWindowActive() {
        return turnoAtual == Turn.ENEMY && elapsedSinceLastClick >= WAIT_TIME / 2f && elapsedSinceLastClick <= WAIT_TIME;
    }
}
