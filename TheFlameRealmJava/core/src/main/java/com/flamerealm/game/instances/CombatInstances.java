package com.flamerealm.game.instances;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.flamerealm.game.attacks.Attack;
import com.flamerealm.game.attacks.PlayerAttack;
import com.flamerealm.game.characters.PlayerCombatForm;
import com.flamerealm.game.ui.GameText;
import com.flamerealm.game.ui.RectButton;
import com.flamerealm.game.ui.TextButton;

import java.util.List;

/**
 * Holder do dominio "combate": so agrupa objetos ja construidos por
 * GameInstances, sem logica propria.
 */
public class CombatInstances {
    public final PlayerCombatForm playerCombatForm;
    public final GameText playerCombatFormHpText;
    public final GameText playerCombatFormManaText;

    public final TextButton vortexButton;
    public final TextButton azuringButton;
    public final TextButton nebulaButton;
    public final TextButton razorButton;
    public final GameText vortexText;
    public final GameText azuringText;
    public final GameText nebulaText;
    public final GameText razorText;

    public final RectButton secretEyeButton;

    public final PlayerAttack azuring;
    public final PlayerAttack nebula;
    public final PlayerAttack vortex;
    public final PlayerAttack razor;
    public final List<PlayerAttack> playerAtkList;

    public final Attack furyOfTheEye;

    public final TextureRegion battleScreen;

    public CombatInstances(PlayerCombatForm playerCombatForm, GameText playerCombatFormHpText, GameText playerCombatFormManaText,
            TextButton vortexButton, TextButton azuringButton, TextButton nebulaButton, TextButton razorButton,
            GameText vortexText, GameText azuringText, GameText nebulaText, GameText razorText,
            RectButton secretEyeButton,
            PlayerAttack azuring, PlayerAttack nebula, PlayerAttack vortex, PlayerAttack razor,
            List<PlayerAttack> playerAtkList, Attack furyOfTheEye, TextureRegion battleScreen) {
        this.playerCombatForm = playerCombatForm;
        this.playerCombatFormHpText = playerCombatFormHpText;
        this.playerCombatFormManaText = playerCombatFormManaText;
        this.vortexButton = vortexButton;
        this.azuringButton = azuringButton;
        this.nebulaButton = nebulaButton;
        this.razorButton = razorButton;
        this.vortexText = vortexText;
        this.azuringText = azuringText;
        this.nebulaText = nebulaText;
        this.razorText = razorText;
        this.secretEyeButton = secretEyeButton;
        this.azuring = azuring;
        this.nebula = nebula;
        this.vortex = vortex;
        this.razor = razor;
        this.playerAtkList = playerAtkList;
        this.furyOfTheEye = furyOfTheEye;
        this.battleScreen = battleScreen;
    }
}
