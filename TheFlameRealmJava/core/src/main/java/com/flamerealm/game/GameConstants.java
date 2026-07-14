package com.flamerealm.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

/**
 * Port literal de parameters.py. Mesmos valores, mesma ordem, mesmos nomes.
 */
public final class GameConstants {
    private GameConstants() {}

    public static final int SCREEN_WIDTH = 900;
    public static final int SCREEN_HEIGHT = 600;

    // 1 - Colors
    public static final Color yellow = new Color(255 / 255f, 255 / 255f, 0 / 255f, 1f);
    public static final Color orange = new Color(255 / 255f, 120 / 255f, 0 / 255f, 1f);
    public static final Color darkOrange = new Color(200 / 255f, 50 / 255f, 0 / 255f, 1f);
    public static final Color red = new Color(180 / 255f, 0 / 255f, 0 / 255f, 1f);
    public static final Color white = new Color(255 / 255f, 255 / 255f, 255 / 255f, 1f);
    public static final Color black = new Color(0 / 255f, 0 / 255f, 0 / 255f, 1f);
    public static final Color green = new Color(0 / 255f, 180 / 255f, 0 / 255f, 1f);
    public static final Color cian = new Color(0 / 255f, 200 / 255f, 255 / 255f, 1f);
    public static final Color blue = new Color(0 / 255f, 0 / 255f, 255 / 255f, 1f);

    // 2 - Button: positions and dimensions

    // 2.1 Dimensions
    public static final GridPoint2 mainMenuButtonsSize = new GridPoint2(50, 50);
    public static final GridPoint2 returnToMainMenuButtonSize = new GridPoint2(150, 20);
    public static final GridPoint2 returnToPlayButtonSize = new GridPoint2(102, 20);

    public static final GridPoint2 moveButtonHSize = new GridPoint2(64, 48);
    public static final GridPoint2 moveButtonVSize = new GridPoint2(48, 64);

    public static final GridPoint2 pauseButtonSize = new GridPoint2(48, 48);

    public static final GridPoint2 combatButtonsSize = new GridPoint2(50, 40);
    public static final GridPoint2 secretEyeSize = new GridPoint2(40, 40);

    public static final GridPoint2 secretButtonSize = new GridPoint2(20, 20);

    // 2.2 Positions

    // 2.2.1 - Main menu
    public static final float mainMenuButtonsX = SCREEN_WIDTH * 0.1f;
    public static final Vector2 playButtonPosition = new Vector2(mainMenuButtonsX, SCREEN_HEIGHT * 0.15f);
    public static final Vector2 historyButtonPosition = new Vector2(mainMenuButtonsX, SCREEN_HEIGHT * 0.35f);
    public static final Vector2 tutorialButtonPosition = new Vector2(mainMenuButtonsX, SCREEN_HEIGHT * 0.55f);
    public static final Vector2 quitButtonPosition = new Vector2(mainMenuButtonsX, SCREEN_HEIGHT * 0.75f);

    // 2.2.2 - Return buttons
    public static final Vector2 returnToMainMenuButtonPosition = new Vector2(SCREEN_WIDTH * 0.02f, SCREEN_HEIGHT * 0.95f);
    public static final Vector2 returnToPlayButtonPosition = new Vector2(SCREEN_WIDTH * 0.865f, SCREEN_HEIGHT * 0.95f);

    // 2.2.3 - Play screen
    public static final Vector2 moveButtonRightPosition = new Vector2(SCREEN_WIDTH * 0.17f, SCREEN_HEIGHT * 0.7f);
    public static final Vector2 moveButtonLeftPosition = new Vector2(SCREEN_WIDTH * 0.02f, SCREEN_HEIGHT * 0.7f);
    public static final Vector2 moveButtonUpPosition = new Vector2(SCREEN_WIDTH * 0.106f, SCREEN_HEIGHT * 0.58f);
    public static final Vector2 moveButtonDownPosition = new Vector2(SCREEN_WIDTH * 0.106f, SCREEN_HEIGHT * 0.79f);

    public static final Vector2 pauseButtonPosition = new Vector2(SCREEN_WIDTH * 0.106f, SCREEN_HEIGHT * 0.7f);

    // 2.2.4 - Secret Button (History screen)
    public static final Vector2 secretButtonPosition = new Vector2(SCREEN_WIDTH * 0.95f, SCREEN_HEIGHT * 0.95f);

