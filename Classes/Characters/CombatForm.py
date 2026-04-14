from Classes.Attacks.Attacks import *
from random import randint

class CombatForm(AnimatedSprite):
    def __init__(self, spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames, healthPoints, atkList):
        """
        :param spriteSheet: spriteSheet loaded by pygame.image.load()
        :param qtdFrames: int
        :param pixels: (int, int) (pixels width, pixels height) per frame
        :param spritePosition: (float, float). Top-left position
        :param offsetFrames: float (dictates the animation speed)
        :param healthPoints: int
        :param atkList: list off Attack type objects
        """
        AnimatedSprite.__init__(self, spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames)

        self._healthPoints = healthPoints
        self._atkList = atkList

    def randomizeAtk(self):
        randomizer = randint(0, len(self._atkList) - 1)
        return self._atkList[randomizer]

    def revive(self, HP):
        self.setHealthPoints(HP)

    def getHealthPoints(self):
        return self._healthPoints

    def setHealthPoints(self, healthPoints):
        if healthPoints <= 0:
            self._healthPoints = 0
        else:
            self._healthPoints = healthPoints

    def getAtkList(self):
        return self._atkList

    def setAtkList(self, atkList):
        self._atkList = atkList

class PlayerCombatForm(CombatForm):
    def __init__(self, spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames, healthPoints, atkList, manaPoints):
        """
        :param spriteSheet: spriteSheet loaded by pygame.image.load()
        :param qtdFrames: int
        :param pixels: (int, int) (pixels width, pixels height) per frame
        :param spritePosition: (float, float). Top-left position
        :param offsetFrames: float (dictates the animation speed)
        :param healthPoints: int
        :param atkList: list off Attack type objects
        :param manaPoints: int
        """
        CombatForm.__init__(self, spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames, healthPoints, atkList)

        self._manaPoints = manaPoints

    def atkCheck(self):
        lista = self.getAtkList()
        for i in lista:
            if i.getAtkButton().mouseInButton():
                return i

        return None

    def getManaPoints(self):
        return self._manaPoints

    def setManaPoints(self, manaPoints):
        self._manaPoints = manaPoints
