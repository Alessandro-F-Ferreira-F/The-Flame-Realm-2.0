package com.flamerealm.game.instances;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.Assets;
import com.flamerealm.game.attacks.Attack;
import com.flamerealm.game.attacks.PlayerAttack;
import com.flamerealm.game.characters.CharacterEntity;
import com.flamerealm.game.characters.PlayerCombatForm;
import com.flamerealm.game.gamemap.FightPoint;
import com.flamerealm.game.gamemap.GameMap;
import com.flamerealm.game.ui.GameText;
import com.flamerealm.game.ui.RectButton;
import com.flamerealm.game.ui.SpriteButton;
import com.flamerealm.game.ui.TextAlign;
import com.flamerealm.game.ui.TextButton;
import com.flamerealm.game.ui.UiFactory;

import java.util.Arrays;
import java.util.List;

import static com.flamerealm.game.GameConstants.*;

/**
 * Port de Instances/*.py: monta todos os objetos do jogo, na mesma ordem de
 * dependencia dos imports originais (Texto -&gt; Botoes -&gt; Ataques -&gt; Personagens -&gt; Mapa).
 */
public class GameInstances {

    // ===== 1 - Text (port de TextInstances.py) =====
    public final GameText playButtonText;
    public final GameText historyButtonText;
    public final GameText tutorialButtonText;
    public final GameText quitButtonText;

    public final GameText returnToMainMenuText;
    public final GameText returnToGameText;

    public final GameText historyText;
    public final GameText historySecretText;

    public final GameText tutorialMainText;
    public final GameText tutorialManaText;
    public final GameText tutorialMoveText;

    public final GameText vortexText;
    public final GameText azuringText;
    public final GameText nebulaText;
    public final GameText razorText;

    public final GameText playerCombatFormHpText;
    public final GameText playerCombatFormManaText;

    public final GameText pauseText;
    public final GameText deadText;

    // ===== 2 - Buttons (port de ButtonsInstances.py) =====
    public final TextButton playButton;
    public final TextButton historyButton;
    public final TextButton tutorialButton;
    public final TextButton quitButton;

    public final TextButton returnToMainMenuButton;
    public final TextButton returnToGameButton;

    public final SpriteButton moveUpButton;
    public final SpriteButton moveDownButton;
    public final SpriteButton moveRightButton;
    public final SpriteButton moveLeftButton;

    public final SpriteButton pauseButton;

    public final TextButton vortexButton;
    public final TextButton azuringButton;
    public final TextButton nebulaButton;
    public final TextButton razorButton;

    public final TextButton secretButton;
    public final RectButton secretEyeButton;

    // ===== 3 - Attacks (port de AttacksInstances.py) =====
    public final PlayerAttack azuring;
    public final PlayerAttack nebula;
    public final PlayerAttack vortex;
    public final PlayerAttack razor;
    public final List<PlayerAttack> playerAtkList;

    public final Attack furyOfTheEye;

    // ===== 4 - Characters (port de CharactersInstances.py) =====
    public final Texture playerSpriteUp;
    public final Texture playerSpriteDown;
    public final Texture playerSpriteRight;
    public final Texture playerSpriteLeft;
    public final CharacterEntity playerObject;

    public final PlayerCombatForm playerCombatForm;

    // ===== 5 - Game map (port de GameMapInstances.py) =====
    public final Texture gameMapImage;
    public final FightPoint startPoint;
    public final FightPoint centralFightPoint;
    public final FightPoint rightFightPoint;
    public final FightPoint leftFightPoint;
    public final FightPoint highFightPoint;
    public final GameMap gameMap;

    // ===== 6 - Other screen images (port do topo de main.py) =====
    public final TextureRegion mainMenuScreen;
    public final TextureRegion battleScreen;

