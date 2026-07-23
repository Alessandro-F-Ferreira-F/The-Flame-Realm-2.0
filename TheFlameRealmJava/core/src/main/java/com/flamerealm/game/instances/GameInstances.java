package com.flamerealm.game.instances;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.Assets;
import com.flamerealm.game.animation.AnimState;
import com.flamerealm.game.animation.AnimationSpec;
import com.flamerealm.game.assets.EncounterManifest;
import com.flamerealm.game.assets.SpecEncounterManifest;
import com.flamerealm.game.attacks.Attack;
import com.flamerealm.game.attacks.PlayerAttack;
import com.flamerealm.game.characters.Bestiary;
import com.flamerealm.game.characters.CharacterEntity;
import com.flamerealm.game.characters.CombatBodySpec;
import com.flamerealm.game.characters.PlayerCombatForm;
import com.flamerealm.game.gamemap.Direction;
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
import java.util.Map;

import static com.flamerealm.game.GameConstants.*;

/**
 * Port de Instances/*.py: monta todos os objetos do jogo, na mesma ordem de
 * dependencia dos imports originais (Texto -&gt; Botoes -&gt; Ataques -&gt; Personagens -&gt; Mapa).
 * A superficie plana de campos foi quebrada em holders coesos por dominio
 * (ver {@link MenuInstances} etc.); a ordem e o conteudo da construcao nao mudam.
 */
public class GameInstances {

    public final MenuInstances menu;
    public final NavInstances nav;
    public final MovementInstances movement;
    public final StoryInstances story;
    public final CombatInstances combat;
    public final MapInstances map;
    public final OverlayInstances overlays;

