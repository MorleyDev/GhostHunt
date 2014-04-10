#ifndef GHOSTHUNT_SERVICE_GHOSTRENDERINGSERVICE_H_INCLUDED
#define GHOSTHUNT_SERVICE_GHOSTRENDERINGSERVICE_H_INCLUDED

#include <Emna/Emna.h>
#include "../ec/Position.h"

namespace gh
{
	namespace service
	{
		class GhostRenderingService
		{
		private:
			std::unique_ptr<emna::content::Texture> m_spriteSheet;
			
		public:
			GhostRenderingService(emna::ContentFactory& contentFactory);
		
			void operator()(emna::GraphicsDriver& graphics, const gh::ec::Position& position) const;
		};
	}
}

#endif//GHOSTHUNT_SERVICE_GHOSTRENDERINGSERVICE_H_INCLUDED
