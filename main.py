import sys

import parameters as param
import pygame as pg
import Validation as valid
import time

from Screen import screen
from Instances import (ButtonsInstances as buttonsInst,
                       CharactersInstances as charsInst,
                       GameMapInstances as gmpInst, TextInstances as txtInst)
from Instances.AttacksInstances import furyOfTheEye

# 1 - Other screen Images
mainMenuScreen = pg.image.load("Images/Interface/Backgrounds/Boss Gate mockup.png").convert()
battleScreen = pg.image.load("Images/Interface/Backgrounds/Battle Screen.png").convert()

# 2 - Valdation instance
booleans = valid.Validation()

# 3 - Control variables
mapMovespeed = param.mapSpeed
currentMapoffset = mapMovespeed
playerLastAtk = None
enemyLastAtk = None
turnoAtual = "jogador"
waitTime = 11 # Tempo total entre um clique num ataque e a volta do turno para o jogador (em segundos)
lastClick = 0

clock = pg.time.Clock()

running = True
while running:
    clock.tick(60)
    screen.fill(param.black)

    for event in pg.event.get():
        if event.type == pg.QUIT:
            running = False
        if event.type == pg.MOUSEBUTTONDOWN:
            if booleans.getMainMenuScreen():

                if buttonsInst.quitButton.mouseInButton():
                    running = False

                elif buttonsInst.playButton.mouseInButton():
                    booleans.changeToPlayScreen()

                elif buttonsInst.historyButton.mouseInButton():
                    booleans.changeToStoryScreen()

                elif buttonsInst.tutorialButton.mouseInButton():
                    booleans.changeToTutorialScreen()

            elif booleans.getPlayScreen():
                if buttonsInst.returnToMainMenuButton.mouseInButton():
                    booleans.changeToMainMenuScreen()

                if buttonsInst.pauseButton.mouseInButton():
                    booleans.changeToPauseScreen()

                if ((buttonsInst.moveUpButton.mouseInButton() or buttonsInst.moveRightButton.mouseInButton()
                     or buttonsInst.moveLeftButton.mouseInButton() or buttonsInst.moveDownButton.mouseInButton()) and
                        not charsInst.playerObject.getMoving()):

                    if ((buttonsInst.moveRightButton.mouseInButton() and booleans.getMoveRight()) or
                            (buttonsInst.moveLeftButton.mouseInButton() and booleans.getMoveLeft())):

                        charsInst.playerObject.setMoving(True)
                        booleans.horizontalMove()

                        if buttonsInst.moveRightButton.mouseInButton():
                            currentMapoffset = mapMovespeed
                            charsInst.playerObject.setSprite(charsInst.playerSpriteRight)
                        else:
                            currentMapoffset = -mapMovespeed
                            charsInst.playerObject.setSprite(charsInst.playerSpriteLeft)

                    elif ((buttonsInst.moveUpButton.mouseInButton() and booleans.getMoveUp()) or
                          (buttonsInst.moveDownButton.mouseInButton() and booleans.getMoveDown())):

                        charsInst.playerObject.setMoving(True)
                        booleans.verticalMove()

                        if buttonsInst.moveUpButton.mouseInButton():
                            currentMapoffset = -mapMovespeed
                            charsInst.playerObject.setSprite(charsInst.playerSpriteUp)
                        else:
                            currentMapoffset = mapMovespeed
                            charsInst.playerObject.setSprite(charsInst.playerSpriteDown)

            elif booleans.getCombatScreen():
                if turnoAtual == "jogador":
                    playerLastAtk = charsInst.playerCombatForm.atkCheck()

                    if playerLastAtk is not None and playerLastAtk.getMana() <= charsInst.playerCombatForm.getManaPoints():
                        turnoAtual = "inimigo"
                        lastClick = time.time()
                        playerLastAtk.setIsOver(False)
                        playerLastAtk.getAtkButton().getText().setMessage(f"{playerLastAtk.getName()}\nDamage/Uses:"
                                                     f" {playerLastAtk.getDamage()}/{playerLastAtk.getMana()}", )
                        charsInst.playerCombatForm.setManaPoints(charsInst.playerCombatForm.getManaPoints() - playerLastAtk.getMana())
                        charsInst.theadDarkus.setHealthPoints(charsInst.theadDarkus.getHealthPoints() - playerLastAtk.getDamage())

                        txtInst.playerCombatFormManaText.setMessage(f"Mana: {charsInst.playerCombatForm.getManaPoints()}")
                        txtInst.theadDarkusHpText.setMessage(f"HP: {charsInst.theadDarkus.getHealthPoints()}")

                    if buttonsInst.secretEyeButton.mouseInButton() and not booleans.getUsedFuryOfTheEye():
                        charsInst.theadDarkus.setHealthPoints(charsInst.theadDarkus.getHealthPoints() - param.furyOfTheEyeDamage)
                        txtInst.theadDarkusHpText.setMessage(f"HP: {charsInst.theadDarkus.getHealthPoints()}")
                        furyOfTheEye.setIsOver(False)
                        booleans.useFuryOfTheEye()

            elif booleans.getHistoryScreen():
                if buttonsInst.returnToMainMenuButton.mouseInButton():
                    booleans.changeToMainMenuScreen()

                if buttonsInst.secretButton.mouseInButton():
                    booleans.swapSecret()

            elif booleans.getTutorialScreen():
                if buttonsInst.returnToMainMenuButton.mouseInButton():
                    booleans.changeToMainMenuScreen()

            elif booleans.getPauseScreen():
                if buttonsInst.returnToMainMenuButton.mouseInButton():
                    booleans.changeToMainMenuScreen()

                if buttonsInst.returnToGameButton.mouseInButton():
                    booleans.changeToPlayScreen()

            elif booleans.getDeathScreen():
                if buttonsInst.returnToMainMenuButton.mouseInButton():
                    booleans.changeToMainMenuScreen()

                if buttonsInst.returnToGameButton.mouseInButton():
                    booleans.changeToPlayScreen()

    if booleans.getMainMenuScreen():

        screen.blit(mainMenuScreen, (0, 0))
        buttonsInst.playButton.draw()
        buttonsInst.historyButton.draw()
        buttonsInst.tutorialButton.draw()
        buttonsInst.quitButton.draw()

    elif booleans.getPlayScreen():

        screen.blit(gmpInst.gameMap.getImage().subsurface(gmpInst.gameMap.getCurrentPoint().getPoint(), param.screenDimensions),
                    (0, 0))

        screen.blit(charsInst.playerObject.getImage(), charsInst.playerObject.getPosition())

        buttonsInst.pauseButton.draw()

        if charsInst.playerObject.getMoving():
            charsInst.playerObject.update()

            if booleans.getVerticalMoving():
                gmpInst.gameMap.getCurrentPoint().setPoint((gmpInst.gameMap.getCurrentPoint().getPoint()[0],
                                                            gmpInst.gameMap.getCurrentPoint().getPoint()[
                                                                1] + currentMapoffset))
            else:
                gmpInst.gameMap.getCurrentPoint().setPoint(
                    (gmpInst.gameMap.getCurrentPoint().getPoint()[0] + currentMapoffset,
                     gmpInst.gameMap.getCurrentPoint().getPoint()[1]))

        else:
            charsInst.playerObject.setImage(charsInst.playerObject.getFrame()[1])
        if gmpInst.gameMap.fightPointTest():
            booleans.disableMoves()
            charsInst.playerObject.setMoving(False)

            if gmpInst.gameMap.getCurrentPoint().getActivated():
                booleans.changeToCombatScreen()
            else:
                gmpInst.gameMap.setPreviousPoint(gmpInst.gameMap.getCurrentPoint())

            if 'UP' in gmpInst.gameMap.getCurrentPoint().getAllowedDirections():
                buttonsInst.moveUpButton.draw()
                booleans.setMoveUp(True)

            if "DOWN" in gmpInst.gameMap.getCurrentPoint().getAllowedDirections():
                buttonsInst.moveDownButton.draw()
                booleans.setMoveDown(True)

            if "RIGHT" in gmpInst.gameMap.getCurrentPoint().getAllowedDirections():
                buttonsInst.moveRightButton.draw()
                booleans.setMoveRight(True)

            if "LEFT" in gmpInst.gameMap.getCurrentPoint().getAllowedDirections():
                buttonsInst.moveLeftButton.draw()
                booleans.setMoveLeft(True)

    elif booleans.getHistoryScreen():
        txtInst.historyText.showText((param.screenDimensions[0] * 0.05, param.screenDimensions[1] * 0.1))

        if buttonsInst.secretButton.mouseInButton():
            buttonsInst.secretButton.draw()

        if booleans.getSecretReveal():
            buttonsInst.secretButton.getText().setFontColor(param.red)
        else:
            buttonsInst.secretButton.getText().setFontColor(param.black)

        buttonsInst.returnToMainMenuButton.draw()

    elif booleans.getTutorialScreen():
        txtInst.tutorialMainText.showText((param.screenDimensions[0] * 0.115, param.screenDimensions[1] * 0.1))
        txtInst.tutorialManaText.showText((param.screenDimensions[0] * 0.2, param.screenDimensions[1] * 0.35))
        txtInst.tutorialMoveText.showText((param.screenDimensions[0] * 0.3, param.screenDimensions[1] * 0.65))

        pg.draw.rect(screen, param.blue, (param.screenDimensions[0] * 0.05, param.screenDimensions[1] * 0.35,
                                          80, 15))

        txtInst.playerCombatFormManaText.showText((param.screenDimensions[0] * 0.05, param.screenDimensions[1] * 0.38))

        buttonsInst.moveUpButton.draw()
        buttonsInst.moveDownButton.draw()
        buttonsInst.moveRightButton.draw()
        buttonsInst.moveLeftButton.draw()
        buttonsInst.returnToMainMenuButton.draw()

    elif booleans.getCombatScreen():
        buttonsInst.secretEyeButton.draw()

        screen.blit(battleScreen, (0, 0))

        screen.blit(charsInst.theadDarkus.getImage(), charsInst.theadDarkus.getPosition())
        screen.blit(charsInst.playerCombatForm.getImage(), charsInst.playerCombatForm.getPosition())


        pg.draw.rect(screen, param.black, (0, param.screenDimensions[1] * 0.75,
                                           param.screenDimensions[0], param.screenDimensions[1] * 0.25))

        pg.draw.rect(screen, param.red, (param.screenDimensions[0] * 0.035, param.screenDimensions[1] * 0.86,
                                         charsInst.theadDarkus.getHealthPoints() * 0.4, 15))
        pg.draw.rect(screen, param.red, (param.screenDimensions[0] * 0.82, param.screenDimensions[1] * 0.8,
                                         charsInst.playerCombatForm.getHealthPoints() * 0.8, 15))

        pg.draw.rect(screen, param.blue, (param.screenDimensions[0] * 0.82, param.screenDimensions[1] * 0.89,
                                         charsInst.playerCombatForm.getManaPoints() * 0.8, 15))

        txtInst.theadDarkusHpText.showText((param.screenDimensions[0] * 0.035, param.screenDimensions[1] * 0.9))
        txtInst.playerCombatFormHpText.showText((param.screenDimensions[0] * 0.82, param.screenDimensions[1] * 0.83))
        txtInst.playerCombatFormManaText.showText((param.screenDimensions[0] * 0.82, param.screenDimensions[1] * 0.92))

        buttonsInst.azuringButton.draw()
        buttonsInst.vortexButton.draw()
        buttonsInst.nebulaButton.draw()
        buttonsInst.razorButton.draw()

        if booleans.getUsedFuryOfTheEye() and not furyOfTheEye.getIsOver():
            screen.blit(furyOfTheEye.getImage(), furyOfTheEye.getPosition())
            furyOfTheEye.update()

        if time.time() - lastClick > waitTime:
            turnoAtual = "jogador"
            enemyLastAtk = None

            if charsInst.playerCombatForm.getHealthPoints() == 0:
                charsInst.playerCombatForm.revive(param.playerHp)
                txtInst.playerCombatFormHpText.setMessage(f"HP: {charsInst.playerCombatForm.getHealthPoints()}")

                charsInst.playerCombatForm.setManaPoints(param.playerMana)
                txtInst.playerCombatFormManaText.setMessage(f"Mana: {charsInst.playerCombatForm.getManaPoints()}")

                charsInst.theadDarkus.revive(param.theadDarkusHp)
                txtInst.theadDarkusHpText.setMessage(f"HP: {param.theadDarkusHp}")

                gmpInst.gameMap.setCurrentPoint(gmpInst.gameMap.getPreviousPoint())
                booleans.changeToDeathScreen()

        if playerLastAtk is not None:
            if not playerLastAtk.getIsOver():
                screen.blit(playerLastAtk.getImage(), playerLastAtk.getPosition())
                playerLastAtk.update()

            else:
                if charsInst.theadDarkus.getHealthPoints() == 0:
                    turnoAtual = "jogador"
                    charsInst.playerCombatForm.revive(param.playerHp)
                    txtInst.playerCombatFormHpText.setMessage(f"HP: {charsInst.playerCombatForm.getHealthPoints()}")

                    charsInst.playerCombatForm.setManaPoints(param.playerMana)
                    txtInst.playerCombatFormManaText.setMessage(f"Mana: {charsInst.playerCombatForm.getManaPoints()}")

                    charsInst.theadDarkus.revive(param.theadDarkusHp)
                    txtInst.theadDarkusHpText.setMessage(f"HP: {param.theadDarkusHp}")

                    gmpInst.gameMap.disableCurrentPoint()

                    gmpInst.gameMap.setPreviousPoint(gmpInst.gameMap.getCurrentPoint())
                    booleans.changeToPlayScreen()

        if turnoAtual == "inimigo" and waitTime/2 <= time.time() - lastClick <= waitTime:

            if enemyLastAtk is None:
                enemyLastAtk = charsInst.theadDarkus.randomizeAtk()
                enemyLastAtk.randomizeHitKill()
                enemyLastAtk.setIsOver(False)

                charsInst.playerCombatForm.setHealthPoints(charsInst.playerCombatForm.getHealthPoints() - enemyLastAtk.getDamage())
                txtInst.playerCombatFormHpText.setMessage(f"HP: {charsInst.playerCombatForm.getHealthPoints()}")

            if not enemyLastAtk.getIsOver():
                screen.blit(enemyLastAtk.getImage(), enemyLastAtk.getPosition())
                enemyLastAtk.update()

        charsInst.theadDarkus.update()
        charsInst.playerCombatForm.update()

    elif booleans.getPauseScreen():
        txtInst.pauseText.showText((param.screenDimensions[0] * 0.4, param.screenDimensions[1] * 0.45))
        buttonsInst.returnToMainMenuButton.draw()
        buttonsInst.returnToGameButton.draw()

    elif booleans.getDeathScreen():
        txtInst.deadText.showText((param.screenDimensions[0] * 0.4, param.screenDimensions[1] * 0.45))
        buttonsInst.returnToMainMenuButton.draw()
        buttonsInst.returnToGameButton.draw()

    pg.display.flip()

pg.quit()
sys.exit()
