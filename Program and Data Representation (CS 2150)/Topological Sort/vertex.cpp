//Kyle Cheng, kwc9ap, 11/27/20, vertex.cpp

#include "vertex.h"

using namespace std;

vertex::vertex(){
  name = "";
  indegree = 0;
  adjacent = {};


}
vertex::vertex(string n){
  name = n;
  indegree = 0;
  adjacent = {};
}

vertex::~vertex(){

}

void vertex::addEdge(vertex* v){
  adjacent.push_back(v);
}

void vertex::increment(){
  indegree++;
}

void vertex::decrement(){
  indegree--;
}
