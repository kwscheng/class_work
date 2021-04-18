//Kyle Cheng, kwc9ap, 11/27/20, vertex.h

#ifndef VERTEX_H
#define VERTEX_H

#include <list>
#include <string>

using namespace std;


class vertex{
	public:
	/**
	@brief Default constructor
	@details name field set to empty string, indegree set to 0, adjacent initialized as empty list of vertex pointers.
	*/
	vertex(); 
	/**
	@brief Constructor used 99% of the time. Same as default asides from name parameter
	@param n The name of the vertex
	*/
	vertex(string n);
	~vertex();
	string name; /** String representation of name */
	int indegree; /** Number of prerequisites */
	list<vertex*> adjacent; /** List of pointers to vertices that are pointed to by the current vertex */
	void addEdge(vertex* v); /** Add an edge between current node and parameter */
	void increment(); /** Increment indegree */
	void decrement(); /** Decrement indegree */ 
	
};

#endif
