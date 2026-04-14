import pygame as pg
from Classes.Attacks import Attacks
from Classes.Attacks import PlayerAttack, BossAttack
import parameters as param
import Instances.ButtonsInstances as buttonsInst

# 1 - Player attacks
azuringSprite = pg.image.load("Images/Attacks/Player/Azuring Impact.png").convert_alpha()
azuring = PlayerAttack.AtkPersonagem("Azuring Impact", param.azuringDamage, azuringSprite, param.azuringLoops,
                            param.azuringFrames, param.azuringPixels, param.azuringPosition, param.azuringOffset,
                            param.azuringMana, buttonsInst.azuringButton)

nebulaSprite = pg.image.load("Images/Attacks/Player/Nebula.png").convert_alpha()
nebula = PlayerAttack.AtkPersonagem("Nebula Crystal", param.nebulaDamage, nebulaSprite, param.nebulaLoops, param.nebulaFrames,
                           param.nebulaPixels, param.nebulaPosition, param.nebulaOffset, param.nebulaMana, buttonsInst.nebulaButton)

vortexSprite = pg.image.load("Images/Attacks/Player/Dual Vortex.png").convert_alpha()
vortex = PlayerAttack.AtkPersonagem("Dual Vortex", param.vortexDamage, vortexSprite, param.vortexLoops, param.vortexFrames,
                           param.vortexPixels, param.vortexPosition, param.vortexOffset, param.vortexMana, buttonsInst.vortexButton)

razorSprite = pg.image.load("Images/Attacks/Player/Razor.png").convert_alpha()
razor = PlayerAttack.AtkPersonagem("Razor Shock", param.razorDamage, razorSprite, param.razorLoops, param.razorFrames,
                          param.razorPixels, param.razorPosition, param.razorOffset, param.razorMana, buttonsInst.razorButton)

playerAtkList = [azuring, nebula, vortex, razor]

# 2 - Boss attacks
eldenRingSprite = pg.image.load("Images/Attacks/Boss/Elden Ring.png").convert_alpha()
eldenRing = BossAttack.AtkChefe("Elden Ring", param.eldenRingDamage, eldenRingSprite, param.eldenRingLoops,
                              param.eldenRingFrames, param.eldenRingPixels, param.eldenRingPosition,
                              param.eldenRingOffset, param.eldenRingHitKillChance)

flamelashSprite = pg.image.load("Images/Attacks/Boss/Flamelash.png").convert_alpha()
flamelash = BossAttack.AtkChefe("Flamelash", param.flamelashDamage, flamelashSprite, param.flamelashLoops,
                              param.flamelashFrames, param.flamelashPixels, param.flamelashPosition,
                              param.flamelashOffset, param.flamelashHitKillChance)

darkVortexSprite = pg.image.load("Images/Attacks/Boss/Dark Vortex.png").convert_alpha()
darkVortex = BossAttack.AtkChefe("Dark Vortex", param.darkVortexDamage, darkVortexSprite, param.darkVortexLoops,
                              param.darkVortexFrames, param.darkVortexPixels, param.darkVortexPosition,
                              param.darkVortexOffset, param.darkVortexHitKillChance)

fireSoulSprite = pg.image.load("Images/Attacks/Boss/Fire Soul.png").convert_alpha()
fireSoul = BossAttack.AtkChefe("Fire Soul", param.fireSoulDamage, fireSoulSprite, param.fireSoulLoops,
                              param.fireSoulFrames, param.fireSoulPixels, param.fireSoulPosition,
                              param.fireSoulOffset, param.fireSoulHitKillChance)

theadDarkusAtks = [flamelash, fireSoul, eldenRing, darkVortex]

# 3 - Fury of the eye
furyOfTheEyeImage = pg.image.load("Images/PuzzleElements/Fury of the eye.png").convert_alpha()
furyOfTheEye = Attacks.Attack("Fury of the eye", param.furyOfTheEyeDamage, furyOfTheEyeImage, param.furyOfTheEyeLoops,
                          param.furyOfTheEueFrames, param.furyOfTheEyePixels, param.furyOfTheEyePosition,
                          param.furyOfTheEyeOffset)
