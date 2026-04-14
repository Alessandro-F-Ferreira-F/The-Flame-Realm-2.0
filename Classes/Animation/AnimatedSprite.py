import pygame as pg

class AnimatedSprite(pg.sprite.Sprite):
    def __init__(self, spriteSheet, qtdFrames, pixels, spritePosition, offsetFrames):
        """
        :param spriteSheet: spriteSheet loaded by pygame.image.load()
        :param qtdFrames: int
        :param pixels: (int, int) (pixels width, pixels height) per frame
        :param spritePosition: (float, float). Top-left position
        :param offsetFrames: float (dictates the animation speed)
        """
        pg.sprite.Sprite.__init__(self)
        self._frame = []
        self._qtdFrames = qtdFrames
        self._spriteSheet = spriteSheet
        self._pixels = pixels
        self._offsetFrames = offsetFrames

        for i in range(qtdFrames):
            image = spriteSheet.subsurface((self.getPixels()[0] * i, 0), self.getPixels())
            self._frame.append(image)

        self._atual = 0
        self._image = self.getFrame()[self._atual]
        self._position = spritePosition

    def update(self):
        if self._atual > self.getQtdFrames() - 1:
            self._atual = 0
        self._image = self.getFrame()[int(round(self._atual, 0))]
        self._atual += self.getOffsetFrames()


    def getFrame(self):
        return self._frame

    def getSprite(self):
        return self._spriteSheet

    def getQtdFrames(self):
        return self._qtdFrames

    def getPixels(self):
        return self._pixels

    def getOffsetFrames(self):
        return self._offsetFrames

    def setOffsetFrames(self, s):
        self._offsetFrames = s

    def getPosition(self):
        return self._position

    def setPosition(self, pos):
        self._position = pos

    def getImage(self):
        return self._image

    def setImage(self, img):
        self._image = img
