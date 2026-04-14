import pygame as pg

class Button:
    def __init__(self, screen, coords, size):
        """
        This class was not made to be implemented. It just reunites the common attributes and methods of the real buttons
        :param screen: display where the object will be placed
        :param coords: (float, float)
        :param size: (int, int), (width, height)
        """
        self._screen = screen
        self._coords = coords
        self._size = size

    def mouseInButton(self):
        return (self.getCoords()[0] <= pg.mouse.get_pos()[0] <= self.getCoords()[0] + self.getSize()[0]
                and self.getCoords()[1] <= pg.mouse.get_pos()[1] <= self.getCoords()[1] + self.getSize()[1])

    def getScreen(self):
        return self._screen

    def getCoords(self):
        return self._coords

    def setCoords(self, coords):
        self._coords = coords

    def getSize(self):
        return self._size

    def setSize(self, size):
        self._size = size
