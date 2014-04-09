#ifndef GHOSTHUNT_VIEW_ACTORVIEW_H_INCLUDED
#define GHOSTHUNT_VIEW_ACTORVIEW_H_INCLUDED

#include <Emna/Emna.h>
#include "../ec/Position.h"

namespace gh
{
	namespace view
	{
		class ActorView : public emna::View
		{
		private:
			emna::ec::EntityComponentStore &m_entities;
			std::unique_ptr<emna::content::Texture> m_spriteSheet;
	
		public:
			ActorView(emna::ec::EntityComponentStore &entities, emna::ContentFactory &content);
			
			virtual void draw(emna::GraphicsDriver& graphics);
			
		private:
			void drawGhost(gh::ec::Position position, emna::GraphicsDriver& graphics);
			void drawPlayer(gh::ec::Position position, emna::GraphicsDriver& graphics);
		};
	}
}

#endif//GHOSTHUNT_VIEW_ACTORVIEW_H_INCLUDED
