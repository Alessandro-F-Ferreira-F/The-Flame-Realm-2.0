screenDimensions = (900, 600)

# 1 - Colors
yellow = (255, 255, 0)
orange = (255, 120, 0)
darkOrange = (200, 50, 0)
red = (180, 0, 0)
white = (255, 255, 255)
black = (0, 0, 0)
green = (0, 180, 0)
cian = (0, 200, 255)
blue = (0, 0, 255)

# 2 - Button: positions and dimensions

# 2.1 Dimensions
mainMenuButtonsSize = (50, 50)
returnToMainMenuButtonSize = (150, 20)
returnToPlayButtonSize = (102, 20)

moveButtonHSize = (64, 48)
moveButtonVSize = (48, 64)

pauseButtonSize = (48, 48)

combatButtonsSize = (50, 40)
secretEyeSize = (40, 40)

secretButtonSize = (20, 20)

# 2.2 Positions

# 2.2.1 - Main menu
mainMenuButtonsX = screenDimensions[0] * 0.1
playButtonPosition = (mainMenuButtonsX, screenDimensions[1] * 0.15)
historyButtonPosition = (mainMenuButtonsX, screenDimensions[1] * 0.35)
tutorialButtonPosition = (mainMenuButtonsX, screenDimensions[1] * 0.55)
quitButtonPosition = (mainMenuButtonsX, screenDimensions[1] * 0.75)

# 2.2.2 - Return buttons
returnToMainMenuButtonPosition = (screenDimensions[0] * 0.02, screenDimensions[1] * 0.95)
returnToPlayButtonPosition = (screenDimensions[0] * 0.865, screenDimensions[1] * 0.95)

# 2.2.3 - Play screen
moveButtonRightPosition = (screenDimensions[0] * 0.17, screenDimensions[1] * 0.7)
moveButtonLeftPosition = (screenDimensions[0] * 0.02, screenDimensions[1] * 0.7)
moveButtonUpPosition = (screenDimensions[0] * 0.106, screenDimensions[1] * 0.58)
moveButtonDownPosition = (screenDimensions[0] * 0.106, screenDimensions[1] * 0.79)

pauseButtonPosition = (screenDimensions[0] * 0.106, screenDimensions[1] * 0.7)

# 2.2.4 - Secret Button (History screen)
secretButtonPosition = (screenDimensions[0] * 0.95, screenDimensions[1] * 0.95)

# 2.2.5 - combat screen
vortexButtonPosition = (screenDimensions[0] * 0.25, screenDimensions[1] * 0.8)
azuringButtonPosition = (screenDimensions[0] * 0.25, screenDimensions[1] * 0.9)
nebulaButtonPosition = (screenDimensions[0] * 0.5, screenDimensions[1] * 0.9)
razorButtonPosition = (screenDimensions[0] * 0.5, screenDimensions[1] * 0.8)

secretEyePosition = screenDimensions[0] * 0.5125, screenDimensions[1] * 0.065


# 3 Text

# 3.1 Main menu
mainMenuTextSize = 50
mainMenuTextFont = "gabriola"

# 3.2 - return button
returnButtonTextFont = "arial"
returnButtonTextSize = 15

# 3.3 History screen
historyScreenFont = "Times New Roman"
historyScreenTextSize = 30
secretTextPosition = (screenDimensions[0] * 0.1, screenDimensions[1] * 0.7)

# 3.4 - Tutorial screen
tutorialScreenFont = historyScreenFont
tutorialScreenTextSize = 27

# 3.5 - combat screen
combatScreenFont = historyScreenFont
combatScreenTextSize = 17

# 4 - Characters Sprites

# 4.1 - Dimensions (in pixels)
theadDarkusSize = (224, 240)
combatFormSize = (128, 128)
playerSize = (48, 64)

# 4.2 - Number of Frames
theadDarkusFrames = 15
combatFormFrames = 14
playerMoveFrames = 4

# 4.3 Offsets (dictates the animation speed)
theadDarkusOffset = 0.18
combatFormOffset = 0.18
playerOffset = 0.1

