#include <Emna/Emna.h>
#include <Emna/util/log.h>
#include <iostream>
#include <memory>

class MainGame : public emna::Game
{
public:
    MainGame()
	: Game(640, 400)
	{
		inputDriver.onClose([this]() { 
			kill();
		});
	}
};

int main()
{
	MainGame game;
	game.run();
	return 0;
}
