from random import randint
import parameters as param
from Classes.Attacks.Attacks import Attack

class AtkChefe(Attack):
    def __init__(self, nomeAtk, danoAtk, spriteSheet, limiteLoops,
                 qtdFrames, pixels, spritePosition, offsetFrames, hitKillChance):
        """
        :param nomeAtk: str
        :param danoAtk: int
        :param spriteSheet: spriteSheet loaded by pygame.image.load()
        :param limiteLoops: int
        :param pixels: (int, int) (pixels width, pixels height)
        :param qtdFrames: int
        :param spritePosition: (float, float)
        :param offsetFrames: float (dictates the animation speed)
        :param hitKillChance: int
        """

        Attack.__init__(self, nomeAtk, danoAtk, spriteSheet, limiteLoops,
                        qtdFrames, pixels, spritePosition, offsetFrames)

        self._hitKillChance = hitKillChance

    def randomizeHitKill(self):
        randomizer = randint(1, 100)
        if randomizer <= self._hitKillChance:
            self.setDamage(param.playerHp)

    def getHitKillChance(self):
        return self._hitKillChance

    def setHitKillChance(self, c):
        self._hitKillChance = c
