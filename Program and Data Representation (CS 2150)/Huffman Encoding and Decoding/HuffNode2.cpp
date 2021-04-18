// Kyle Cheng, kwc9ap, 11/10/20, HuffNode2.h

#include "HuffNode2.h"
#include <iostream>

using namespace std;

HuffNode2::HuffNode2(){
  val = '_';
  left = nullptr;
  right = nullptr; 
}

HuffNode2::HuffNode2(char v){
  val = v;
  left = nullptr;
  right = nullptr;
}

HuffNode2::~HuffNode2(){
}



