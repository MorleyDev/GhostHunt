#include <GhostHunt/service/PlayerRenderingService.h>
#include <GhostHunt/ec/Player.h>

#include <bandit/bandit.h>

#include <Emna/test/mocks/MockGraphicsDriver.h>
#include <Emna/test/mocks/MockContentFactory.h>
#include <Emna/test/mocks/content/MockTexture.h>

go_bandit([]() { 
	using namespace bandit;
	
	describe("Given a player rendering service", []() { 
		
		std::unique_ptr<emna::test::mocks::MockContentFactory> mockContentFactory;
		emna::test::mocks::content::MockTexture* mockTexture;
		
		std::unique_ptr<gh::service::PlayerRenderingService> playerRenderingService;
		before_each([&]() {
			mockContentFactory = emna::util::make_unique<emna::test::mocks::MockContentFactory>();
			mockContentFactory->mockloadTexture.setReturn([&](std::string file) { 
				auto texture = emna::util::make_unique<emna::test::mocks::content::MockTexture>(120, 210);
				mockTexture = texture.get();
				return texture;
			});
			
			playerRenderingService = emna::util::make_unique<gh::service::PlayerRenderingService>(*mockContentFactory);
		});
		it("Then the spritesheet is loaded", [&]() { 
			mockContentFactory->mockloadTexture.verify("resources/spritesheet.png");
		});
		
		describe("When drawing a player", [&]() { 
			std::unique_ptr<emna::test::mocks::MockGraphicsDriver> mockGraphicsDriver;
			gh::ec::Position position;
			before_each([&]() { 
				mockGraphicsDriver = emna::util::make_unique<emna::test::mocks::MockGraphicsDriver>();
				
				position = gh::ec::Position();
				position.x = 124.0f;
				position.y = 52.0f;
				
				(*playerRenderingService)(*mockGraphicsDriver, position);
			});
			it("Then the player is drawn via the graphics driver as expected", [&]() { 
				emna::maths::point2f pos(position.x, position.y);
				emna::maths::point2f size(gh::ec::Player::Width, gh::ec::Player::Height);
				emna::maths::point2i texturePos(0, 0);
				emna::maths::point2i textureSize(16, 16);
				
				mockGraphicsDriver->mockdraw.verify(pos, size, texturePos, textureSize, mockTexture);
			});
		});
	});
});
