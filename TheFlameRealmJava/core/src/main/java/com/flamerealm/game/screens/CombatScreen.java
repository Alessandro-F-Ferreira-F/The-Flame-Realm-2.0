package com.flamerealm.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.attacks.BossAttack;
import com.flamerealm.game.attacks.PlayerAttack;
import com.flamerealm.game.combat.CombatController;
import com.flamerealm.game.combat.CombatOutcome;
import com.flamerealm.game.instances.CombatEncounter;

/**
 * Port do bloco "Combat screen" de main.py: casca fina que dirige um
 * CombatController (dono da regra de turno/timers/vitoria/derrota) e cuida
 * so de input bruto, navegacao entre telas e desenho.
 */
public class CombatScreen extends BaseScreen {
    private final CombatController controller;

    public CombatScreen(FlameRealmGame game) {
        super(game);
        controller = new CombatController(instances, assets);
    }

    /**
     * Chamado pela LoadingScreen (postLoadJob) antes desta tela virar ativa.
     * Delega ao controller, unico dono do estado de escopo de encontro.
     */
    public void setEncounter(CombatEncounter encounter) {
        controller.setEncounter(encounter);
    }

    @Override
    protected boolean handleInput() {
        if (Gdx.input.justTouched()) {
            controller.handleClick();
        }
        return false;
    }

    @Override
    protected boolean update(float delta) {
        CombatOutcome outcome = controller.update(delta);
        switch (outcome) {
            case DEFEAT:
                game.setScreen(game.death);
                return true;

            case VICTORY:
                CombatEncounter enc = controller.consumeVictoryEncounter();
                game.loading.beginTransition(
                        () -> enc.release(assets),
                        () -> controller.applyVictoryRewards(enc),
                        () -> game.play);
                game.setScreen(game.loading);
                return true;

            default:
                return false;
        }
    }

    @Override
    protected void draw(float delta) {
        CombatEncounter encounter = controller.getEncounter();

        instances.combat.secretEyeButton.draw(batch);

        batch.draw(instances.combat.battleScreen, 0, 0);

        batch.draw(encounter.boss.getImage(), encounter.boss.getPosition().x, encounter.boss.getPosition().y);
        batch.draw(instances.combat.playerCombatForm.getImage(), instances.combat.playerCombatForm.getPosition().x, instances.combat.playerCombatForm.getPosition().y);

        batch.setColor(GameConstants.black);
        batch.draw(assets.whitePixel(), 0, GameConstants.SCREEN_HEIGHT * 0.75f,
                GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT * 0.25f);

        float bossHpWidth = (encounter.boss.getHealthPoints() / (float) encounter.maxHp) * GameConstants.bossHpBarMaxWidth;
        float playerHpWidth = (instances.combat.playerCombatForm.getHealthPoints() / (float) GameConstants.playerHp) * GameConstants.playerHpBarMaxWidth;
        float playerManaWidth = (instances.combat.playerCombatForm.getManaPoints() / (float) GameConstants.playerMana) * GameConstants.playerManaBarMaxWidth;

        batch.setColor(GameConstants.red);
        batch.draw(assets.whitePixel(), GameConstants.SCREEN_WIDTH * 0.035f, GameConstants.SCREEN_HEIGHT * 0.86f,
                bossHpWidth, 15);
        batch.draw(assets.whitePixel(), GameConstants.SCREEN_WIDTH * 0.82f, GameConstants.SCREEN_HEIGHT * 0.8f,
                playerHpWidth, 15);

        batch.setColor(GameConstants.blue);
        batch.draw(assets.whitePixel(), GameConstants.SCREEN_WIDTH * 0.82f, GameConstants.SCREEN_HEIGHT * 0.89f,
                playerManaWidth, 15);
        batch.setColor(Color.WHITE);

        encounter.bossHpText.draw(batch, GameConstants.SCREEN_WIDTH * 0.035f, GameConstants.SCREEN_HEIGHT * 0.9f);
        instances.combat.playerCombatFormHpText.draw(batch, GameConstants.SCREEN_WIDTH * 0.82f, GameConstants.SCREEN_HEIGHT * 0.83f);
        instances.combat.playerCombatFormManaText.draw(batch, GameConstants.SCREEN_WIDTH * 0.82f, GameConstants.SCREEN_HEIGHT * 0.92f);

        instances.combat.azuringButton.draw(batch);
        instances.combat.vortexButton.draw(batch);
        instances.combat.nebulaButton.draw(batch);
        instances.combat.razorButton.draw(batch);

        if (controller.isUsedFuryOfTheEye() && !instances.combat.furyOfTheEye.getIsOver()) {
            batch.draw(instances.combat.furyOfTheEye.getImage(), instances.combat.furyOfTheEye.getPosition().x, instances.combat.furyOfTheEye.getPosition().y);
        }

        PlayerAttack playerLastAtk = controller.getPlayerLastAtk();
        if (playerLastAtk != null && !playerLastAtk.getIsOver()) {
            batch.draw(playerLastAtk.getImage(), playerLastAtk.getPosition().x, playerLastAtk.getPosition().y);
        }

        BossAttack enemyLastAtk = controller.getEnemyLastAtk();
        if (controller.isEnemyAttackWindowActive() && enemyLastAtk != null && !enemyLastAtk.getIsOver()) {
            batch.draw(enemyLastAtk.getImage(), enemyLastAtk.getPosition().x, enemyLastAtk.getPosition().y);
        }
    }
}
