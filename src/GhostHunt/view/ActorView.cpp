#include "ActorView.h"
#include "../ec/Actor.h"
#include "../ec/Ghost.h"
#include "../ec/Player.h"

gh::view::ActorView::ActorView(emna::ec::EntityComponentStore &entities,
			          std::function<void (emna::GraphicsDriver&, gh::ec::Position)> drawGhost,
					  std::function<void (emna::GraphicsDriver&, gh::ec::Position)> drawPlayer)
: m_entities(entities),
  m_drawGhost(drawGhost),
  m_drawPlayer(drawPlayer)
{
}

void gh::view::ActorView::draw(emna::GraphicsDriver& graphics)
{
	for(auto actor : m_entities.getAllEntitiesWith<gh::ec::Ghost, gh::ec::Position, gh::ec::Actor>())
		m_drawGhost(graphics, m_entities.getComponentFor<gh::ec::Position>(actor));
		
	for(auto actor : m_entities.getAllEntitiesWith<gh::ec::Player, gh::ec::Position, gh::ec::Actor>())
		m_drawPlayer(graphics, m_entities.getComponentFor<gh::ec::Position>(actor));
}
