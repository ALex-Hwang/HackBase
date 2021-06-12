cmake_minimum_required(VERSION 3.16)
project(RegionServer)

set(CMAKE_CXX_STANDARD 14)


aux_source_directory(. DIR_SRC)

add_subdirectory(minisql)

add_executable(demo
        main.cpp)

target_link_libraries(demo minisql)
