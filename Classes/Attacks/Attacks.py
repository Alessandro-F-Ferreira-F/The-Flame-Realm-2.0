from Classes.Animation.AnimatedSprite import *

class Attack(AnimatedSprite):
    def __init__(self, nomeAtk, danoAtk, spriteSheet, limiteLoops,
                 qtdFrames, pixels, spritePosition, offsetFrames):
        """
        :param nomeAtk: str
        :param danoAtk: int
        :param spriteSheet: spriteSheet loaded by pygame.image.load()
        :param limiteLoops: int
        :param pixels: (int, int) (pixels width, pixels height)
        :param qtdFrames: int
        :param spritePosition: (float, float)
        :param offsetFrames: float (dictates the animation speed)
        """
        AnimatedSprite.__init__(self, spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames)

        # Atributos
        self._nome = nomeAtk
        self._damage = danoAtk
        self._limiteLoops = limiteLoops
        self._isOver = True

        # Controle do loop do ataque
        self._loops_completos = 0

    def update(self):
        if self._loops_completos >= self.getLimiteLoops():
            print(f"{self.getName()} terminou após {self._loops_completos} loops.")
            self._loops_completos = 0
            self.setIsOver(True)
        else:
            self._image = self._frame[int(round(self._atual, 0))]
            self._atual += self.getOffsetFrames()
            if self._atual > self.getQtdFrames() - 1:
                self._atual = 0
                self._loops_completos += 1



    def getName(self):
        return self._nome

    def setName(self, n):
        self._nome = n

    def getDamage(self):
        return self._damage

    def setDamage(self, d):
        self._damage = d

    def getLimiteLoops(self):
        return self._limiteLoops

    def setLimiteLoops(self, l):
        self._limiteLoops = l

    def getIsOver(self):
        return self._isOver

    def setIsOver(self, s):
        self._isOver = s
