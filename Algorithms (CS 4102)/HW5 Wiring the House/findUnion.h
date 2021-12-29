//Kyle Cheng, kwc9ap, Nov 2021, findUnion.h
#ifndef FINDUNION_H
#define FINDUNION_H
#include <string>
#include <unordered_map>
#include <vector>
#include "wireNode.h"

using namespace std;

class findUnion {
    public:
        findUnion();
        void makeSet(wireNode n);
        int findSet(int i);
        bool unify(int i, int j); 
        wireNode idxToNode(int i); //get wireNode from disjointSet idx
        int nameToIdx(string name); //get disjointSet idx from node name
        wireNode nameToNode(string name); //get wireNode from node name
    private:
        vector<int> disjointSet; //at start, every vertex is a separate tree. dsjSet holds set num
        unordered_map<int, wireNode> dict; //maps indices of disjointSet to nodes 
        unordered_map<string, int> dict2; //maps node name to indices of disjointSet
        int idx;

};

#endif
