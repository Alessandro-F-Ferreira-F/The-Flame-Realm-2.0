import pygame as pg

class Text:
    def __init__(self, fontName, fontSize, fontColor, bold, italic, message, antialising, screen):
        """
        This class is used to create the text object
        :param fontName:
        :param fontSize:
        :param fontColor:
        :param bold:
        :param italic:
        :param message:
        :param antialising:
        :param screen:
        """
        self._fontName = fontName
        self._fontSize = fontSize
        self._fontColor = fontColor
        self._bold = bold
        self._italic = italic
        self._message = message
        self._antialising = antialising
        self._screen = screen

        self._fonte = ''
        self._formatedText = ''

        self.changeFont()

    def showText(self, position):
        self._screen.blit(self._formatedText, position)

    def changeFont(self):
        self._fonte = pg.font.SysFont(self.getFontName(), self.getFontSize(), self.getBold(), self.getItalic())
        self._formatedText = self._fonte.render(self.getMessage(), self.getAntialising(), self.getFontColor())

    def getFontName(self):
        return self._fontName

    def setFontName(self, fontName):
        self._fontName = fontName
        self.changeFont()

    def getFontSize(self):
        return self._fontSize

    def setFontSize(self, fontSize):
        self._fontSize = fontSize
        self.changeFont()

    def getFontColor(self):
        return self._fontColor

    def setFontColor(self, fontColor):
        self._fontColor = fontColor
        self.changeFont()

    def getBold(self):
        return self._bold

    def setBold(self, bold):
        self._bold = bold
        self.changeFont()

    def getItalic(self):
        return self._italic

    def setItalic(self, italic):
        self._italic = italic
        self.changeFont()

    def getMessage(self):
        return self._message

    def setMessage(self, message):
        self._message = message
        self.changeFont()

    def getAntialising(self):
        return self._antialising

    def setAntialising(self, antialising):
        self._antialising = antialising
        self.changeFont()

    def getScreen(self):
        return self._screen
