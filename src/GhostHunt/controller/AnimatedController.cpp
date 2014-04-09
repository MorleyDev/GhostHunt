#include "AnimatedController.h"
#include "../ec/Animated.h"

gh::controller::AnimatedController::AnimatedController(emna::ec::EntityComponentStore& entityComponentStore)
: m_entityComponentStore(entityComponentStore)
{
}

void gh::controller::AnimatedController::update(std::chrono::microseconds deltaTime)
{
	for(auto animated : m_entityComponentStore.getAllEntitiesWith<ec::Animated>()) {
		auto component = m_entityComponentStore.getComponentFor<ec::Animated>(animated);
		component.runningTime += deltaTime;
		m_entityComponentStore.link(animated, component);
	}
}
