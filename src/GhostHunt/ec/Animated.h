#ifndef GHOSTHUNT_EC_ANIMATED_H_INCLUDED
#define GHOSTHUNT_EC_ANIMATED_H_INCLUDED

#include <chrono>

namespace gh
{
	namespace ec
	{
		struct Animated
		{
			std::chrono::microseconds runningTime;
		};
	}
}

#endif//GHOSTHUNT_EC_ANIMATED_H_INCLUDED
