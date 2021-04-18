//Kyle Cheng, kwc9ap, 11/10/20, HuffNode.h

#ifndef HUFFNODE_H
#define HUFFNODE_H

#include <iostream>
using namespace std;

class HuffNode {
public: 
	HuffNode();
	HuffNode(char v, int frq);
	~HuffNode();
	
	void setFreq(int frq);
	
	int freq;
	char val;
	HuffNode* left;
	HuffNode* right;
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
