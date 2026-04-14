import pygame as pg
from Classes.Characters import CharacterSprite as charSprite, CombatForm
import parameters as param
from Instances import AttacksInstances as atkInst

# 1 - Main character

# 1.1 Normal form
playerSpriteUp = pg.image.load("Images/Characters/Player/main character (walking up).png")
playerSpriteDown = pg.image.load("Images/Characters/Player/main character (walking down).png")
playerSpriteRight = pg.image.load("Images/Characters/Player/main character (walking right).png")
playerSpriteLeft = pg.image.load("Images/Characters/Player/main character (walking left).png")

playerObject = charSprite.CharacterSprite(playerSpriteUp, param.playerMoveFrames, param.playerSize,
                                          param.playerPosition, param.playerOffset)

# 1.2 Combat form
combatFormImage = pg.image.load("Images/Characters/Player/BlueMageGuardian.png").convert_alpha()

playerCombatForm = CombatForm.PlayerCombatForm(combatFormImage, param.combatFormFrames, param.combatFormSize,
                                               param.combatFormPosition, param.combatFormOffset, param.playerHp,
                                               atkInst.playerAtkList, param.playerMana)

# 2 - Boss
theadDarkusImage = pg.image.load("Images/Characters/Boss/Thead Darkus.png").convert_alpha()

theadDarkus = CombatForm.CombatForm(theadDarkusImage, param.theadDarkusFrames, param.theadDarkusSize,
                                    param.theadDarkusPosition, param.theadDarkusOffset, param.theadDarkusHp, atkInst.theadDarkusAtks)
