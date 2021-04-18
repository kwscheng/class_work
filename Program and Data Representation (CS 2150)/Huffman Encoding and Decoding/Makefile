# Kyle Cheng, kwc9ap 11/12/20, Makefile

CXX = clang++
CXXFLAGS = -Wall -g

target: huffmanenc huffmandec

huffmanenc: HuffNode.o huffmanenc.o heap.o
	$(CXX) $(CXXFLAGS) -o encoder HuffNode.o huffmanenc.o heap.o
	@echo encoder ready

huffmandec: HuffNode2.o huffmandec.o
	$(CXX) $(CXXFLAGS) -o decoder HuffNode2.o huffmandec.o


clean:
	-rm -f *.o *~
