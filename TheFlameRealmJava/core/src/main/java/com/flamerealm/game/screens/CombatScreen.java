package com.flamerealm.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.flamerealm.game.FlameRealmGame;
import com.flamerealm.game.GameConstants;
import com.flamerealm.game.attacks.BossAttack;
import com.flamerealm.game.attacks.PlayerAttack;

/**
 * Port do bloco "Combat screen" de main.py: combate por turnos entre
 * playerCombatForm e theadDarkus. elapsedSinceLastClick substitui o
 * time.time()-lastClick do Python por um acumulador de delta, que so avanca
 * enquanto esta tela esta ativa - exatamente como o bloco Python so rodava
 * dentro do elif booleans.getCombatScreen().
 */
public class CombatScreen extends BaseScreen {
    private static final float WAIT_TIME = 11f;

    private enum Turn { PLAYER, ENEMY }

    private Turn turnoAtual = Turn.PLAYER;
    private PlayerAttack playerLastAtk;
    private BossAttack enemyLastAtk;
    private float elapsedSinceLastClick = WAIT_TIME + 1f;
    private boolean usedFuryOfTheEye;

    public CombatScreen(FlameRealmGame game) {
        super(game);
    }

    @Override
    public void show() {
        usedFuryOfTheEye = false;
    }

    @Override
    protected boolean handleInput() {
        elapsedSinceLastClick += Gdx.graphics.getDeltaTime();

        if (!Gdx.input.justTouched() || turnoAtual != Turn.PLAYER) {
            return false;
        }

        playerLastAtk = instances.playerCombatForm.atkCheck();

        if (playerLastAtk != null && playerLastAtk.getMana() <= instances.playerCombatForm.getManaPoints()) {
            turnoAtual = Turn.ENEMY;
            elapsedSinceLastClick = 0f;
            playerLastAtk.setIsOver(false);
            playerLastAtk.getAtkButton().getText().setMessage(playerLastAtk.getName() + "\nDamage/Uses: "
                    + playerLastAtk.getDamage() + "/" + playerLastAtk.getMana());
            instances.playerCombatForm.setManaPoints(instances.playerCombatForm.getManaPoints() - playerLastAtk.getMana());
            instances.theadDarkus.setHealthPoints(instances.theadDarkus.getHealthPoints() - playerLastAtk.getDamage());

            instances.playerCombatFormManaText.setMessage("Mana: " + instances.playerCombatForm.getManaPoints());
            instances.theadDarkusHpText.setMessage("HP: " + instances.theadDarkus.getHealthPoints());
        }

        if (instances.secretEyeButton.mouseInButton() && !usedFuryOfTheEye) {
            instances.theadDarkus.setHealthPoints(instances.theadDarkus.getHealthPoints() - GameConstants.furyOfTheEyeDamage);
            instances.theadDarkusHpText.setMessage("HP: " + instances.theadDarkus.getHealthPoints());
            instances.furyOfTheEye.setIsOver(false);
            usedFuryOfTheEye = true;
        }

        return false;
    }

    @Override
    protected boolean update(float delta) {
        if (usedFuryOfTheEye && !instances.furyOfTheEye.getIsOver()) {
            instances.furyOfTheEye.update();
        }

        if (elapsedSinceLastClick > WAIT_TIME) {
            turnoAtual = Turn.PLAYER;
            enemyLastAtk = null;

            if (instances.playerCombatForm.getHealthPoints() == 0) {
                instances.playerCombatForm.revive(GameConstants.playerHp);
                instances.playerCombatFormHpText.setMessage("HP: " + instances.playerCombatForm.getHealthPoints());

                instances.playerCombatForm.setManaPoints(GameConstants.playerMana);
                instances.playerCombatFormManaText.setMessage("Mana: " + instances.playerCombatForm.getManaPoints());

                instances.theadDarkus.revive(GameConstants.theadDarkusHp);
                instances.theadDarkusHpText.setMessage("HP: " + GameConstants.theadDarkusHp);

                instances.gameMap.setCurrentPoint(instances.gameMap.getPreviousPoint());
                game.setScreen(game.death);
                return true;
            }
        }

        if (playerLastAtk != null) {
            if (!playerLastAtk.getIsOver()) {
                playerLastAtk.update();
            } else if (instances.theadDarkus.getHealthPoints() == 0) {
                turnoAtual = Turn.PLAYER;
                instances.playerCombatForm.revive(GameConstants.playerHp);
                instances.playerCombatFormHpText.setMessage("HP: " + instances.playerCombatForm.getHealthPoints());

                instances.playerCombatForm.setManaPoints(GameConstants.playerMana);
                instances.playerCombatFormManaText.setMessage("Mana: " + instances.playerCombatForm.getManaPoints());

                instances.theadDarkus.revive(GameConstants.theadDarkusHp);
                instances.theadDarkusHpText.setMessage("HP: " + GameConstants.theadDarkusHp);

                instances.gameMap.disableCurrentPoint();
                instances.gameMap.setPreviousPoint(instances.gameMap.getCurrentPoint());
                game.setScreen(game.play);
                return true;
            }
        }

        if (turnoAtual == Turn.ENEMY && elapsedSinceLastClick >= WAIT_TIME / 2f && elapsedSinceLastClick <= WAIT_TIME) {
            if (enemyLastAtk == null) {
                enemyLastAtk = instances.theadDarkus.randomizeAtk();
                enemyLastAtk.randomizeHitKill();
                enemyLastAtk.setIsOver(false);

                instances.playerCombatForm.setHealthPoints(instances.playerCombatForm.getHealthPoints() - enemyLastAtk.getDamage());
                instances.playerCombatFormHpText.setMessage("HP: " + instances.playerCombatForm.getHealthPoints());
            }

            if (!enemyLastAtk.getIsOver()) {
                enemyLastAtk.update();
            }
        }

        instances.theadDarkus.update();
        instances.playerCombatForm.update();

        return false;
    }

