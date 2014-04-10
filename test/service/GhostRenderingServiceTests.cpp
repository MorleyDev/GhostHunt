#include <GhostHunt/service/GhostRenderingService.h>
#include <bandit/bandit.h>
#include <GhostHunt/ec/Ghost.h>
#include <Emna/test/mocks/MockGraphicsDriver.h>
#include <Emna/test/mocks/MockContentFactory.h>
#include <Emna/test/mocks/content/MockTexture.h>

go_bandit([]() { 
	using namespace bandit;
	
	describe("Given a ghost rendering service", []() { 
		
		std::unique_ptr<emna::test::mocks::MockContentFactory> mockContentFactory;
		emna::test::mocks::content::MockTexture* mockTexture;
		
		std::unique_ptr<gh::service::GhostRenderingService> ghostRenderingService;
		before_each([&]() {
			mockContentFactory = emna::util::make_unique<emna::test::mocks::MockContentFactory>();
			mockContentFactory->mockloadTexture.setReturn([&](std::string file) { 
				auto texture = emna::util::make_unique<emna::test::mocks::content::MockTexture>(120, 210);
				mockTexture = texture.get();
				return texture;
			});
			
			ghostRenderingService = emna::util::make_unique<gh::service::GhostRenderingService>(*mockContentFactory);
		});
		it("Then the spritesheet is loaded", [&]() { 
			mockContentFactory->mockloadTexture.verify("resources/spritesheet.png");
		});
		
		describe("When drawing a ghost", [&]() { 
			std::unique_ptr<emna::test::mocks::MockGraphicsDriver> mockGraphicsDriver;
			gh::ec::Position position;
			before_each([&]() { 
				mockGraphicsDriver = emna::util::make_unique<emna::test::mocks::MockGraphicsDriver>();
				
				position = gh::ec::Position();
				position.x = 124.0f;
				position.y = 52.0f;
				
				(*ghostRenderingService)(*mockGraphicsDriver, position);
			});
			it("Then the ghost is drawn via the graphics driver as expected", [&]() { 
				emna::maths::point2f pos(position.x, position.y);
				emna::maths::point2f size(gh::ec::Ghost::Width, gh::ec::Ghost::Height);
				emna::maths::point2i texturePos(16, 0);
				emna::maths::point2i textureSize(16, 16);
				
				mockGraphicsDriver->mockdraw.verify(pos, size, texturePos, textureSize, mockTexture);
			});
		});
	});
});
