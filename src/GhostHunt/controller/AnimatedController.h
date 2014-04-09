#ifndef GHOSTHUNT_CONTROLLER_ANIMATEDCONTROLLER_H_INCLUDED
#define GHOSTHUNT_CONTROLLER_ANIMATEDCONTROLLER_H_INCLUDED

#include <Emna/Emna.h>

namespace gh
{
	namespace controller
	{
		class AnimatedController : public emna::Controller
		{
		private:
			emna::ec::EntityComponentStore& m_entityComponentStore;
			
		public:
			AnimatedController(emna::ec::EntityComponentStore& entityComponentStore);
			
			virtual void update(std::chrono::microseconds deltaTime);
		};
	}
}

#endif//GHOSTHUNT_CONTROLLER_ANIMATEDCONTROLLER_H_INCLUDED
