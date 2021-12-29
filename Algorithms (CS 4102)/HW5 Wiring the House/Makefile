all: findUnion.o wireNode.o
	g++ -std=c++11 -o a.out wiring.cpp findUnion.o wireNode.o

findUnion.o: findUnion.cpp findUnion.h
wireNode.o: wireNode.cpp wireNode.h 

run :	
	./a.out
