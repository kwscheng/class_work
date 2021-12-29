#include "findUnion.h"
#include "wireNode.h"
#include <iostream>


using namespace std;

findUnion::findUnion(){
    idx = 0;
}

void findUnion::makeSet(wireNode n){

    dict[idx] = n;
    dict2[n.getName()] = idx;

    disjointSet.push_back(idx);

    idx++;
}
        
        
int findUnion::findSet(int i){
    if(disjointSet[i] == i){
        return i;
    } else {
        while(disjointSet[i] != i){
            i = disjointSet[i];
        }
        return i;
    }
}
// 
bool findUnion::unify(int i, int j){
    int setI = findSet(i);
    int setJ = findSet(j);
    if (setI != setJ){
        disjointSet[setJ] = setI;
        //cout << "unifying sets " << setI << " and " << setJ << endl; 
        return true;
    }
    return false;
} 

wireNode findUnion::idxToNode(int i){
    return dict[i];
}

int findUnion::nameToIdx(string name){
    return dict2[name];
}

wireNode findUnion::nameToNode(string name){
    return dict[dict2[name]];
}