    public GameInstances(Assets assets) {
        // 1 - Text
        playButtonText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "Play");
        historyButtonText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "History");
        tutorialButtonText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "Tutorial");
        quitButtonText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "Quit");

        returnToMainMenuText = UiFactory.text(assets, returnButtonTextFont, returnButtonTextSize, black, "Return to main menu");
        returnToGameText = UiFactory.text(assets, returnButtonTextFont, returnButtonTextSize, black, "Return to play");

        String historyTextMessage = "Efil Lightam, a powerful wizard, arrives in his village and is confronted with a scene taken"
                + "over by flames and destruction. "
                + "Thead Darkus, an ancient mage and dark sorcerer master, leaded the attack, "
                + "destroying everything, and everyone. Now, Lightam, using his combat form, must defeat Darkus before"
                + "he expands his realm all over the world. But Lightam needs to be careful, for Darkus "
                + "likes to play dice with his enemy's lives.";
        historyText = UiFactory.text(assets, historyScreenFont, historyScreenTextSize, white, historyTextMessage);

        String historySecretMessage = "The true fire lies in the eye of the tower Seize it and behold the true power";
        historySecretText = UiFactory.text(assets, historyScreenFont, historyScreenTextSize, red, historySecretMessage);

        String tutorialMainMessage = "You must defeat Thead Darkus four times in a row to finish the game";
        tutorialMainText = UiFactory.text(assets, tutorialScreenFont, tutorialScreenTextSize, cian, tutorialMainMessage);

        String tutorialManaMessage = "Pain attention to your mana Points or else you will be defeated";
        tutorialManaText = UiFactory.text(assets, tutorialScreenFont, tutorialScreenTextSize, white, tutorialManaMessage);

        String tutorialMoveMessage = "To move your character, click on the buttons. Depending on your current point, some moves "
                + "will be possible, some won't";
        tutorialMoveText = UiFactory.text(assets, tutorialScreenFont, tutorialScreenTextSize, white, tutorialMoveMessage);

        vortexText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white,
                "Dual Vortex\nDamage/Uses: " + vortexDamage + "/" + vortexMana);
        azuringText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white,
                "Azuring Impact\nDamage/Uses: " + azuringDamage + "/" + azuringMana);
        nebulaText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white,
                "Nebula Crystal\nDamage/Uses: " + nebulaDamage + "/" + nebulaMana);
        razorText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white,
                "Razor Shock\nDamage/Uses: " + razorDamage + "/" + razorMana);

        playerCombatFormHpText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white, "HP: " + playerHp);
        playerCombatFormManaText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white, "Mana: " + playerMana);

        pauseText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "Game paused");
        deadText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "YOU DIED!!");

        // 2 - Buttons
        playButton = UiFactory.textButton(assets, yellow, playButtonPosition, mainMenuButtonsSize, playButtonText,
                mainMenuTextFont, mainMenuTextSize,
                new Vector2(playButtonPosition.x + mainMenuTextSize * 1.2f, playButtonPosition.y));

        historyButton = UiFactory.textButton(assets, orange, historyButtonPosition, mainMenuButtonsSize, historyButtonText,
                mainMenuTextFont, mainMenuTextSize,
                new Vector2(historyButtonPosition.x + mainMenuTextSize * 1.2f, historyButtonPosition.y));

        tutorialButton = UiFactory.textButton(assets, darkOrange, tutorialButtonPosition, mainMenuButtonsSize, tutorialButtonText,
                mainMenuTextFont, mainMenuTextSize,
                new Vector2(tutorialButtonPosition.x + mainMenuTextSize * 1.2f, tutorialButtonPosition.y));

        quitButton = UiFactory.textButton(assets, red, quitButtonPosition, mainMenuButtonsSize, quitButtonText,
                mainMenuTextFont, mainMenuTextSize,
                new Vector2(quitButtonPosition.x + mainMenuTextSize * 1.2f, quitButtonPosition.y));

        returnToMainMenuButton = UiFactory.textButton(assets, green, returnToMainMenuButtonPosition, returnToMainMenuButtonSize,
                returnToMainMenuText, returnButtonTextFont, returnButtonTextSize,
                new Vector2(returnToMainMenuButtonPosition.x + returnButtonTextSize * 0.1f, returnToMainMenuButtonPosition.y));

        returnToGameButton = UiFactory.textButton(assets, cian, returnToPlayButtonPosition, returnToPlayButtonSize,
                returnToGameText, returnButtonTextFont, returnButtonTextSize,
                new Vector2(returnToPlayButtonPosition.x + returnButtonTextSize * 0.1f, returnToPlayButtonPosition.y));

        returnToMainMenuButton.setAlign(TextAlign.CENTER);
        returnToGameButton.setAlign(TextAlign.CENTER);

        moveUpButton = UiFactory.spriteButton(assets,
                "Images/Interface/Buttons/MovementButtons/MoveUpButton.png",
                "Images/Interface/Buttons/MovementButtons/WhiteMoveUpButton.png",
                moveButtonUpPosition, moveButtonVSize);

        moveDownButton = UiFactory.spriteButton(assets,
                "Images/Interface/Buttons/MovementButtons/MoveDownButton.png",
                "Images/Interface/Buttons/MovementButtons/WhiteMoveDownButton.png",
                moveButtonDownPosition, moveButtonVSize);

        moveRightButton = UiFactory.spriteButton(assets,
                "Images/Interface/Buttons/MovementButtons/MoveRightButton.png",
                "Images/Interface/Buttons/MovementButtons/WhiteMoveRightButton.png",
                moveButtonRightPosition, moveButtonHSize);

        moveLeftButton = UiFactory.spriteButton(assets,
                "Images/Interface/Buttons/MovementButtons/MoveLeftButton.png",
                "Images/Interface/Buttons/MovementButtons/WhiteMoveLeftButton.png",
                moveButtonLeftPosition, moveButtonHSize);

        pauseButton = UiFactory.spriteButton(assets,
                "Images/Interface/Buttons/Pause Button/PauseButton.png",
                "Images/Interface/Buttons/Pause Button/WhitePauseButton.png",
                pauseButtonPosition, pauseButtonSize);

        vortexButton = UiFactory.textButton(assets, cian, vortexButtonPosition, combatButtonsSize, vortexText,
                combatScreenFont, combatScreenTextSize,
                new Vector2(vortexButtonPosition.x + combatButtonsSize.x * 1.2f, vortexButtonPosition.y));

        azuringButton = UiFactory.textButton(assets, cian, azuringButtonPosition, combatButtonsSize, azuringText,
                combatScreenFont, combatScreenTextSize,
                new Vector2(azuringButtonPosition.x + combatButtonsSize.x * 1.2f, azuringButtonPosition.y));

        nebulaButton = UiFactory.textButton(assets, cian, nebulaButtonPosition, combatButtonsSize, nebulaText,
                combatScreenFont, combatScreenTextSize,
                new Vector2(nebulaButtonPosition.x + combatButtonsSize.x * 1.2f, nebulaButtonPosition.y));

        razorButton = UiFactory.textButton(assets, cian, razorButtonPosition, combatButtonsSize, razorText,
                combatScreenFont, combatScreenTextSize,
                new Vector2(razorButtonPosition.x + combatButtonsSize.x * 1.2f, razorButtonPosition.y));

        secretButton = UiFactory.textButton(assets, white, secretButtonPosition, secretButtonSize, historySecretText,
                historyScreenFont, historyScreenTextSize,
                new Vector2(secretTextPosition.x * 1.2f, secretTextPosition.y * 0.985f));

        secretEyeButton = UiFactory.rectButton(assets, black, secretEyePosition, secretEyeSize);

        // 3 - Attacks
        Texture azuringSprite = assets.texture("Images/Attacks/Player/Azuring Impact.png");
        azuring = new PlayerAttack("Azuring Impact", azuringDamage, azuringSprite, azuringLoops, azuringFrames,
                azuringPixels, azuringPosition, azuringOffset, azuringMana, azuringButton);

        Texture nebulaSprite = assets.texture("Images/Attacks/Player/Nebula.png");
        nebula = new PlayerAttack("Nebula Crystal", nebulaDamage, nebulaSprite, nebulaLoops, nebulaFrames,
                nebulaPixels, nebulaPosition, nebulaOffset, nebulaMana, nebulaButton);

        Texture vortexSprite = assets.texture("Images/Attacks/Player/Dual Vortex.png");
        vortex = new PlayerAttack("Dual Vortex", vortexDamage, vortexSprite, vortexLoops, vortexFrames,
                vortexPixels, vortexPosition, vortexOffset, vortexMana, vortexButton);

        Texture razorSprite = assets.texture("Images/Attacks/Player/Razor.png");
        razor = new PlayerAttack("Razor Shock", razorDamage, razorSprite, razorLoops, razorFrames,
                razorPixels, razorPosition, razorOffset, razorMana, razorButton);

        playerAtkList = Arrays.asList(azuring, nebula, vortex, razor);

        Texture furyOfTheEyeImage = assets.texture("Images/PuzzleElements/Fury of the eye.png");
        furyOfTheEye = new Attack("Fury of the eye", furyOfTheEyeDamage, furyOfTheEyeImage, furyOfTheEyeLoops,
                furyOfTheEueFrames, furyOfTheEyePixels, furyOfTheEyePosition, furyOfTheEyeOffset);

        // 4 - Characters
        playerSpriteUp = assets.texture("Images/Characters/Player/main character (walking up).png");
        playerSpriteDown = assets.texture("Images/Characters/Player/main character (walking down).png");
        playerSpriteRight = assets.texture("Images/Characters/Player/main character (walking right).png");
        playerSpriteLeft = assets.texture("Images/Characters/Player/main character (walking left).png");

        playerObject = new CharacterEntity(playerSpriteUp, playerMoveFrames, playerSize, playerPosition, playerOffset);

        Texture combatFormImage = assets.texture("Images/Characters/Player/BlueMageGuardian.png");
        playerCombatForm = new PlayerCombatForm(combatFormImage, combatFormFrames, combatFormSize, combatFormPosition,
                combatFormOffset, playerHp, playerAtkList, playerMana);

        // 5 - Game map
        gameMapImage = assets.texture("Images/Gamemaps/GameMap.png");

        startPoint = new FightPoint(new Vector2(downPoint), Arrays.asList("UP"), false);
        centralFightPoint = new FightPoint(new Vector2(centralPoint), Arrays.asList("UP", "DOWN", "LEFT", "RIGHT"));
        rightFightPoint = new FightPoint(new Vector2(rightPoint), Arrays.asList("LEFT"));
        leftFightPoint = new FightPoint(new Vector2(leftPoint), Arrays.asList("RIGHT"));
        highFightPoint = new FightPoint(new Vector2(highPoint), Arrays.asList("DOWN"));

        List<FightPoint> gameMapPoints = Arrays.asList(startPoint, centralFightPoint, rightFightPoint, leftFightPoint, highFightPoint);
        gameMap = new GameMap(gameMapImage, gameMapPoints, tolerance);

        // 6 - Other screen images
        mainMenuScreen = assets.flippedRegion("Images/Interface/Backgrounds/Boss Gate mockup.png");
        battleScreen = assets.flippedRegion("Images/Interface/Backgrounds/Battle Screen.png");
    }
}
