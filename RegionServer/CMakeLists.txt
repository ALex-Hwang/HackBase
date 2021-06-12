cmake_minimum_required(VERSION 3.16)
project(regionServer)

set(CMAKE_CXX_STANDARD 17)

aux_source_directory(. DIR_SRCS)

add_subdirectory(minisql)

add_executable(test
        main.cpp
        )

      target_link_libraries(test minisqlDependency)

