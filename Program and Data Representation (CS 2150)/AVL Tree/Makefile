#Kyle Cheng, kwc9ap, 10/8/20, Makefile-postlab

CXX = clang++ $(CXXFLAGS)
CXXFLAGS = -Wall -O2

AVL: AVLNode.o AVLPathTest.o AVLTree.o
	$(CXX) AVLNode.o AVLPathTest.o AVLTree.o
	@echo "AVL compiled"

AVLNode.o: AVLNode.cpp AVLNode.h
AVLPathTest.o: AVLPathTest.cpp AVLTree.h AVLNode.h
AVLTree.o: AVLTree.cpp AVLNode.h AVLTree.h


.PHONY: clean
clean:
	-rm -f *.o BST
