# Kyle Cheng, kwc9ap, 11/17/20, Makefile

CXX = clang++ -std=c++11
CXXFLAGS = -Wall -g

topological: middleearth.o vertex.o topological.o
	$(CXX) $(CXXFLAGS) middleearth.o vertex.o topological.o
	doxygen
	
clean:
	-rm -f *.o *~
