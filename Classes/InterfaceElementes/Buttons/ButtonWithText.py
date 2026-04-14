from Classes.InterfaceElementes.Buttons.BasicButtton import BasicButton
import parameters as param
import pygame as pg

class ButtonWithText(BasicButton):
    def __init__(self, screen, collor, coords, size, text, coordsText):
        """
        This class creates a Square/rectangular button with text
        :param screen: display where the object will be placed
        :param collor: (int, int, int), according to the RGB system
        :param coords: (float, float)
        :param size: (int, int), (width, height)
        :param text: Text type
        :param coordsText: (float, float)
        """
        BasicButton.__init__(self, screen, collor, coords, size)

        self._text = text
        self._coordsText = coordsText

    def draw(self):
        boolean = BasicButton.mouseInButton(self)
        if boolean:
            pg.draw.rect(self._screen, param.white, (self.getCoords(), self.getSize()))
            self.getText().setBold(True)
        else:
            pg.draw.rect(self.getScreen(), self.getCollor(), (self.getCoords(), self.getSize()))
            self.getText().setBold(False)

        self.getText().showText(self.getCoordsText())

    def getText(self):
        return self._text

    def setText(self, text):
        self._text = text

    def getCoordsText(self):
        return self._coordsText

    def setCoordsText(self, coordsText):
        self._coordsText = coordsText