    // 2.2.5 - combat screen
    public static final Vector2 vortexButtonPosition = new Vector2(SCREEN_WIDTH * 0.25f, SCREEN_HEIGHT * 0.8f);
    public static final Vector2 azuringButtonPosition = new Vector2(SCREEN_WIDTH * 0.25f, SCREEN_HEIGHT * 0.9f);
    public static final Vector2 nebulaButtonPosition = new Vector2(SCREEN_WIDTH * 0.5f, SCREEN_HEIGHT * 0.9f);
    public static final Vector2 razorButtonPosition = new Vector2(SCREEN_WIDTH * 0.5f, SCREEN_HEIGHT * 0.8f);

    public static final Vector2 secretEyePosition = new Vector2(SCREEN_WIDTH * 0.5125f, SCREEN_HEIGHT * 0.065f);

    // 3 - Text

    // Fontes abertas substituindo as fontes do Windows do jogo original
    // (Gabriola, Times New Roman, Arial) - ver plano de refatoracao.
    public static final String FONT_FANTASY = "fonts/MedievalSharp-Regular.ttf"; // no lugar de Gabriola
    public static final String FONT_SERIF = "fonts/NotoSerif-Regular.ttf";       // no lugar de Times New Roman
    public static final String FONT_SANS = "fonts/Roboto-Regular.ttf";           // no lugar de Arial

    // 3.1 Main menu
    public static final int mainMenuTextSize = 50;
    public static final String mainMenuTextFont = FONT_FANTASY;

    // 3.2 - return button
    public static final String returnButtonTextFont = FONT_SANS;
    public static final int returnButtonTextSize = 15;

    // 3.3 History screen
    public static final String historyScreenFont = FONT_SERIF;
    public static final int historyScreenTextSize = 30;
    public static final Vector2 secretTextPosition = new Vector2(SCREEN_WIDTH * 0.1f, SCREEN_HEIGHT * 0.7f);

    // 3.4 - Tutorial screen
    public static final String tutorialScreenFont = historyScreenFont;
    public static final int tutorialScreenTextSize = 27;

    // 3.5 - combat screen
    public static final String combatScreenFont = historyScreenFont;
    public static final int combatScreenTextSize = 17;

    // 4 - Characters Sprites

    // 4.1 - Dimensions (in pixels)
    public static final GridPoint2 theadDarkusSize = new GridPoint2(224, 240);
    public static final GridPoint2 combatFormSize = new GridPoint2(128, 128);
    public static final GridPoint2 playerSize = new GridPoint2(48, 64);

    // 4.2 - Number of Frames
    public static final int theadDarkusFrames = 15;
    public static final int combatFormFrames = 14;
    public static final int playerMoveFrames = 4;

    // 4.3 Offsets (dictates the animation speed)
    public static final float theadDarkusOffset = 0.18f;
    public static final float combatFormOffset = 0.18f;
    public static final float playerOffset = 0.1f;

    // 4.4 Positions (top-left of the images)
    public static final Vector2 theadDarkusPosition = new Vector2(0, SCREEN_HEIGHT * 0.3f);
    public static final Vector2 combatFormPosition = new Vector2(SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT * 0.5f);
    public static final Vector2 playerPosition = new Vector2(SCREEN_WIDTH / 2f - playerSize.x / 2f, SCREEN_HEIGHT / 2f - playerSize.y / 2f);

    // 5 - Attacks parameters

    // 5.1 - player

    // 5.1.1 - Damage
    public static final int vortexDamage = 200;
    public static final int azuringDamage = 40;
    public static final int nebulaDamage = 95;
    public static final int razorDamage = 65;

    // 5.1.2 - Mana
    public static final int vortexMana = 55;
    public static final int azuringMana = 10;
    public static final int nebulaMana = 20;
    public static final int razorMana = 15;

    // 5.1.3 - Dimensions (in pixels)
    public static final GridPoint2 vortexPixels = new GridPoint2(128, 128);
    public static final GridPoint2 azuringPixels = new GridPoint2(100, 100);
    public static final GridPoint2 nebulaPixels = new GridPoint2(40, 40);
    public static final GridPoint2 razorPixels = new GridPoint2(40, 40);

    // 5.1.4 - Number of frames
    public static final int vortexFrames = 60;
    public static final int azuringFrames = 60;
    public static final int nebulaFrames = 64;
    public static final int razorFrames = 60;

