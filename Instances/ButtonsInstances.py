from Screen import screen
import parameters as param
import pygame as pg
import Instances.TextInstances as txtInst

from Classes.InterfaceElementes.Buttons.BasicButtton import BasicButton
from Classes.InterfaceElementes.Buttons.ButtonWithText import ButtonWithText
from Classes.InterfaceElementes.Buttons.ButtonWithSprite import ButtonWithSprite

# 1 - Main menu
playButton = ButtonWithText(screen, param.yellow, param.playButtonPosition, param.mainMenuButtonsSize,
                                    txtInst.playButtonText, (param.playButtonPosition[0] + param.mainMenuTextSize * 1.2,
                                     param.playButtonPosition[1]))

historyButton = ButtonWithText(screen, param.orange, param.historyButtonPosition, param.mainMenuButtonsSize,
                                       txtInst.historyButtonText,
                                       (param.historyButtonPosition[0] + param.mainMenuTextSize * 1.2,
                                        param.historyButtonPosition[1]))

tutorialButton = ButtonWithText(screen, param.darkOrange, param.tutorialButtonPosition,
                                        param.mainMenuButtonsSize, txtInst.tutorialButtonText,
                                        (param.tutorialButtonPosition[0] + param.mainMenuTextSize * 1.2,
                                         param.tutorialButtonPosition[1]))

quitButton = ButtonWithText(screen, param.red, param.quitButtonPosition, param.mainMenuButtonsSize,
                                    txtInst.quiButtonText,
                                    (param.quitButtonPosition[0] + param.mainMenuTextSize * 1.2,
                                           param.quitButtonPosition[1]))

# 2 - Return buttons
returnToMainMenuButton = ButtonWithText(screen, param.green, param.returnToMainMenuButtonPosition,
                                                param.returnToMainMenuButtonSize, txtInst.returnToMainMenuText,
                                                (param.returnToMainMenuButtonPosition[0] + param.returnButtonTextSize * 0.1,
                                                 param.returnToMainMenuButtonPosition[1]))

returnToGameButton = ButtonWithText(screen, param.cian, param.returnToPlayButtonPosition, param.returnToPlayButtonSize,
                                            txtInst.returnToGameText,
                                            (param.returnToPlayButtonPosition[0] + param.returnButtonTextSize * 0.1,
                                             param.returnToPlayButtonPosition[1]))

# 3 - Movement buttons
moveUpImage = pg.image.load("Images/Interface/Buttons/MovementButtons/MoveUpButton.png").convert_alpha()
whiteMoveUpImage = pg.image.load("Images/Interface/Buttons/MovementButtons/WhiteMoveUpButton.png").convert_alpha()
moveUpButton = ButtonWithSprite(screen, param.moveButtonUpPosition, param.moveButtonVSize,
                                        moveUpImage, whiteMoveUpImage)

moveDownImage = pg.image.load("Images/Interface/Buttons/MovementButtons/MoveDownButton.png").convert_alpha()
whiteMoveDownImage = pg.image.load(
    "Images/Interface/Buttons/MovementButtons/WhiteMoveDownButton.png").convert_alpha()
moveDownButton = ButtonWithSprite(screen, param.moveButtonDownPosition, param.moveButtonVSize,
                                          moveDownImage, whiteMoveDownImage)

moveRightImage = pg.image.load("Images/Interface/Buttons/MovementButtons/MoveRightButton.png").convert_alpha()
whiteMoveRightImage = pg.image.load(
    "Images/Interface/Buttons/MovementButtons/WhiteMoveRightButton.png").convert_alpha()
moveRightButton = ButtonWithSprite(screen, param.moveButtonRightPosition, param.moveButtonHSize,
                                           moveRightImage, whiteMoveRightImage)

moveLeftImage = pg.image.load("Images/Interface/Buttons/MovementButtons/MoveLeftButton.png").convert_alpha()
whiteMoveLeftImage = pg.image.load(
    "Images/Interface/Buttons/MovementButtons/WhiteMoveLeftButton.png").convert_alpha()
moveLeftButton = ButtonWithSprite(screen, param.moveButtonLeftPosition, param.moveButtonHSize,
                                          moveLeftImage, whiteMoveLeftImage)

# 4 - Pause button
pauseButtonImage = pg.image.load("Images/Interface/Buttons/Pause Button/PauseButton.png").convert_alpha()
whitePauseButtonImage = pg.image.load("Images/Interface/Buttons/Pause Button/WhitePauseButton.png").convert_alpha()
pauseButton = ButtonWithSprite(screen, param.pauseButtonPosition, param.pauseButtonSize, pauseButtonImage,
                                       whitePauseButtonImage)

# 5 - Combat screen buttons
vortexButton = ButtonWithText(screen, param.cian, param.vortexButtonPosition, param.combatButtonsSize,
                                      txtInst.vortexText,
                                      (param.vortexButtonPosition[0] + param.combatButtonsSize[0] * 1.2,
                                             param.vortexButtonPosition[1]))

azuringButton = ButtonWithText(screen, param.cian, param.azuringButtonPosition, param.combatButtonsSize,
                                       txtInst.azuringText,
                                       (param.azuringButtonPosition[0] + param.combatButtonsSize[0] * 1.2,
                                        param.azuringButtonPosition[1]))

nebulaButton = ButtonWithText(screen, param.cian, param.nebulaButtonPosition, param.combatButtonsSize,
                                      txtInst.nebulaText,
                                      (param.nebulaButtonPosition[0] + param.combatButtonsSize[0] * 1.2,
                                        param.nebulaButtonPosition[1]))

razorButton = ButtonWithText(screen, param.cian, param.razorButtonPosition, param.combatButtonsSize,
                                     txtInst.razorText,
                                     (param.razorButtonPosition[0] + param.combatButtonsSize[0] * 1.2,
                                        param.razorButtonPosition[1]))

# 6 - Secret message button
secretButton = ButtonWithText(screen, param.white, param.secretButtonPosition, param.secretButtonSize,
                                      txtInst.historySecretText, (param.secretTextPosition[0] * 1.2,
                                                                  param.secretTextPosition[1] * 0.985))

# 7 - Secret eye button
secretEyeButton = BasicButton(screen, param.black, param.secretEyePosition, param.secretEyeSize)
