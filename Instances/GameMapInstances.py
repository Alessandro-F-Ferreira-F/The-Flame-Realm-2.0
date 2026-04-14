import Classes.GameMap.GameMap as gp
import parameters as param
import pygame as pg

# 1 - Image
gameMapImage = pg.image.load("Images/Gamemaps/GameMap.png")

# 2 - FightPoint instances

startPoint = gp.FightPoint(param.downPoint, ["UP"], False)
centralPoint = gp.FightPoint(param.centralPoint, ["UP", "DOWN", "LEFT", "RIGHT"])
rightPoint = gp.FightPoint(param.rightPoint, ["LEFT"])
leftPoint = gp.FightPoint(param.leftPoint, ["RIGHT"])
highPoint = gp.FightPoint(param.highPoint, ["DOWN"])

gameMapPoints = [startPoint, centralPoint, rightPoint, leftPoint, highPoint]

# 3 - gameMap object
gameMap = gp.GameMap(gameMapImage, gameMapPoints, param.tolerance)
