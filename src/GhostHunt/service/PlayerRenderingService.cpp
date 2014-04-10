#include "PlayerRenderingService.h"
#include "../ec/Player.h"

gh::service::PlayerRenderingService::PlayerRenderingService(emna::ContentFactory& contentFactory)
: m_spriteSheet(contentFactory.loadTexture("resources/spritesheet.png"))
{
}

void gh::service::PlayerRenderingService::operator()(emna::GraphicsDriver& graphics, const gh::ec::Position& position) const
{
	emna::maths::point2f pos(position.x, position.y);
	emna::maths::point2f size(gh::ec::Player::Width, gh::ec::Player::Height);
	emna::maths::point2i texPos(0,0);
	emna::maths::point2i texSize(16,16);
	
	graphics.draw(pos, size, texPos, texSize, *m_spriteSheet);
}
