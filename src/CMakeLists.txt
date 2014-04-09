include(${GHOSTHUNT_SOURCE_DIR}/src/files.cmake)
include(${GHOSTHUNT_SOURCE_DIR}/src/dependencies.cmake)

add_executable(ghost_hunt ${GHOSTHUNT_MAIN_FILE} ${GHOSTHUNT_SRC_FILES})
target_link_libraries(ghost_hunt ${GHOSTHUNT_SRC_DEPENDENCIES})

install(TARGETS ghost_hunt RUNTIME DESTINATION bin LIBRARY DESTINATION bin ARCHIVE DESTINATION lib)
