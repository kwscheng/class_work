//Kyle Cheng, kwc9ap, 11/17/20, topological.cpp

#include <iostream>
#include <fstream>
#include <cstdlib>
#include <deque>
#include <algorithm>
#include <queue>
#include <list>
#include "vertex.h"
#include <unordered_map>

using namespace std;

void topsort();

priority_queue<string, vector<string>, greater<string>> indegreeZero; /** Priority queue that automatically sorts and holds vertices with an indegree of 0 */

unordered_map<string, vertex> vertices; /** An unordered map holding class name and analagous vertex*/

/**
Topologically sorts the command-line parameter file
 */
int main(int argc, char** argv){

  if (argc != 2){
    cout << "Must supply the input file name as the one and only parameter" << endl;
    exit(1);
  }
  ifstream file(argv[1]);
  if (!file.is_open()){
    cout << "Unable to open '" << argv[1] << "' for reading" << endl;
    exit(2);
}
  string v,w;
  vertex* wPoint;
  while(v != "0" && w != "0"){
    file >> v;
    file >> w;
    //cout << v << " " << w << endl;
    if(vertices.count(v) == 0){
      vertex one = vertex(v);
      vertices.insert({v,one});
    }
    if(vertices.count(w) == 0){
      vertex one = vertex(w);
      vertices.insert({w,one});
    }
    wPoint = &vertices[w];
    vertices[v].addEdge(wPoint);
    vertices[w].increment();
  }
  //cout << vertices["cs4414"].indegree << endl;
  topsort();
  cout << endl;

  }
/**
@brief Performs a topological sort on the given command-line parameter
*/
void topsort(){
  string vert;
  for(auto& v : vertices){
    if (v.second.indegree == 0){
      indegreeZero.push(v.first);
      //cout << v.first << endl;
    }
  }
  while(!indegreeZero.empty()){
    vert = indegreeZero.top();
    indegreeZero.pop();
    cout << vert << " ";
    for(auto& t : vertices[vert].adjacent){
      //cout << t->name << endl;
      t->decrement();
      if(t->indegree == 0)
	indegreeZero.push(t->name);
    }
  }
  
}