# 4.4 Positions (top-left of the images)
theadDarkusPosition = (0, screenDimensions[1] * 0.3)
combatFormPosition = (screenDimensions[0] * 0.8, screenDimensions[1] * 0.5)
playerPosition = (screenDimensions[0]/2 - playerSize[0]/2, screenDimensions[1]/2 - playerSize[1]/2)

# 5 - Attacks parameters

# 5.1 - player

# 5.1.1 - Damage
vortexDamage = 200
azuringDamage = 40
nebulaDamage = 95
razorDamage = 65

# 5.1.2 - Mana
vortexMana = 55
azuringMana = 10
nebulaMana = 20
razorMana = 15

# 5.1.3 - Dimensions (in pixels)
vortexPixels = (128, 128)
azuringPixels = (100, 100)
nebulaPixels = (40, 40)
razorPixels = (40, 40)

# 5.1.4 - Number of frames
vortexFrames = 60
azuringFrames = 60
nebulaFrames = 64
razorFrames = 60

# 5.1.5 - Positions
vortexPosition = (screenDimensions[0] * 0.055, screenDimensions[1] * 0.4)
azuringPosition = (screenDimensions[0] * 0.07, screenDimensions[1] * 0.5)
nebulaPosition = (screenDimensions[0] * 0.105, screenDimensions[1] * 0.5)
razorPosition = (screenDimensions[0] * 0.1, screenDimensions[1] * 0.5)

# 5.1.6 - Loops
vortexLoops = 2
azuringLoops = 2
nebulaLoops = 1
razorLoops = 3

# 5.1.7 - Offsets
vortexOffset = 1
azuringOffset = 1
nebulaOffset = 0.5
razorOffset = 1.2

# 5.2 - Boss

# 5.2.1 - Damage
eldenRingDamage = 30
fireSoulDamage = 40
darkVortexDamage = 100
flamelashDamage = 10

# 5.2.2 - HitKill Chance
eldenRingHitKillChance = 90
fireSoulHitKillChance = 80
darkVortexHitKillChance = 100
flamelashHitKillChance = 70

# 5.2.3 - Dimensions (in pixels)
eldenRingPixels = (40, 40)
fireSoulPixels = (50, 50)
darkVortexPixels = (128, 128)
flamelashPixels = (100, 100)

# 5.2.4 - Number of frames
eldenRingFrames = 60
fireSoulFrames = 60
darkVortexFrames = 60
flamelashFrames = 49

# 5.2.5 - Positions
eldenRingPosition = (screenDimensions[0] * 0.848, screenDimensions[1] * 0.55)
fireSoulPosition = (screenDimensions[0] * 0.84, screenDimensions[1] * 0.55)
darkVortexPosition = (screenDimensions[0] * 0.8, screenDimensions[1] * 0.45)
flamelashPosition = (screenDimensions[0] * 0.82, screenDimensions[1] * 0.5)

# 5.2.6 - Loops
eldenRingLoops = 4
fireSoulLoops = 3
darkVortexLoops = 1
flamelashLoops = 3

# 5.2.7 - Offsets
eldenRingOffset = 0.9
fireSoulOffset = 0.7
darkVortexOffset = 0.2
flamelashOffset = 0.6


# 5.3 Fury of the eye

furyOfTheEyeDamage = 200
furyOfTheEyePixels = (64, 64)
furyOfTheEueFrames = 12
furyOfTheEyePosition = (screenDimensions[0] * 0.09, screenDimensions[1] * 0.5)
furyOfTheEyeLoops = 8
furyOfTheEyeOffset = 1

# 6 - Map

# 6.1 - Game points
downPoint = (1150, 2450)
centralPoint = (1150, 1000)
rightPoint = (2175, 1000)
leftPoint = (125, 1000)
highPoint = (1150, 170)

# 6.2 - Speed
mapSpeed = 6
tolerance = mapSpeed * 0.5 # will be used to test which point the player is close to


# 7 - stats

# 7.1 - Player
playerHp = 100
playerMana = 100

playerHpBarSize = 100
playerManaBarSize = playerHpBarSize

# 7.2 - Boss
theadDarkusHp = 400

theadDarkusHpBarSize = 200
