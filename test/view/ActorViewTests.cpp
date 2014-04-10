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

		emna::test::Mock<void (emna::GraphicsDriver*, gh::ec::Position)> mockGhostRenderingService;
		emna::test::Mock<void (emna::GraphicsDriver*, gh::ec::Position)> mockPlayerRenderingService;
		
		std::unique_ptr<emna::ec::EntityComponentStore> entityComponentStore;
		std::unique_ptr<gh::view::ActorView> actorView;

		before_each([&]() {
			entityComponentStore = emna::util::make_unique<emna::ec::EntityComponentStore>();

			auto grs = [&](emna::GraphicsDriver& g, gh::ec::Position p) { mockGhostRenderingService(&g, p); };
			auto prs = [&](emna::GraphicsDriver& g, gh::ec::Position p) { mockPlayerRenderingService(&g, p); };
			actorView = emna::util::make_unique<gh::view::ActorView>(*entityComponentStore, grs, prs);
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
					auto position = std::get<1>(e.second);
					mockGhostRenderingService.verify(mockGraphicsDriver.get(), 
					                                 [&](gh::ec::Position pos) { return pos.x == position.x && pos.y == position.y; });
				}
			});
			it("Then the player is drawn", [&]() {
				mockPlayerRenderingService.verify(mockGraphicsDriver.get(), 
				                                  [&](gh::ec::Position pos) { return pos.x == playerPosition.x && pos.y == playerPosition.y; });
			});
		});
	});
});
