// Kyle Cheng, kwc9ap, 11/10/20, heap.cpp
// References code written by Aaron Bloomfield, 2014

#include <iostream>
#include "heap.h"
using namespace std;

heap::heap() : heap_size() {
  huff_heap.push_back(nullptr);
}

heap::heap(vector<HuffNode*> vec) : heap_size(vec.size()){
  huff_heap = vec;
  huff_heap.push_back(huff_heap[0]);
  huff_heap[0] = nullptr;
  for (int i = heap_size/2; i > 0 ; i--) {
    percolateDown(i);
  }

}

heap::~heap(){
}

void heap::insert(HuffNode* x){
  huff_heap.push_back(x);
  percolateUp(++heap_size);
}

void heap::percolateUp(int hole){
  HuffNode* x = huff_heap[hole];
  int x_freq = x->freq;
  for( ; (hole > 1) && (x_freq < huff_heap[hole/2]->freq); hole /=2){
    huff_heap[hole] = huff_heap[hole/2];
  }
  huff_heap[hole] = x;
}

HuffNode* heap::deleteMin(){
  if (heap_size == 0){
    throw "deleteMin() called on empty heap";
  }
  HuffNode* ret = huff_heap[1];
  huff_heap[1] = huff_heap[heap_size--];
  huff_heap.pop_back();
  if (!isEmpty()){
    percolateDown(1);
  }
  return ret;
}

void heap::percolateDown(int hole) {
    // get the value to percolate down
    HuffNode* x = huff_heap[hole];
    // while there is a left child...
    while (hole*2 <= heap_size) {
        int child = hole*2; // the left child
        // is there a right child?  if so, is it lesser?
        if ((child+1 <= heap_size) && (huff_heap[child+1]->freq < huff_heap[child]->freq)) {
            child++;
        }
        // if the child is greater than the node...
        if (x->freq > huff_heap[child]->freq) {
            huff_heap[hole] = huff_heap[child]; // move child up
            hole = child;             // move hole down
        } else {
            break;
        }
    }
    // correct position found!  insert at that spot
    huff_heap[hole] = x;
}

HuffNode* heap::findMin(){
  if (heap_size == 0)
    throw "findMin() called on empty heap";
  return huff_heap[1];
}

unsigned int heap::size(){
  return heap_size;
}

void heap::makeEmpty(){
  heap_size = 0;
  huff_heap.resize(1);
}

bool heap::isEmpty(){
  return heap_size == 0;
}

void heap::print(){
  cout << "        ";
  for (int i = 1; i <= heap_size; i++){
    cout << huff_heap[i]->val << " ";
    bool isPow2 = (((i+1) & ~(i))==(i+1))? i+1 : 0;
    if(isPow2){
      cout << endl << "\t";
    }
  }
  cout << endl;
}
