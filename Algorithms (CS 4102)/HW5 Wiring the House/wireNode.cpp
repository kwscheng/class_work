#include "wireNode.h"

using namespace std;

wireNode::wireNode(){
    name = "";
    type = "";
    req_switch = "";
}

// x = name, y = type, z = switch
wireNode::wireNode(string x, string y, string z){
    name = x;
    type = y;
    req_switch = z;
}

string wireNode::getName(){
    return name;
}

string wireNode::getType(){
    return type;
}

string wireNode::getSwitch(){
    return req_switch;
}
