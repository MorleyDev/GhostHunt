#include <GhostHunt/view/ActorView.h>
#include <GhostHunt/ec/Actor.h>
#include <GhostHunt/ec/Position.h>
#include <GhostHunt/ec/Ghost.h>
#include <GhostHunt/ec/Player.h>
#include <bandit/bandit.h>
#include <Emna/test/mocks/MockContentFactory.h>
#include <Emna/test/mocks/MockGraphicsDriver.h>
#include <Emna/test/mocks/content/MockTexture.h>

go_bandit([]() { 
	using namespace bandit;

	describe("Given an ActorView with an EntityComponentStore and ContentFactory", []() {

		emna::content::Texture* mockTexture;
		emna::test::mocks::MockContentFactory mockContentFactory;
		std::unique_ptr<emna::ec::EntityComponentStore> entityComponentStore;
		std::unique_ptr<gh::view::ActorView> actorView;

		before_each([&]() {
			mockContentFactory = emna::test::mocks::MockContentFactory();
			mockContentFactory.mockloadTexture.setReturn([&](std::string name) {
				auto texture = emna::util::make_unique<emna::test::mocks::content::MockTexture>(52, 95);
				mockTexture = texture.get();
				return texture;
			});

			entityComponentStore = emna::util::make_unique<emna::ec::EntityComponentStore>();
			actorView = emna::util::make_unique<gh::view::ActorView>(*entityComponentStore, mockContentFactory);
		});
		it("Then the content factory is used to load the spritesheet", [&]() {
			mockContentFactory.mockloadTexture.verifyTimes(1, "resources/spritesheet.png");
		});
		
		describe("When drawing the view with a set of Ghosts and a Player with positions", [&]() {
			
			std::map<emna::ec::EntityId, std::tuple<gh::ec::Actor, gh::ec::Position>> entityComponentMap;
			std::unique_ptr<emna::test::mocks::MockGraphicsDriver> mockGraphicsDriver;
			
			gh::ec::Position playerPosition({ 123.0f, 98.0f });
			
			before_each([&]() { 
				entityComponentMap = std::map<emna::ec::EntityId, std::tuple<gh::ec::Actor, gh::ec::Position>>();
				entityComponentMap[emna::ec::EntityId::generate()] = std::make_tuple(gh::ec::Actor(), gh::ec::Position({10.2f, 19.0f}));
				entityComponentMap[emna::ec::EntityId::generate()] = std::make_tuple(gh::ec::Actor(), gh::ec::Position({9.5f, 29.0f}));
				entityComponentMap[emna::ec::EntityId::generate()] = std::make_tuple(gh::ec::Actor(), gh::ec::Position({1.2f, 99.0f}));
				for(auto ec : entityComponentMap) {
					entityComponentStore->add(ec.first);
					entityComponentStore->link(ec.first, gh::ec::Ghost());
					entityComponentStore->link(ec.first, std::get<0>(ec.second));
					entityComponentStore->link(ec.first, std::get<1>(ec.second));
				}
				
				auto playerId = emna::ec::EntityId::generate();
				entityComponentStore->add(playerId);
				entityComponentStore->link(playerId, gh::ec::Actor());
				entityComponentStore->link(playerId, gh::ec::Player());
				entityComponentStore->link(playerId, playerPosition);

				mockGraphicsDriver = emna::util::make_unique<emna::test::mocks::MockGraphicsDriver>();
				
				actorView->draw(*mockGraphicsDriver);
			});
			it("Then the ghosts are drawn", [&]() {
				for(auto e : entityComponentMap) {
					emna::maths::point2f pos(std::get<1>(e.second).x, std::get<1>(e.second).y);
					emna::maths::point2f size(gh::ec::Ghost::Width, gh::ec::Ghost::Height);
					emna::maths::point2i texturePos(0, 0);
					emna::maths::point2i textureSize(16, 16);
					
					mockGraphicsDriver->mockdraw.verify(pos, size, texturePos, textureSize, mockTexture);
				}
			});
			it("Then the player is drawn", [&]() { 
				
				emna::maths::point2f pos(playerPosition.x, playerPosition.y);
				emna::maths::point2f size(gh::ec::Player::Width, gh::ec::Player::Height);
				emna::maths::point2i texturePos(16, 0);
				emna::maths::point2i textureSize(16, 16);
				
				mockGraphicsDriver->mockdraw.verify(pos, size, texturePos, textureSize, mockTexture);
			});
		});
	});
});
