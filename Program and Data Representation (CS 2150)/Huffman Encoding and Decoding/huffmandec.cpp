// Kyle Cheng, kwc9ap, 10/11/20, huffmandec.cpp

#include <iostream>
#include <fstream>
#include <sstream>
#include <cstdlib>
#include <unordered_map>
#include <string>
#include "HuffNode2.h"

using namespace std;

unordered_map< string, char> codes;
HuffNode2* huffTree;
void decode(HuffNode2* tree, string prefix);
void buildTree(HuffNode2* tree, char leaf, string prefix);


// main(): we want to use parameters
int main (int argc, char** argv) {
    // verify the correct number of parameters
    if (argc != 2) {
        cout << "Must supply the input file name as the only parameter" << endl;
        exit(1);
    }

    // attempt to open the supplied file
    // must be opened in binary mode as otherwise trailing whitespace is discarded
    ifstream file(argv[1], ifstream::binary);
    // report any problems opening the file and then exit
    if (!file.is_open()) {
        cout << "Unable to open file '" << argv[1] << "'." << endl;
        exit(2);
    }

    // read in the first section of the file: the prefix codes
    while (true) {
      string character, prefix;
      // read in the first token on the line
      file >> character;
      
      // did we hit the separator?
      if (character[0] == '-' && character.length() > 1) {
	break;
        }

        // check for space
        if (character == "space") {
            character = " ";
        }
	char x = character[0];
        // read in the prefix code
        file >> prefix;
	codes.insert({prefix,x});
        // do something with the prefix code
        //cout << "character '" << character << "' has prefix code '" << prefix << "'" << endl;
    }
    huffTree = new HuffNode2();
    for(auto& p : codes){
      buildTree(huffTree, p.second, p.first);
    }
    //cout << huffTree->left->val << " " << huffTree->right->val << endl;
    //cout << huffTree->right->left->left->val << " " << huffTree->right->right->val << endl;
    // read in the second section of the file: the encoded message
  
    stringstream sstm;
    while (true) {
        string bits;
        // read in the next set of 1's and 0's
        file >> bits;
        // check for the separator
        if (bits[0] == '-') {
            break;
        }
        // add it to the stringstream
        sstm << bits;
    }
    string allbits = sstm.str();
    // at this point, all the bits are in the 'allbits' string
    decode(huffTree, allbits);
    cout << endl;
    //cout << "All the bits: " << allbits << endl;
    // close the file before exiting
    file.close();
}

void decode(HuffNode2* tree, string prefix){
  if(tree->left == nullptr && tree->right == nullptr){
    cout << tree->val;
    if(prefix.size() != 0)
      decode(huffTree, prefix);
  }
  else if(prefix.front() == '0')
    decode(tree->left, prefix.substr(1));
  else if(prefix.front() == '1')
    decode(tree->right, prefix.substr(1));
}

void buildTree(HuffNode2* tree, char leaf, string prefix){
  if (prefix == ""){
    tree->val = leaf;
  }
  else{
    if(prefix[0] == '0'){
      if(tree->left == nullptr)
	 tree->left = new HuffNode2();
      buildTree(tree->left, leaf, prefix.substr(1));
    }
    if(prefix[0] == '1'){
      if(tree->right == nullptr)
	tree->right = new HuffNode2();
      buildTree(tree->right, leaf, prefix.substr(1));
    }
    
    }

}

 
