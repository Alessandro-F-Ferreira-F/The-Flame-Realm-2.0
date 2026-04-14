class Validation:
    def __init__(self):
        """
        This class is used to control screen changes and movement changes in the game, based on logical tests
        NOTE: This class was designed to only have boolean values
        """
        self._mainMenuScreen = True
        self._historyScreen = False
        self._tutorialScreen = False
        self._playScreen = False
        self._combatScreen = False
        self._pauseScreen = False
        self._deathScreen = False

        self._verticalMoving = False
        self._horizontalMoving = False

        self._moveUp = False
        self._moveDown = False
        self._moveLeft = False
        self._moveRight = False

        self._secretReveal = False
        self._usedFuryOfTheEye = False

    def disableMoves(self):
        self.setMoveUp(False)
        self.setMoveDown(False)
        self.setMoveLeft(False)
        self.setMoveRight(False)

    def changeToMainMenuScreen(self):
        self.setMainMenuScreen(True)
        self.setHistoryScreen(False)
        self.setTutorialScreen(False)
        self.setPlayScreen(False)
        self.setDeathScreen(False)

    def changeToStoryScreen(self):
        self.hideSecret()
        self.setMainMenuScreen(False)
        self.setHistoryScreen(True)

    def changeToTutorialScreen(self):
        self.setMainMenuScreen(False)
        self.setTutorialScreen(True)

    def changeToPlayScreen(self):
        self.setMainMenuScreen(False)
        self.setPlayScreen(True)
        self.setCombatScreen(False)
        self.setPauseScreen(False)
        self.setDeathScreen(False)

    def changeToCombatScreen(self):
        self.setUsedFuryOfTheEye(False)
        self.setPlayScreen(False)
        self.setCombatScreen(True)

    def changeToPauseScreen(self):
        self.setPlayScreen(False)
        self.setPauseScreen(True)

    def changeToDeathScreen(self):
        self.setCombatScreen(False)
        self.setDeathScreen(True)

    def horizontalMove(self):
        self.setHorizontalMoving(True)
        self.setVerticalMoving(False)

    def verticalMove(self):
        self.setVerticalMoving(True)
        self.setHorizontalMoving(False)

    def revealSecret(self):
        self.setSecretReveal(True)

    def hideSecret(self):
        self.setSecretReveal(False)

    def swapSecret(self):
        if self.getSecretReveal():
            self.hideSecret()
        else:
            self.revealSecret()

    def useFuryOfTheEye(self):
        self.setUsedFuryOfTheEye(True)

    def getMainMenuScreen(self):
        return self._mainMenuScreen

    def setMainMenuScreen(self, boolean):
        self._mainMenuScreen = boolean

    def getHistoryScreen(self):
        return self._historyScreen

    def setHistoryScreen(self, boolean):
        self._historyScreen = boolean

    def getTutorialScreen(self):
        return self._tutorialScreen

    def setTutorialScreen(self, boolean):
        self._tutorialScreen = boolean

    def getPlayScreen(self):
        return self._playScreen

    def setPlayScreen(self, boolean):
        self._playScreen = boolean

    def getVerticalMoving(self):
        return self._verticalMoving

    def setVerticalMoving(self, boolean):
        self._verticalMoving = boolean

    def getHorizontalMoving(self):
        return self._horizontalMoving

    def setHorizontalMoving(self, boolean):
        self._horizontalMoving = boolean

    def getMoveUp(self):
        return self._moveUp

    def setMoveUp(self, boolean):
        self._moveUp = boolean

    def getMoveDown(self):
        return self._moveDown

    def setMoveDown(self, boolean):
        self._moveDown = boolean

    def getMoveLeft(self):
        return self._moveLeft

    def setMoveLeft(self, boolean):
        self._moveLeft = boolean

    def getMoveRight(self):
        return self._moveRight

    def setMoveRight(self, boolean):
        self._moveRight = boolean

    def getCombatScreen(self):
        return self._combatScreen

    def setCombatScreen(self, boolean):
        self._combatScreen = boolean

    def getSecretReveal(self):
        return self._secretReveal

    def setSecretReveal(self, boolean):
        self._secretReveal = boolean

    def getUsedFuryOfTheEye(self):
        return self._usedFuryOfTheEye

    def setUsedFuryOfTheEye(self, boolean):
        self._usedFuryOfTheEye = boolean

    def getPauseScreen(self):
        return self._pauseScreen

    def setPauseScreen(self, boolean):
        self._pauseScreen = boolean

    def getDeathScreen(self):
        return self._deathScreen

    def setDeathScreen(self, boolean):
        self._deathScreen = boolean
