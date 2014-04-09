cmake_minimum_required (VERSION 2.6) 
project(GHOSTHUNT CXX) 

option(ENABLE_TESTS "Enable usage of CTest" ON)
option(BUILD_UNIT_TESTS "Build the unit tests" ON)
option(COMPILE_RESOURCES "Compile the resources" ON)
option(ENABLE_DEBUG "Enable debug behaviours" OFF)

include(cxx11.cmake)

if(MSVC)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /bigobj")
endif()

set(ZANDER_DEPENDENCIES bandit emna)
include(ext.cmake)
include_directories(ext/include)

if (ENABLE_DEBUG)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DEMNA_DEBUG")
endif()

add_subdirectory(src)

if (ENABLE_TESTS)
	enable_testing()
endif()

if (BUILD_UNIT_TESTS)
	add_subdirectory(test)
endif()

install(DIRECTORY test/ DESTINATION test/src FILES_MATCHING PATTERN "*.cpp")
install(DIRECTORY test/ DESTINATION test/src FILES_MATCHING PATTERN "*.h")

if (ENABLE_ZANDER)
	install(DIRECTORY ${ZANDER_EXT_DIRECTORY}/bin/ DESTINATION bin)
	install(DIRECTORY ${ZANDER_EXT_DIRECTORY}/bin/ DESTINATION test)
endif()
