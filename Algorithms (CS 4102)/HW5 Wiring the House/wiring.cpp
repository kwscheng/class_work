#include <iostream>
#include <map>
#include <string>
#include "findUnion.h"
#include "wireNode.h"
#include <queue>
#include <tuple>


using namespace std;

bool compareEdges(tuple<string, string, int> e1, tuple<string, string, int> e2){
    return  (get<2>(e1) < get<2>(e2)) || ((get<2>(e1) == get<2>(e2)) && get<1>(e1) < get<1>(e2)); 
}



int main(){
    /*
    wiring Kruskals:

        take in input
            J lines (nodes):
                - when switch s is listed, for light l that are listed next :
                    l.switch = s
                - add nodes to findUnion F, indexes in disjoint_set mapped to nodes

            C lines (edges):
            - if breaker to light, discard
            - if switch to switch, discard
            - if outlet/junction to light, discard
            - if edge is between light and light/switch where the required switch doesn't match discard
            - add edges to respective priority queues

        
        3 priorityQ
            - one for outlet/junction/BB
            - one for switches/lights
            - one for outlet/junction/BB to switch

        wire outlets and junctions to breaker box first
            - Kruskals on PQ1

        wire lights to switches separately until all lights and switches wired
            - only one switch in path, one switch as node
            - Kruskals on PQ2

        wire bb/outlets/junctions to lights/switch
            Kruskals on PQ3 

        while P is non-empty and F is not spanning
            pop edge of pQ;
            if edge connects two distinct trees:
                F.findSet 
                F.findSet
                F.unify(edge);

        
    */

   findUnion wiringF = findUnion();
   int j, c;
   cin >> j; 
   cin >> c;
   //cout << "j = " << j << endl;
   //cout << "c = " << c << endl;
   bool ignore = false;
   //bool breaker = false;
   for (int x = 0; x < j; x++){

        string name, type, reqswitch;
        cin >> name;
        cin >> type;
        /*
        if(type == "breaker"){
            if(!breaker){
                breaker = true;
            } else {
                continue;
            }
        }*/

        wireNode newNode = wireNode(name, type, "");

        wiringF.makeSet(newNode);

        if (type == "switch" && x < (j-1)  ){ //if type is switch, check for dependent lights. May need to check if x < j in if condition (switch is last junction and don't need to test for dependent lights)
        string name2, type2;
        ignore = false;
        do {
            ignore = false;
            cin >> name2;
            cin >> type2;
            if (type2 == "light"){ //if incoming node is a light, create new wireNode and "assign" it to correct switch
                //cout << " -- light " << name2 << " dependent on switch " << name  << " detected.";
                wireNode newLight = wireNode(name2,type2,name);
                wiringF.makeSet(newLight);
                x++;
            } else if(type2 == "switch"){
                name = name2;
                type = type2;
                ignore = true;
                wireNode newSwitch = wireNode(name2,type2,name);
                wiringF.makeSet(newSwitch);
                x++;
                continue;
            }

        
        } while ((type2 == "light" || ignore) && x < (j-1)); //might also need to check if x < j 
        if(x == (j-1) && type2 == "light"){
            //cout << "dont add light";
        } else {
        wireNode newNode2 = wireNode(name2,type2,"");
        wiringF.makeSet(newNode2);
        x++;
        }
       }
   }
    
    
    auto compareEdges = [](tuple<string, string, int> e1, tuple<string, string, int> e2){
        return  (get<2>(e1) > get<2>(e2)) || ((get<2>(e1) == get<2>(e2)) && get<1>(e1) > get<1>(e2)); 
    };
   
    priority_queue< tuple<string, string, int>, vector<tuple<string, string, int>>, decltype(compareEdges)> pqOJB(compareEdges);
    priority_queue< tuple<string, string, int>, vector<tuple<string, string, int>>, decltype(compareEdges)> pqSL(compareEdges);
    priority_queue< tuple<string, string, int>, vector<tuple<string, string, int>>, decltype(compareEdges)> pqFinal(compareEdges);

    for (int x = 0; x < c; x++){
        string nodeU, nodeV;
        int cost;
        cin >> nodeU;
        cin >> nodeV;
        cin >> cost;

        wireNode u = wiringF.nameToNode(nodeU);
        wireNode v = wiringF.nameToNode(nodeV);

        if(u.getType() == "breaker"){

            if(v.getType() == "box" || v.getType() == "outlet"){
                pqOJB.push(make_tuple(nodeU, nodeV, cost));
                
            } else if (v.getType() == "switch"){
                pqFinal.push(make_tuple(nodeU, nodeV, cost));

            } //dont do anything if edge is breaker - light

        } else if (u.getType() == "box" || u.getType() == "outlet"){

            if(v.getType() == "box" || v.getType() == "outlet"){
                pqOJB.push(make_tuple(nodeU, nodeV, cost));
            } else if (v.getType() == "switch"){
                pqFinal.push(make_tuple(nodeU, nodeV, cost));

            } else if (v.getType() == "breaker"){
                pqOJB.push(make_tuple(nodeU, nodeV, cost));
            } //do nothing if box/switch - light


        } else if (u.getType() == "switch"){

            if(v.getType() == "box" || v.getType() == "outlet"){
                pqFinal.push(make_tuple(nodeU, nodeV, cost));
            } else if (v.getType() == "switch"){
                // do nothing if switch - switch

            } else if (v.getType() == "light"){
                 if(nodeU == v.getSwitch()){
                    pqSL.push(make_tuple(nodeU, nodeV, cost));
                 }

            } else if (v.getType() == "breaker"){
                pqFinal.push(make_tuple(nodeU, nodeV, cost));
            }


        } else if (u.getType() == "light"){
            
            if (v.getType() == "switch"){
                if(u.getSwitch() == nodeV){
                    pqSL.push(make_tuple(nodeU, nodeV, cost));
                }
            } else if (v.getType() == "light"){
                 if(u.getSwitch() == v.getSwitch()){
                    pqSL.push(make_tuple(nodeU, nodeV, cost));
                }
            } 

        }


    }

    int edgesAccepted = 0;
    int totalCost = 0;

    while (!pqOJB.empty()){
        tuple<string, string, int> edge = pqOJB.top();
        //cout << get<0>(edge) << "--" << get<1>(edge) << ", cost: " << get<2>(edge) << endl;
        pqOJB.pop();
        if(wiringF.unify(wiringF.nameToIdx(get<0>(edge)), wiringF.nameToIdx(get<1>(edge)))){
            totalCost += get<2>(edge);
            //cout << "accepted" << endl;
        }
    }

    while (!pqSL.empty()){
        tuple<string, string, int> edge = pqSL.top();
        //cout << get<0>(edge) << "--" << get<1>(edge) << ", cost: " << get<2>(edge) << endl;
        pqSL.pop();
        if(wiringF.unify(wiringF.nameToIdx(get<0>(edge)), wiringF.nameToIdx(get<1>(edge)))){
            totalCost += get<2>(edge);
            //cout << "accepted" << endl;
        }
    }

    while (!pqFinal.empty()){
        tuple<string, string, int> edge = pqFinal.top();
        //cout << get<0>(edge) << "--" << get<1>(edge) << ", cost: " << get<2>(edge) << endl;
        pqFinal.pop();
        if(wiringF.unify(wiringF.nameToIdx(get<0>(edge)), wiringF.nameToIdx(get<1>(edge)))){
            totalCost += get<2>(edge);
            //cout << "accepted" << endl;
        }
    }

    cout << totalCost << endl;
}

