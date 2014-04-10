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
			std::function<void (emna::GraphicsDriver& , gh::ec::Position)> m_drawGhost;
			std::function<void (emna::GraphicsDriver&, gh::ec::Position)> m_drawPlayer;
	
		public:
			ActorView(emna::ec::EntityComponentStore &entities,
			          std::function<void (emna::GraphicsDriver&, gh::ec::Position)> drawGhost,
					  std::function<void (emna::GraphicsDriver&, gh::ec::Position)> drawPlayer);
			
			virtual void draw(emna::GraphicsDriver& graphics);
		};
	}
}

#endif//GHOSTHUNT_VIEW_ACTORVIEW_H_INCLUDED
