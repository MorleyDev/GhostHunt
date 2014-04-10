#include "GhostRenderingService.h"
#include "../ec/Ghost.h"

gh::service::GhostRenderingService::GhostRenderingService(emna::ContentFactory& contentFactory)
: m_spriteSheet(contentFactory.loadTexture("resources/spritesheet.png"))
{
}

void gh::service::GhostRenderingService::operator()(emna::GraphicsDriver& graphics, const gh::ec::Position& position) const
{
	emna::maths::point2f pos(position.x, position.y);
	emna::maths::point2f size(gh::ec::Ghost::Width, gh::ec::Ghost::Height);
	emna::maths::point2i texPos(16,0);
	emna::maths::point2i texSize(16,16);
	
	graphics.draw(pos, size, texPos, texSize, *m_spriteSheet);
}
