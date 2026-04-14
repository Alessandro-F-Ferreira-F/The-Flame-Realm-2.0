class FightPoint:
    def __init__(self, point, allowedDirections, boolean = True):
        """
        :param point: (float, float)
        :param allowedDirections: list of allowed directions (each direction receives "UP", "DOWN", "LEFT" or "RIGHT")
        :param boolean: indicates if the point is activated
        """
        self._point = point
        self._allowedDirections = allowedDirections
        self._activated = boolean

    def activate(self):
        self.setActivated(True)

    def deactivate(self):
        self.setActivated(False)

    def getPoint(self):
        return self._point

    def setPoint(self, point):
        self._point = point

    def getAllowedDirections(self):
        return self._allowedDirections

    def setAllowedDirections(self, allowedDirecions):
        self._allowedDirections = allowedDirecions

    def getActivated(self):
        return self._activated

    def setActivated(self, boolean):
        self._activated = boolean
