include(${GHOSTHUNT_SOURCE_DIR}/src/files.cmake)
include(${GHOSTHUNT_SOURCE_DIR}/src/dependencies.cmake)
include(${GHOSTHUNT_SOURCE_DIR}/test/files.cmake)
include(${GHOSTHUNT_SOURCE_DIR}/test/dependencies.cmake)

include_directories(${GHOSTHUNT_SOURCE_DIR}/src)
link_directories(${CMAKE_BINARY_DIR}/src)

add_executable(ghosthunt_test ${GHOSTHUNT_SRC_FILES} ${GHOSTHUNT_TEST_FILES})
target_link_libraries(ghosthunt_test ${GHOSTHUNT_SRC_DEPENDENCIES} ${GHOSTHUNT_TEST_DEPENDENCIES})

add_test(unit ghosthunt_test)

install(TARGETS ghosthunt_test RUNTIME DESTINATION test)
