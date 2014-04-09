##############################################
# Enable required C++11 flags on compilation #
##############################################

option(FORCE_CXX11_FLAG "Force the -std=c++11 flag to always be enabled" OFF)

if(FORCE_CXX11_FLAG)
	set(CMAKE_CXX_FLAGS "-std=c++11 ${CMAKE_CXX_FLAGS}")
elseif(MSVC)
	set(CMAKE_CXX_FLAGS "/MP ${CMAKE_CXX_FLAGS}")
elseif(CMAKE_COMPILER_IS_GNUCXX)
	execute_process(COMMAND ${CMAKE_CXX_COMPILER} -dumpversion OUTPUT_VARIABLE GXX_VERSION)
	if (GXX_VERSION VERSION_GREATER 4.6)
		if (GXX_VERSION VERSION_GREATER 4.7)
			set(CMAKE_CXX_FLAGS "-std=c++1y ${CMAKE_CXX_FLAGS}")
		else()
			set(CMAKE_CXX_FLAGS "-std=c++11 ${CMAKE_CXX_FLAGS}")
		endif()
	else()
		set(CMAKE_CXX_FLAGS "-std=c++0x ${CMAKE_CXX_FLAGS}")
	endif()
elseif ("${CMAKE_CXX_COMPILER} ${CMAKE_CXX_COMPILER_ARG1}" MATCHES ".*clang")
	set(CMAKE_CXX_FLAGS "-std=c++11 ${CMAKE_CXX_FLAGS}")
endif()
