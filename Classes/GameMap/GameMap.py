from Classes.GameMap.FightPoint import FightPoint

class GameMap:
    def __init__(self, image, fightPoints, pointDistTollerance):
        """
        This class creates a new game map.
        :param image: Full image of the map
        :param fightPoints: list of FightPoint objects
        :param pointDistTollerance: float (used to calculate de difference between te current point and the other
         fighPoint objects)
        """
        self._currentPoint = FightPoint(fightPoints[0].getPoint(), fightPoints[0].getAllowedDirections(),
                                        fightPoints[0].getActivated())

        self._previousPoint = FightPoint(fightPoints[0].getPoint(), fightPoints[0].getAllowedDirections(),
                                        fightPoints[0].getActivated())
        self._image = image
        self._fightPoints = fightPoints
        self.tol = pointDistTollerance

    def pointMatchTest(self, point):
        """
        :param point: FightPoint object
        :return: boolean
        """
        return (point.getPoint()[0] - self.getTol() <= self.getCurrentPoint().getPoint()[0] <= point.getPoint()[0] + self.getTol() and
                point.getPoint()[1] - self.getTol() <= self.getCurrentPoint().getPoint()[1] <= point.getPoint()[1] + self.getTol())

    def pointMatch(self, point):
        test = self.pointMatchTest(point)
        if test:
            self.getCurrentPoint().setPoint(point.getPoint())
            self.getCurrentPoint().setAllowedDirections(point.getAllowedDirections())
            self.getCurrentPoint().setActivated(point.getActivated())
        return test

    def fightPointTest(self):
        list = self.getFightPoints()

        for fighPoint in list:
            if self.pointMatch(fighPoint):
                return True

        return False

    def disableCurrentPoint(self):
        list = self.getFightPoints()

        for fightPoint in list:
            if self.pointMatchTest(fightPoint):
                fightPoint.deactivate()
                self.getCurrentPoint().deactivate()


    def getCurrentPoint(self):
        return self._currentPoint

    def setCurrentPoint(self, point):
        self.getCurrentPoint().setPoint(point.getPoint())
        self.getCurrentPoint().setAllowedDirections(point.getAllowedDirections())
        self.getCurrentPoint().setActivated(point.getActivated())

    def getImage(self):
        return self._image

    def setImage(self, image):
        self._image = image

    def getFightPoints(self):
        return self._fightPoints

    def getTol(self):
        return self.tol

    def setTol(self, tol):
        self.tol = tol

    def getPreviousPoint(self):
        return self._previousPoint

    def setPreviousPoint(self, point):
        self.getPreviousPoint().setPoint(point.getPoint())
        self.getPreviousPoint().setAllowedDirections(point.getAllowedDirections())
        self.getPreviousPoint().setActivated(point.getActivated())
