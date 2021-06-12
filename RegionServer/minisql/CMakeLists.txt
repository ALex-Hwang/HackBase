cmake_minimum_required(VERSION 3.17)
project(minisql)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(minisql
        API.cpp
        API.h
        Attribute.cpp
        Attribute.h
        BPlusTree.h
        BufferManager.cpp
        BufferManager.h
        CatalogManager.cpp
        CatalogManager.h
        Condition.cpp
        Condition.h
        IndexInfo.h
        IndexManager.cpp
        IndexManager.h
        Interpreter.cpp
        Interpreter.h
        main.cpp
        Minisql.h
        RecordManager.cpp
        RecordManager.h)