    // 5.1.5 - Positions
    public static final Vector2 vortexPosition = new Vector2(SCREEN_WIDTH * 0.055f, SCREEN_HEIGHT * 0.4f);
    public static final Vector2 azuringPosition = new Vector2(SCREEN_WIDTH * 0.07f, SCREEN_HEIGHT * 0.5f);
    public static final Vector2 nebulaPosition = new Vector2(SCREEN_WIDTH * 0.105f, SCREEN_HEIGHT * 0.5f);
    public static final Vector2 razorPosition = new Vector2(SCREEN_WIDTH * 0.1f, SCREEN_HEIGHT * 0.5f);

    // 5.1.6 - Loops
    public static final int vortexLoops = 2;
    public static final int azuringLoops = 2;
    public static final int nebulaLoops = 1;
    public static final int razorLoops = 3;

    // 5.1.7 - Offsets
    public static final float vortexOffset = 1f;
    public static final float azuringOffset = 1f;
    public static final float nebulaOffset = 0.5f;
    public static final float razorOffset = 1.2f;

    // 5.2 - Boss

    // 5.2.1 - Damage
    public static final int eldenRingDamage = 30;
    public static final int fireSoulDamage = 40;
    public static final int darkVortexDamage = 100;
    public static final int flamelashDamage = 10;

    // 5.2.2 - HitKill Chance
    public static final int eldenRingHitKillChance = 90;
    public static final int fireSoulHitKillChance = 80;
    public static final int darkVortexHitKillChance = 100;
    public static final int flamelashHitKillChance = 70;

    // 5.2.3 - Dimensions (in pixels)
    public static final GridPoint2 eldenRingPixels = new GridPoint2(40, 40);
    public static final GridPoint2 fireSoulPixels = new GridPoint2(50, 50);
    public static final GridPoint2 darkVortexPixels = new GridPoint2(128, 128);
    public static final GridPoint2 flamelashPixels = new GridPoint2(100, 100);

    // 5.2.4 - Number of frames
    public static final int eldenRingFrames = 60;
    public static final int fireSoulFrames = 60;
    public static final int darkVortexFrames = 60;
    public static final int flamelashFrames = 49;

    // 5.2.5 - Positions
    public static final Vector2 eldenRingPosition = new Vector2(SCREEN_WIDTH * 0.848f, SCREEN_HEIGHT * 0.55f);
    public static final Vector2 fireSoulPosition = new Vector2(SCREEN_WIDTH * 0.84f, SCREEN_HEIGHT * 0.55f);
    public static final Vector2 darkVortexPosition = new Vector2(SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT * 0.45f);
    public static final Vector2 flamelashPosition = new Vector2(SCREEN_WIDTH * 0.82f, SCREEN_HEIGHT * 0.5f);

    // 5.2.6 - Loops
    public static final int eldenRingLoops = 4;
    public static final int fireSoulLoops = 3;
    public static final int darkVortexLoops = 1;
    public static final int flamelashLoops = 3;

    // 5.2.7 - Offsets
    public static final float eldenRingOffset = 0.9f;
    public static final float fireSoulOffset = 0.7f;
    public static final float darkVortexOffset = 0.2f;
    public static final float flamelashOffset = 0.6f;

    // 5.3 Fury of the eye
    public static final int furyOfTheEyeDamage = 200;
    public static final GridPoint2 furyOfTheEyePixels = new GridPoint2(64, 64);
    public static final int furyOfTheEueFrames = 12;
    public static final Vector2 furyOfTheEyePosition = new Vector2(SCREEN_WIDTH * 0.09f, SCREEN_HEIGHT * 0.5f);
    public static final int furyOfTheEyeLoops = 8;
    public static final float furyOfTheEyeOffset = 1f;

    // 6 - Map

    // 6.1 - Game points
    public static final Vector2 downPoint = new Vector2(1150, 2450);
    public static final Vector2 centralPoint = new Vector2(1150, 1000);
    public static final Vector2 rightPoint = new Vector2(2175, 1000);
    public static final Vector2 leftPoint = new Vector2(125, 1000);
    public static final Vector2 highPoint = new Vector2(1150, 170);

    // 6.2 - Speed
    public static final float mapSpeed = 6f;
    public static final float tolerance = mapSpeed * 0.5f; // will be used to test which point the player is close to

    // 7 - stats

    // 7.1 - Player
    public static final int playerHp = 100;
    public static final int playerMana = 100;

    public static final int playerHpBarSize = 100;
    public static final int playerManaBarSize = playerHpBarSize;

    // 7.2 - Boss
    public static final int theadDarkusHp = 400;

    public static final int theadDarkusHpBarSize = 200;
}
