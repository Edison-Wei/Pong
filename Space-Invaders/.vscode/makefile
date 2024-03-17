all: Program

Program: Program.o main.o
	g++ main.o -o main.exe -L/opt/homebrew/Cellar/glfw/3.4/lib -Lglew-2.2.0/lib -lglfw -lglew -framework OpenGL 

Program.o: main.cpp
	g++ -c main.cpp -I/opt/homebrew/Cellar/glfw/3.4/include -Iglew-2.2.0/include