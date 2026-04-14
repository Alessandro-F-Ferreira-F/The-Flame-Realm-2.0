from Classes.Animation.AnimatedSprite import *

class CharacterSprite(AnimatedSprite):
    def __init__(self, spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames):
        """
        :param spriteSheet: spriteSheet loaded by pygame.image.load()
        :param qtdFrames: int
        :param pixels: (int, int) (pixels width, pixels height) per frame
        :param spritePosition: (float, float). Top-left position
        :param offsetFrames: float (dictates the animation speed)
        """

        AnimatedSprite.__init__(self, spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames)
        self._idle = self.getFrame()[1]
        self._moving = False

    def getIdle(self):
        return self._idle

    def setIdle(self, idle):
        self._idle = idle

    def getMoving(self):
        return self._moving

    def setMoving(self, moving):
        self._moving = moving

    def setSprite(self, spriteSheet):
        self._spriteSheet = spriteSheet
        AnimatedSprite.__init__(self, spriteSheet, self.getQtdFrames(), self.getPixels(),
                                self.getPosition(), self.getOffsetFrames())
