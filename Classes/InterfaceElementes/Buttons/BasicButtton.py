import parameters as param
from Classes.InterfaceElementes.Text.Text import *
from Classes.InterfaceElementes.Buttons.Button import Button

class BasicButton(Button):
    def __init__(self, screen, collor, coords, size):
        """
        This class creates a basic square/rectangular button
        :param screen: display where the object will be placed
        :param collor: (int, int, int), according to the RGB system
        :param coords: (float, float)
        :param size: (int, int), (width, height)
        """
        Button.__init__(self, screen, coords, size)
        self._collor = collor

    def draw(self):
        boolean = self.mouseInButton()
        if boolean:
            pg.draw.rect(self.getScreen(), param.white, (self.getCoords(), self.getSize()))
        else:
            pg.draw.rect(self.getScreen(), self.getCollor(), (self.getCoords(), self.getSize()))

    def getCollor(self):
        return self._collor

    def setCollor(self, collor):
        self._collor = collor
