from Classes.InterfaceElementes.Buttons.Button import Button

class ButtonWithSprite(Button):
    def __init__(self, screen, coords, size, normalImage, alternativeImage):
        """
        This class creates a button with sprite
        :param screen: display where the object will be placed
        :param coords: (float, float)
        :param size: (int, int), (width, height)
        :param image: image loaded with pygame.image.load() (surface). Standard look for the button
        :param alternativeImage: surface. How the button will look when the mouse is in the button
        """
        Button.__init__(self, screen, coords, size)
        self._normalImage = normalImage
        self._alternativeImage = alternativeImage
        self._currentImage = normalImage # Which image is to be shown in the screen

    def draw(self):
        self.swapImages()
        self.getScreen().blit(self.getCurrentImage(), self.getCoords())

    def swapImages(self):
        if self.mouseInButton():
            self.setCurrentImage(self.getAlternativeImage())
        else:
            self.setCurrentImage(self.getNormalImage())

    def getNormalImage(self):
        return self._normalImage

    def setNormalImage(self, image):
        self._image = image

    def getAlternativeImage(self):
        return self._alternativeImage

    def setAlternativeImage(self, image):
        self._alternativeImage = image

    def getCurrentImage(self):
        return self._currentImage

    def setCurrentImage(self, image):
        self._currentImage = image
