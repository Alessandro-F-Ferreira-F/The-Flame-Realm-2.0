from Classes.Attacks.Attacks import Attack

class AtkPersonagem(Attack):
    def __init__(self, nomeAtk, danoAtk, spriteSheet, limiteLoops,
                 qtdFrames, pixels, spritePosition, offsetFrames, atkMana, atkButton):
        """
        :param nomeAtk: str
        :param danoAtk: int
        :param spriteSheet: spriteSheet loaded by pygame.image.load()
        :param limiteLoops: int
        :param pixels: (int, int) (pixels width, pixels height)
        :param qtdFrames: int
        :param spritePosition: (float, float)
        :param offsetFrames: float (dictates the animation speed)
        :param atkMana: int
        :param atkButton: ButtonWithText
        """

        Attack.__init__(self, nomeAtk, danoAtk, spriteSheet, limiteLoops,
                        qtdFrames, pixels, spritePosition, offsetFrames)

        self._atkMana = atkMana
        self._atkButton = atkButton

    def getAtkButton(self):
        return self._atkButton

    def setAtkButton(self, b):
        self._atkButton = b

    def getMana(self):
        return self._atkMana

    def setMana(self, c):
        self._atkMana = c