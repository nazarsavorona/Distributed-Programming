cmake_minimum_required(VERSION 3.20)
project(lab6)

set(CMAKE_CXX_STANDARD 23)

add_executable(lab6 main.cpp)

find_package(MPI REQUIRED)
if (MPI_FOUND)
    include_directories(SYSTEM ${MPI_INCLUDE_PATH})
else (MPI_FOUND)
    message(SEND_ERROR "This application cannot compile without MPI")
endif (MPI_FOUND)

target_link_libraries(lab6 ${MPI_LIBRARIES})