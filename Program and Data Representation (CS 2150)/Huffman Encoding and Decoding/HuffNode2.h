//Kyle Cheng, kwc9ap, 11/10/20, HuffNode2.h

#ifndef HUFFNODE2_H
#define HUFFNODE2_H

#include <iostream>
using namespace std;

class HuffNode2 {
public: 
	HuffNode2();
	HuffNode2(char v);
	~HuffNode2();
	
	
	
	char val;
	HuffNode2* left;
	HuffNode2* right;
private:
/*
	friend class heap;
	friend class huffmanenc;
	int freq;
	char val;
	HuffNode* left; //need to modify code in heap...? change left and right pointers when percolating
	HuffNode* right;*/
};

#endif
