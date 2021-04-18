// Kyle Cheng, kwc9ap, 11/10/20, huffmanenc.cpp

#include "HuffNode.h"
#include <iostream>
#include <fstream>
#include <cstdlib>
#include <unordered_map>
#include <string>
#include <cstring>
#include "heap.h"

using namespace std;

void getPrefixes(HuffNode* tree, string prefix);

unordered_map<char,string> codes;

int main(int argc, char** argv) {

  if (argc !=2){
    cout << "Must supply input file name as the one and only parameter" << endl;
    exit(1);
  }

  ifstream file(argv[1]);
  if (!file.is_open()){
    cout << "Unable to open '" << argv[1] << "' for reading" << endl;
    exit(2);
  }

  char g;
  unordered_map<char,int> dict;
  int totalSymbols =0;
  int distinctSymbols =0;
  while (file.get(g)){
    if(isprint(g)){
      if(dict.count(g) == 0){
	dict.insert({g, 0});
	distinctSymbols++;
      }
      totalSymbols++;
      dict[g] = dict[g]+1;
    }
  }
  /*
  for(auto& p : dict)
    cout<< "Frequency of " << p.first << " is " << p.second << endl;
    cout << "---------------------------" << endl;*/

  heap freqHeap = heap();
  for(auto& p : dict){
    HuffNode* x = new HuffNode(p.first, p.second);
    freqHeap.insert(x);
  }
  //freqHeap.print();
  while(!freqHeap.isEmpty() && freqHeap.size() != 1){
     HuffNode* y = new HuffNode();
     HuffNode* left = freqHeap.deleteMin();
     HuffNode* right = freqHeap.deleteMin();
     y->left = left;
     y->right = right;
     int newFreq = left->freq+right->freq;
     y->setFreq(newFreq);
     freqHeap.insert(y);
     /*
     cout << y->freq << " ";
     cout << freqHeap.size()<<endl;
     */
    }
  HuffNode* huffTree = freqHeap.deleteMin();
  getPrefixes(huffTree, "");
  int compressedBits =0;
  for(auto& p : codes){
    if(p.first == ' ')
      cout << "space " << p.second << endl;
    else{
    cout << p.first << " " << p.second << endl;
    }
  }
  cout << "----------------------------------------" << endl;
  file.clear();
  file.seekg(0);
  while(file.get(g)){
    cout << codes[g] << " ";
    compressedBits += codes[g].length();
  }
  cout << endl;
  cout << "----------------------------------------" << endl;
  cout << "There are a total of " << totalSymbols << " symbols that are encoded." << endl;
  cout << "There are " << distinctSymbols << " distinct symbols used." << endl;
  cout << "There were " << totalSymbols*8 << " bits in the original file." << endl;
  cout << "There were " << compressedBits << " bits in the compressed file." << endl;
  cout << "This gives a compression ratio of " << ((double)totalSymbols*8)/compressedBits << "." << endl;
  cout << "The cost of the Huffman tree is " << ((double)compressedBits)/totalSymbols << " bits per character." << endl;
  file.close();
  return 0;
}


void getPrefixes(HuffNode* tree, string prefix) {
  if(tree->left == nullptr && tree->right == nullptr){
    codes.insert({tree->val,prefix});
  }
  else{
    getPrefixes(tree->left, prefix+"0");
    getPrefixes(tree->right, prefix+"1");
  }
  
}
