// Kyle Cheng, kwc9ap, 11/10/20, heap.h
// Referenced code written by Aaron Bloomfield, 2014. 

#ifndef HEAP_H
#define HEAP_H

#include <vector>
#include "HuffNode.h"

using namespace std;

class heap {
public:
	heap();
	heap(vector<HuffNode*> vec);
	~heap();
	
	void insert(HuffNode* x);
	HuffNode* findMin();
	HuffNode* deleteMin();
	unsigned int size();
	void makeEmpty();
	bool isEmpty();
	void print();
	
private:
	vector<HuffNode*> huff_heap;
	unsigned int heap_size;
	
	void percolateUp(int hole);
	void percolateDown(int hole);
	friend class huffmanenc;
};

#endif

