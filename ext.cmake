#########################################################
# Utilise the zander system to acquire the dependencies #
# Parameters:
#	List[String] - ZANDER_DEPENDENCIES
#		The list of library names to acquire via zander
#########################################################

if (UNIX)
option(ENABLE_ZANDER "Use zander to install the dependencies" OFF)
else()
option(ENABLE_ZANDER "Use zander to install the dependencies" ON)
endif()

if (ENABLE_ZANDER)
	set(ZANDER_EXT_DIRECTORY "${CMAKE_BINARY_DIR}/ext" CACHE FILEPATH "Path to external includes")
	
	string(TOLOWER "${CMAKE_BUILD_TYPE}" CMAKE_BUILD_TYPE_LOWER)
	if(CMAKE_BUILD_TYPE_LOWER STREQUAL "debug")
		set(ZANDER_BUILD_TYPE "debug" CACHE STRING "Build Type")
	else()
		set(ZANDER_BUILD_TYPE "release" CACHE STRING "Build Type")
	endif()
	
	if(MSVC)
		set(ZANDER_COMPILER "msvc12")
	elseif(CMAKE_COMPILER_IS_GNUCXX OR "${CMAKE_CXX_COMPILER} ${CMAKE_CXX_COMPILER_ARG1}" MATCHES ".*clang")
		set(ZANDER_COMPILER "gnu")
	elseif(EMSCRIPTEN)
		set(ZANDER_COMPILER "gnu")
	else()
		set(ZANDER_UNKNOWN_COMPILER ON)
	endif()
	
	if(NOT ZANDER_UNKNOWN_COMPILER)
		message("Working Directory: ${ZANDER_EXT_DIRECTORY}")
		file(MAKE_DIRECTORY ${ZANDER_EXT_DIRECTORY})
		
		message("Getting dependencies: ${ZANDER_DEPENDENCIES}")
		foreach(ZANDER_DEPENDENCY ${ZANDER_DEPENDENCIES})
			message("Getting dependency: ${ZANDER_DEPENDENCY}")
			execute_process(COMMAND cmd /c zander get ${ZANDER_DEPENDENCY} ${ZANDER_COMPILER} ${ZANDER_BUILD_TYPE} WORKING_DIRECTORY ${ZANDER_EXT_DIRECTORY})
		endforeach(ZANDER_DEPENDENCY)

	endif()

	include_directories(${ZANDER_EXT_DIRECTORY}/include)
	link_directories(${ZANDER_EXT_DIRECTORY}/lib)
	
else()
	set(ZANDER_EXT_DIRECTORY "${CMAKE_BINARY_DIR}/ext" CACHE FILEPATH "Path to external includes")
	include_directories(${ZANDER_EXT_DIRECTORY}/include)
	link_directories(${ZANDER_EXT_DIRECTORY}/lib)
endif()
