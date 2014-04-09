#ifndef GHOSTHUNT_EC_GHOST_H_INCLUDED
#define GHOSTHUNT_EC_GHOST_H_INCLUDED

namespace gh
{
	namespace ec
	{
		struct Ghost
		{
			enum class Id
			{
				Adam,
				Brain,
				Cleo
			};
			Id id;
			
			static constexpr float Width = 16.0f;
			static constexpr float Height = 16.0f;
		};
	}
}

#endif//GHOSTHUNT_EC_GHOST_H_INCLUDED
