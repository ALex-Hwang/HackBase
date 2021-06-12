cmake_minimum_required(VERSION 3.16)
project(regionServer)

set(CMAKE_CXX_STANDARD 17)

aux_source_directory(./minisql DIR_SRCS)


add_executable(demo
        origin.cpp
        ${DIR_SRCS}
        )


