// Kyle Cheng, kwc9ap, 11/10/20, heap.h

#include "HuffNode.h"
#include <iostream>

using namespace std;

HuffNode::HuffNode(){
  freq = 0;
  val = '_';
  left = nullptr;
  right = nullptr; 
}

HuffNode::HuffNode(char v, int frq){
  freq = frq;
  val = v;
  left = nullptr;
  right = nullptr;
}

HuffNode::~HuffNode(){
}

void HuffNode::setFreq(int frq){
  freq = frq;
}


