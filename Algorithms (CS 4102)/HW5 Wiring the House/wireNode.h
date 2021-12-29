//Kyle Cheng, kwc9ap, Nov 2021, wireNode.h
#ifndef WIRENODE_H
#define WIRENODE_H
#include <string>
#include <map>
#include <vector>

using namespace std;

class wireNode {
    public:
        wireNode();
        wireNode(string x, string y, string z);
        string getName();
        string getType();
        string getSwitch();

    private:
        string name;
        string type;
        string req_switch; 
};

#endif
