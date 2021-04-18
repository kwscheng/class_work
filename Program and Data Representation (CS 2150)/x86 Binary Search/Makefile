# Kyle Cheng, kwc9ap, 11/5/20, Makefile

CXX = clang++
CXXFLAGS = -Wall -g -O2

AS = nasm

ASFLAGS = -f elf64 -g

OBJECTS = binarySearch.o testBinarySearch.o timer.o

a.out: $(OBJECTS)
	$(CXX) $(CXXFLAGS) $(OBJECTS)
	
clean:
	-rm -f *.o *~