    public GameInstances(Assets assets) {
        // 1 - Text
        GameText playButtonText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "Play");
        GameText historyButtonText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "History");
        GameText tutorialButtonText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "Tutorial");
        GameText quitButtonText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "Quit");

        GameText returnToMainMenuText = UiFactory.text(assets, returnButtonTextFont, returnButtonTextSize, black, "Return to main menu");
        GameText returnToGameText = UiFactory.text(assets, returnButtonTextFont, returnButtonTextSize, black, "Return to play");

        String historyTextMessage = "Efil Lightam, a powerful wizard, arrives in his village and is confronted with a scene taken"
                + "over by flames and destruction. "
                + "Thead Darkus, an ancient mage and dark sorcerer master, leaded the attack, "
                + "destroying everything, and everyone. Now, Lightam, using his combat form, must defeat Darkus before"
                + "he expands his realm all over the world. But Lightam needs to be careful, for Darkus "
                + "likes to play dice with his enemy's lives.";
        GameText historyText = UiFactory.text(assets, historyScreenFont, historyScreenTextSize, white, historyTextMessage);

        String historySecretMessage = "The true fire lies in the eye of the tower Seize it and behold the true power";
        GameText historySecretText = UiFactory.text(assets, historyScreenFont, historyScreenTextSize, red, historySecretMessage);

        String tutorialMainMessage = "You must defeat Thead Darkus four times in a row to finish the game";
        GameText tutorialMainText = UiFactory.text(assets, tutorialScreenFont, tutorialScreenTextSize, cian, tutorialMainMessage);

        String tutorialManaMessage = "Pain attention to your mana Points or else you will be defeated";
        GameText tutorialManaText = UiFactory.text(assets, tutorialScreenFont, tutorialScreenTextSize, white, tutorialManaMessage);

        String tutorialMoveMessage = "To move your character, click on the buttons. Depending on your current point, some moves "
                + "will be possible, some won't";
        GameText tutorialMoveText = UiFactory.text(assets, tutorialScreenFont, tutorialScreenTextSize, white, tutorialMoveMessage);

        GameText vortexText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white,
                "Dual Vortex\nDamage/Uses: " + vortexDamage + "/" + vortexMana);
        GameText azuringText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white,
                "Azuring Impact\nDamage/Uses: " + azuringDamage + "/" + azuringMana);
        GameText nebulaText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white,
                "Nebula Crystal\nDamage/Uses: " + nebulaDamage + "/" + nebulaMana);
        GameText razorText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white,
                "Razor Shock\nDamage/Uses: " + razorDamage + "/" + razorMana);

        GameText playerCombatFormHpText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white, "HP: " + playerHp);
        GameText playerCombatFormManaText = UiFactory.text(assets, combatScreenFont, combatScreenTextSize, white, "Mana: " + playerMana);

        GameText pauseText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "Game paused");
        GameText deadText = UiFactory.text(assets, mainMenuTextFont, mainMenuTextSize, white, "YOU DIED!!");

        // 2 - Buttons
        TextButton playButton = UiFactory.textButton(assets, yellow, playButtonPosition, mainMenuButtonsSize, playButtonText,
                mainMenuTextFont, mainMenuTextSize,
                new Vector2(playButtonPosition.x + mainMenuTextSize * 1.2f, playButtonPosition.y));

        TextButton historyButton = UiFactory.textButton(assets, orange, historyButtonPosition, mainMenuButtonsSize, historyButtonText,
                mainMenuTextFont, mainMenuTextSize,
                new Vector2(historyButtonPosition.x + mainMenuTextSize * 1.2f, historyButtonPosition.y));

        TextButton tutorialButton = UiFactory.textButton(assets, darkOrange, tutorialButtonPosition, mainMenuButtonsSize, tutorialButtonText,
                mainMenuTextFont, mainMenuTextSize,
                new Vector2(tutorialButtonPosition.x + mainMenuTextSize * 1.2f, tutorialButtonPosition.y));

        TextButton quitButton = UiFactory.textButton(assets, red, quitButtonPosition, mainMenuButtonsSize, quitButtonText,
                mainMenuTextFont, mainMenuTextSize,
                new Vector2(quitButtonPosition.x + mainMenuTextSize * 1.2f, quitButtonPosition.y));

        TextButton returnToMainMenuButton = UiFactory.textButton(assets, green, returnToMainMenuButtonPosition, returnToMainMenuButtonSize,
                returnToMainMenuText, returnButtonTextFont, returnButtonTextSize,
                new Vector2(returnToMainMenuButtonPosition.x + returnButtonTextSize * 0.1f, returnToMainMenuButtonPosition.y));

        TextButton returnToGameButton = UiFactory.textButton(assets, cian, returnToPlayButtonPosition, returnToPlayButtonSize,
                returnToGameText, returnButtonTextFont, returnButtonTextSize,
                new Vector2(returnToPlayButtonPosition.x + returnButtonTextSize * 0.1f, returnToPlayButtonPosition.y));

        returnToMainMenuButton.setAlign(TextAlign.CENTER);
        returnToGameButton.setAlign(TextAlign.CENTER);

        SpriteButton moveUpButton = UiFactory.spriteButton(assets,
                "Images/Interface/Buttons/MovementButtons/MoveUpButton.png",
                "Images/Interface/Buttons/MovementButtons/WhiteMoveUpButton.png",
                moveButtonUpPosition, moveButtonVSize);

        SpriteButton moveDownButton = UiFactory.spriteButton(assets,
                "Images/Interface/Buttons/MovementButtons/MoveDownButton.png",
                "Images/Interface/Buttons/MovementButtons/WhiteMoveDownButton.png",
                moveButtonDownPosition, moveButtonVSize);

        SpriteButton moveRightButton = UiFactory.spriteButton(assets,
                "Images/Interface/Buttons/MovementButtons/MoveRightButton.png",
                "Images/Interface/Buttons/MovementButtons/WhiteMoveRightButton.png",
                moveButtonRightPosition, moveButtonHSize);

        SpriteButton moveLeftButton = UiFactory.spriteButton(assets,
                "Images/Interface/Buttons/MovementButtons/MoveLeftButton.png",
                "Images/Interface/Buttons/MovementButtons/WhiteMoveLeftButton.png",
                moveButtonLeftPosition, moveButtonHSize);

        SpriteButton pauseButton = UiFactory.spriteButton(assets,
                "Images/Interface/Buttons/Pause Button/PauseButton.png",
                "Images/Interface/Buttons/Pause Button/WhitePauseButton.png",
                pauseButtonPosition, pauseButtonSize);

        TextButton vortexButton = UiFactory.textButton(assets, cian, vortexButtonPosition, combatButtonsSize, vortexText,
                combatScreenFont, combatScreenTextSize,
                new Vector2(vortexButtonPosition.x + combatButtonsSize.x * 1.2f, vortexButtonPosition.y));

        TextButton azuringButton = UiFactory.textButton(assets, cian, azuringButtonPosition, combatButtonsSize, azuringText,
                combatScreenFont, combatScreenTextSize,
                new Vector2(azuringButtonPosition.x + combatButtonsSize.x * 1.2f, azuringButtonPosition.y));

        TextButton nebulaButton = UiFactory.textButton(assets, cian, nebulaButtonPosition, combatButtonsSize, nebulaText,
                combatScreenFont, combatScreenTextSize,
                new Vector2(nebulaButtonPosition.x + combatButtonsSize.x * 1.2f, nebulaButtonPosition.y));

        TextButton razorButton = UiFactory.textButton(assets, cian, razorButtonPosition, combatButtonsSize, razorText,
                combatScreenFont, combatScreenTextSize,
                new Vector2(razorButtonPosition.x + combatButtonsSize.x * 1.2f, razorButtonPosition.y));

        TextButton secretButton = UiFactory.textButton(assets, white, secretButtonPosition, secretButtonSize, historySecretText,
                historyScreenFont, historyScreenTextSize,
                new Vector2(secretTextPosition.x * 1.2f, secretTextPosition.y * 0.985f));

        RectButton secretEyeButton = UiFactory.rectButton(assets, black, secretEyePosition, secretEyeSize);

        // 3 - Attacks
        Texture azuringSprite = assets.texture("Images/Attacks/Player/Azuring Impact.png");
        PlayerAttack azuring = new PlayerAttack("Azuring Impact", azuringDamage, azuringSprite, azuringLoops, azuringFrames,
                azuringPixels, azuringPosition, azuringOffset, azuringMana, azuringButton);

        Texture nebulaSprite = assets.texture("Images/Attacks/Player/Anima.png");
        PlayerAttack nebula = new PlayerAttack("Nebula Crystal", nebulaDamage, nebulaSprite, nebulaLoops, nebulaFrames,
                nebulaPixels, nebulaPosition, nebulaOffset, nebulaMana, nebulaButton);

        Texture vortexSprite = assets.texture("Images/Attacks/Player/Dual Vortex.png");
        PlayerAttack vortex = new PlayerAttack("Dual Vortex", vortexDamage, vortexSprite, vortexLoops, vortexFrames,
                vortexPixels, vortexPosition, vortexOffset, vortexMana, vortexButton);

        Texture razorSprite = assets.texture("Images/Attacks/Player/Razor.png");
        PlayerAttack razor = new PlayerAttack("Razor Shock", razorDamage, razorSprite, razorLoops, razorFrames,
                razorPixels, razorPosition, razorOffset, razorMana, razorButton);

        List<PlayerAttack> playerAtkList = Arrays.asList(azuring, nebula, vortex, razor);

        Texture furyOfTheEyeImage = assets.texture("Images/PuzzleElements/Fury of the eye.png");
        Attack furyOfTheEye = new Attack("Fury of the eye", furyOfTheEyeDamage, furyOfTheEyeImage, furyOfTheEyeLoops,
                furyOfTheEueFrames, furyOfTheEyePixels, furyOfTheEyePosition, furyOfTheEyeOffset);

        // 4 - Characters
        Texture playerSpriteUp = assets.texture("Images/Characters/Player/main character (walking up).png");
        Texture playerSpriteDown = assets.texture("Images/Characters/Player/main character (walking down).png");
        Texture playerSpriteRight = assets.texture("Images/Characters/Player/main character (walking right).png");
        Texture playerSpriteLeft = assets.texture("Images/Characters/Player/main character (walking left).png");

        CharacterEntity playerObject = new CharacterEntity(playerSpriteUp, playerMoveFrames, playerSize, playerPosition, playerOffset);

        // Player usando o BlueMageMoveset (recolor do Necromancer sheet) com moveset completo.
        // Sheet padronizada em celulas 300x300 / 4 fileiras (IDLE/ATTACK/HURT/DEATH),
        // igual as variantes White/Purple/Thead/Necromancer.
        String playerSheet = "Images/Characters/Player/BlueMageMoveset.png";
        GridPoint2 playerCell = new GridPoint2(300, 300);
        CombatBodySpec playerBody = new CombatBodySpec(combatFormPosition, Map.of(
                AnimState.IDLE, new AnimationSpec(playerSheet, 8, playerCell, 0.12f, new GridPoint2(0, 0)),
                AnimState.ATTACK, new AnimationSpec(playerSheet, 13, playerCell, 0.2f, new GridPoint2(0, 300)),
                AnimState.HURT, new AnimationSpec(playerSheet, 5, playerCell, 0.2f, new GridPoint2(0, 600)),
                AnimState.DEATH, new AnimationSpec(playerSheet, 9, playerCell, 0.175f, new GridPoint2(0, 900))));
        PlayerCombatForm playerCombatForm = new PlayerCombatForm(playerBody.position(), playerBody.buildClips(assets),
                playerHp, playerAtkList, playerMana);

        // 5 - Game map
        Texture gameMapImage = assets.texture("Images/Gamemaps/GameMap.png");

        EncounterManifest firePhantonManifest = new SpecEncounterManifest(Bestiary.FIRE_PHANTON);
        EncounterManifest necromancerManifest = new SpecEncounterManifest(Bestiary.NECROMANCER);
        EncounterManifest icePhantonManifest = new SpecEncounterManifest(Bestiary.ICE_PHANTON);
        EncounterManifest darkPhantonManifest = new SpecEncounterManifest(Bestiary.DARK_PHANTON);

        FightPoint startPoint = new FightPoint(new Vector2(downPoint), Arrays.asList(Direction.UP), false, null);
        FightPoint centralFightPoint = new FightPoint(new Vector2(centralPoint),
                Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT),
                true, necromancerManifest);
        FightPoint rightFightPoint = new FightPoint(new Vector2(rightPoint), Arrays.asList(Direction.LEFT), true, darkPhantonManifest);
        FightPoint leftFightPoint = new FightPoint(new Vector2(leftPoint), Arrays.asList(Direction.RIGHT), true, firePhantonManifest);
        FightPoint highFightPoint = new FightPoint(new Vector2(highPoint), Arrays.asList(Direction.DOWN), true, icePhantonManifest);

        List<FightPoint> gameMapPoints = Arrays.asList(startPoint, centralFightPoint, rightFightPoint, leftFightPoint, highFightPoint);
        GameMap gameMap = new GameMap(gameMapImage, gameMapPoints, tolerance);

        // 6 - Other screen images
        TextureRegion mainMenuScreen = assets.flippedRegion("Images/Interface/Backgrounds/Boss Gate mockup.png");
        TextureRegion battleScreen = assets.flippedRegion("Images/Interface/Backgrounds/Battle Screen.png");

        // 7 - Holders (agrupamento por dominio; construcao acima preserva a ordem original)
        menu = new MenuInstances(playButton, historyButton, tutorialButton, quitButton,
                playButtonText, historyButtonText, tutorialButtonText, quitButtonText, mainMenuScreen);

        nav = new NavInstances(returnToMainMenuButton, returnToGameButton, pauseButton,
                returnToMainMenuText, returnToGameText);

        movement = new MovementInstances(moveUpButton, moveDownButton, moveRightButton, moveLeftButton);

        story = new StoryInstances(historyText, historySecretText, secretButton,
                tutorialMainText, tutorialManaText, tutorialMoveText);

        combat = new CombatInstances(playerCombatForm, playerCombatFormHpText, playerCombatFormManaText,
                vortexButton, azuringButton, nebulaButton, razorButton,
                vortexText, azuringText, nebulaText, razorText,
                secretEyeButton, azuring, nebula, vortex, razor, playerAtkList, furyOfTheEye, battleScreen);

        map = new MapInstances(gameMap, gameMapImage, startPoint, centralFightPoint, rightFightPoint, leftFightPoint, highFightPoint,
                playerObject, playerSpriteUp, playerSpriteDown, playerSpriteRight, playerSpriteLeft);

        overlays = new OverlayInstances(pauseText, deadText);
    }
}
