import parameters as param
from Screen import screen
import pygame as pg
from Classes.InterfaceElementes.Text import Text

pg.init()

# 1 - Main menu
playButtonText = Text.Text(param.mainMenuTextFont, param.mainMenuTextSize, param.white, False, False,
                           "Play", True, screen)

historyButtonText = Text.Text(param.mainMenuTextFont, param.mainMenuTextSize, param.white, False, False,
                                       "History", True, screen)

tutorialButtonText = Text.Text(param.mainMenuTextFont, param.mainMenuTextSize, param.white, False, False,
                               "Tutorial", True, screen)

quiButtonText = Text.Text(param.mainMenuTextFont, param.mainMenuTextSize, param.white, False, False,
                          "Quit", True, screen)

# 2 - Return buttons
returnToMainMenuText = Text.Text(param.returnButtonTextFont, param.returnButtonTextSize, param.black, False, False,
                       "Return to main menu", True, screen)

returnToGameText = Text.Text(param.returnButtonTextFont, param.returnButtonTextSize, param.black, False, False,
                       "Return to play", True, screen)

# 3 - History Screen
historyTextMessage = ("Efil Lightam, a powerful wizard, arrives in his village and is\nconfronted with a scene taken"
               "over by flames and destruction.\n" 
               "Thead Darkus, an ancient mage and dark sorcerer master, leaded\nthe attack, "
               "destroying everything, and everyone. Now, Lightam,\nusing his combat form, must defeat Darkus before"
               "he expands his\nrealm all over the world. But Lightam needs to be careful, for\nDarkus "
               "likes to play dice with his enemy's lives.")

historyText = Text.Text(param.historyScreenFont, param.historyScreenTextSize, param.white, False, False,
                        historyTextMessage, True, screen)

historySecretMessage = "The true fire lies in the eye of the tower\nSeize it and behold the true power"

historySecretText = Text.Text(param.historyScreenFont, param.historyScreenTextSize, param.red, False,
                              False, historySecretMessage, True, screen)

# 4 - Tutorial screen
tutorialMainMessage = "You must defeat Thead Darkus four times in a row to finish the game"
tutorialMainText = Text.Text(param.tutorialScreenFont, param.tutorialScreenTextSize, param.cian, False,
                             False, tutorialMainMessage, True, screen)

tutorialManaMessage = "Pain attention to your mana Points or else you will be defeated"
tutorialManaText = Text.Text(param.tutorialScreenFont, param.tutorialScreenTextSize, param.white, False,
                             False, tutorialManaMessage, True, screen)

tutorialMoveMessage = ("To move your character, click on the buttons.\nDepending on your current point, some moves\n"
                       "will be possible, some won't")
tutorialMoveText = Text.Text(param.tutorialScreenFont, param.tutorialScreenTextSize, param.white, False,
                             False, tutorialMoveMessage, True, screen)

# 5 - Combat screen
vortexText = Text.Text(param.combatScreenFont, param.combatScreenTextSize, param.white, False, False,
                                      f"Dual Vortex\nDamage/Uses: {param.vortexDamage}/{param.vortexMana}",
                       True, screen)

azuringText = Text.Text(param.combatScreenFont, param.combatScreenTextSize, param.white, False, False,
                        f"Azuring Impact\nDamage/Uses: {param.azuringDamage}/{param.azuringMana}",
                        True, screen)

nebulaText = Text.Text(param.combatScreenFont, param.combatScreenTextSize, param.white, False, False,
                        f"Nebula Crystal\nDamage/Uses: {param.nebulaDamage}/{param.nebulaMana}",
                       True, screen)

razorText = Text.Text(param.combatScreenFont, param.combatScreenTextSize, param.white, False, False,
                        f"Razor Shock\nDamage/Uses: {param.razorDamage}/{param.razorMana}",
                      True, screen)

theadDarkusHpText = Text.Text(param.combatScreenFont, param.combatScreenTextSize, param.white, False, False,
                              f"HP: {param.theadDarkusHp}", False, screen)

playerCombatFormHpText = Text.Text(param.combatScreenFont, param.combatScreenTextSize, param.white, False, False,
                              f"HP: {param.playerHp}", False, screen)

playerCombatFormManaText = Text.Text(param.combatScreenFont, param.combatScreenTextSize, param.white, False, False,
                              f"Mana: {param.playerMana}", False, screen)

# 6 - Pause
pauseText = Text.Text(param.mainMenuTextFont, param.mainMenuTextSize, param.white, False, False,
                      "Game paused", False, screen)

# 7 - Dying screen
deadText = Text.Text(param.mainMenuTextFont, param.mainMenuTextSize, param.white, False, False,
                     "YOU DIED!!", False, screen)
