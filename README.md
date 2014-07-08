# Ghost Hunt - Multiplayer Prototype

## Description
Written for university coursework, this is a multi-player game modeled after Pac-Man where the player and ghosts are all human players. The game is written in Scala using JSFML for graphics and audio. To run, download the [latest release](https://github.com/MorleyDev/GhostHunt/releases), unzip and launch either the ghosthunt shell script or the ghosthunt.bat batch file in the bin directory.

## Compilation
To compile and package, clone or download the repository and with the latest version of sbt on the PATH run the following command in the command line:
> sbt compile universal:package-bin

A zip file containing the compiled binaries, similar to the one downloadable through the above link, can then be found in target/universal.

## Controls
### In-Game
- W = Move Up
- A = Move Left
- S = Move Right
- D = Move Down

### In-Menu
- Mouse Click = Select a menu item (text boxes, buttons, etc.)
- Keyboard = Typing for Host and Port

## Goals
The goals of the 'Hero' are to collect all the pellets and avoid the ghosts.
The goal of the ghosts are to hound and catch the player.

## Known Issues
### Gameplay
- It is possible for the ghosts to make victory impossible for the player by simply not moving, as this means they'll remain covering a pellet.
- It is difficult for the player to lose the ghosts when they are detected, as they have no means of repelling or escaping except for moving around the maze.

### Technical
- If the hero disconnects, then two hero characters will be displayed on-screen when the server returns to the lobby and in the next game.

## Screenshots
![Ghost View](http://i1087.photobucket.com/albums/j479/MorleyDev/PacmanPrototype1_zpsc3265235.png)
![Player View](http://i1087.photobucket.com/albums/j479/MorleyDev/PacmanPrototype2_zpsaec7abe0.png)

## Credits
| Filename | Title | Artist | License | Source |
|----------|-------|--------|---------|--------|
| font.ttf | Sensation | Bernd Montag | Freeware | http://www.dafont.com/sansation.font |
| title.ogg | Wings | Halcyonic Falcon X | http://creativecommons.org/publicdomain/zero/1.0/ |  http://open.commonly.cc/ |
| game.ogg | Those Of Us Who Fight | Halcyonic Falcon X | http://creativecommons.org/publicdomain/zero/1.0/ | http://open.commonly.cc/ |
| endgame.ogg | That Which We Have Lost And Forgotten | Halcyonic Falcon X | http://creativecommons.org/publicdomain/zero/1.0/ | http://open.commonly.cc/ |
| player_death.ogg | Shot Gun Sound | luminalace | http://creativecommons.org/licenses/by/3.0/ | http://soundbible.com/1706-Shot-Gun.html |
| beep.ogg | beepC | Hydranos | http://creativecommons.org/publicdomain/zero/1.0/ | http://www.freesound.org/people/Hydranos/sounds/237706/ |