    @Override
    protected void draw(float delta) {
        instances.secretEyeButton.draw(batch);

        batch.draw(instances.battleScreen, 0, 0);

        batch.draw(instances.theadDarkus.getImage(), instances.theadDarkus.getPosition().x, instances.theadDarkus.getPosition().y);
        batch.draw(instances.playerCombatForm.getImage(), instances.playerCombatForm.getPosition().x, instances.playerCombatForm.getPosition().y);

        batch.setColor(GameConstants.black);
        batch.draw(assets.whitePixel(), 0, GameConstants.SCREEN_HEIGHT * 0.75f,
                GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT * 0.25f);

        batch.setColor(GameConstants.red);
        batch.draw(assets.whitePixel(), GameConstants.SCREEN_WIDTH * 0.035f, GameConstants.SCREEN_HEIGHT * 0.86f,
                instances.theadDarkus.getHealthPoints() * 0.4f, 15);
        batch.draw(assets.whitePixel(), GameConstants.SCREEN_WIDTH * 0.82f, GameConstants.SCREEN_HEIGHT * 0.8f,
                instances.playerCombatForm.getHealthPoints() * 0.8f, 15);

        batch.setColor(GameConstants.blue);
        batch.draw(assets.whitePixel(), GameConstants.SCREEN_WIDTH * 0.82f, GameConstants.SCREEN_HEIGHT * 0.89f,
                instances.playerCombatForm.getManaPoints() * 0.8f, 15);
        batch.setColor(Color.WHITE);

        instances.theadDarkusHpText.draw(batch, GameConstants.SCREEN_WIDTH * 0.035f, GameConstants.SCREEN_HEIGHT * 0.9f);
        instances.playerCombatFormHpText.draw(batch, GameConstants.SCREEN_WIDTH * 0.82f, GameConstants.SCREEN_HEIGHT * 0.83f);
        instances.playerCombatFormManaText.draw(batch, GameConstants.SCREEN_WIDTH * 0.82f, GameConstants.SCREEN_HEIGHT * 0.92f);

        instances.azuringButton.draw(batch);
        instances.vortexButton.draw(batch);
        instances.nebulaButton.draw(batch);
        instances.razorButton.draw(batch);

        if (usedFuryOfTheEye && !instances.furyOfTheEye.getIsOver()) {
            batch.draw(instances.furyOfTheEye.getImage(), instances.furyOfTheEye.getPosition().x, instances.furyOfTheEye.getPosition().y);
        }

        if (playerLastAtk != null && !playerLastAtk.getIsOver()) {
            batch.draw(playerLastAtk.getImage(), playerLastAtk.getPosition().x, playerLastAtk.getPosition().y);
        }

        if (turnoAtual == Turn.ENEMY && elapsedSinceLastClick >= WAIT_TIME / 2f && elapsedSinceLastClick <= WAIT_TIME
                && enemyLastAtk != null && !enemyLastAtk.getIsOver()) {
            batch.draw(enemyLastAtk.getImage(), enemyLastAtk.getPosition().x, enemyLastAtk.getPosition().y);
        }
    }
}
