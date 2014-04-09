#include <GhostHunt/controller/AnimatedController.h>
#include <GhostHunt/ec/Animated.h>
#include <bandit/bandit.h>

go_bandit([]() { 
	using namespace bandit;
	
	describe("Given an Animated controller and animated entities", []() {
		
		emna::ec::EntityComponentStore entityComponentStore;
		std::vector<emna::ec::EntityId> animatedEntities;
		std::unique_ptr<gh::controller::AnimatedController> animatedController;
		
		before_each([&]() { 
			entityComponentStore = emna::ec::EntityComponentStore();
			animatedController = emna::util::make_unique<gh::controller::AnimatedController>(entityComponentStore);
			
			animatedEntities = std::vector<emna::ec::EntityId>();
			animatedEntities.push_back(emna::ec::EntityId::generate());
			animatedEntities.push_back(emna::ec::EntityId::generate());
			
			entityComponentStore.add(animatedEntities[0]);
			entityComponentStore.add(animatedEntities[1]);
			entityComponentStore.link(animatedEntities[0], gh::ec::Animated({ std::chrono::microseconds(1000) }));
			entityComponentStore.link(animatedEntities[1], gh::ec::Animated({ std::chrono::microseconds(2000) }));
		});
		describe("When updating the controller", [&]() { 
			std::chrono::microseconds updateTime;
			before_each([&]() { 
				updateTime = std::chrono::microseconds(1500);
				animatedController->update(updateTime);
			});
			it("Then the animated components are updated", [&]() { 
				auto animatedEntity0 = entityComponentStore.getComponentFor<gh::ec::Animated>(animatedEntities[0]);
				auto animatedEntity1 = entityComponentStore.getComponentFor<gh::ec::Animated>(animatedEntities[1]);
				
				AssertThat(animatedEntity0.runningTime, Is().EqualTo(std::chrono::microseconds(2500)));
				AssertThat(animatedEntity1.runningTime, Is().EqualTo(std::chrono::microseconds(3500)));
			});
		});
	});
});
