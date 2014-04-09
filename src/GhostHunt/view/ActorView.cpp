#include "ActorView.h"
#include "../ec/Actor.h"
#include "../ec/Ghost.h"
#include "../ec/Player.h"

gh::view::ActorView::ActorView(emna::ec::EntityComponentStore& entities, emna::ContentFactory& content)
: m_entities(entities),
  m_spriteSheet(content.loadTexture("resources/spritesheet.png"))
{
}

void gh::view::ActorView::draw(emna::GraphicsDriver& graphics)
{
	for(auto actor : m_entities.getAllEntitiesWith<gh::ec::Ghost, gh::ec::Position, gh::ec::Actor>())
		drawGhost(m_entities.getComponentFor<gh::ec::Position>(actor), graphics);
		
	for(auto actor : m_entities.getAllEntitiesWith<gh::ec::Player, gh::ec::Position, gh::ec::Actor>())
		drawPlayer(m_entities.getComponentFor<gh::ec::Position>(actor), graphics);
}

void gh::view::ActorView::drawGhost(gh::ec::Position position, emna::GraphicsDriver& graphics)
{
	emna::maths::point2f pos(position.x, position.y);
	emna::maths::point2f size(gh::ec::Ghost::Width, gh::ec::Ghost::Height);
	emna::maths::point2i texPos(0,0);
	emna::maths::point2i texSize(16,16);
	
	graphics.draw(pos, size, texPos, texSize, *m_spriteSheet);
}

void gh::view::ActorView::drawPlayer(gh::ec::Position position, emna::GraphicsDriver& graphics)
{
	emna::maths::point2f pos(position.x, position.y);
	emna::maths::point2f size(gh::ec::Player::Width, gh::ec::Player::Height);
	emna::maths::point2i texPos(16,0);
	emna::maths::point2i texSize(16,16);
	
	graphics.draw(pos, size, texPos, texSize, *m_spriteSheet);
}
