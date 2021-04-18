//Kyle Cheng, kwc9ap, 10/9/20, AVLTree.cpp
#include "AVLNode.h"
#include "AVLTree.h"
#include <iostream>
#include <string>
using namespace std;

AVLTree::AVLTree() {
    root = NULL;
    count = 0;
}

AVLTree::~AVLTree() {
    delete root;
    root = NULL;
}

// insert finds a position for x in the tree and places it there, rebalancing
// as necessary.
void AVLTree::insert(const string& x) {
  insert(root, x);
}

void AVLTree::insert(AVLNode*& n, const string& x) { //need to readjust heights
   if(!find(x)){
    if (n == NULL){
    n = new AVLNode();
    n->value = x;
    n->height = 1;
    count++;
    }
    else if (n->value > x){
    insert(n->left, x);
    n->height = 1 + max(height(n->left), height(n->right));
    balance(n);
    }
    else if (n->value < x){
    insert(n->right, x);
    n->height = 1 + max(height(n->left), height(n->right));
    balance(n);
    }
  else{
  }
  }
}

// remove finds x's position in the tree and removes it, rebalancing as
// necessary.
void AVLTree::remove(const string& x) {
    root = remove(root, x);
}

string AVLTree::pathTo(const string& x) const {
  if(!find(x))
    return "";
  string ans = pathTo(root,x);
  return ans;
}

string AVLTree::pathTo(const AVLNode* tree, const string& x) const{
  if (tree->value == x)
    return " "+tree->value;
  else if(tree->value<x)
    return " " + tree->value+" "+pathTo(tree->right,x);
  else if (tree->value > x)
    return " "+tree->value+" "+pathTo(tree->left,x);
  else
    return "";
}

// find determines whether or not x exists in the tree.
bool AVLTree::find(const string& x) const {
  bool ans = find(root ,x);
  return ans;
}

bool AVLTree::find(const AVLNode* tree, const string& x) const {
  if (tree == NULL)
    return false;
  if (tree->value == x)
    return true;
  else if (tree->value < x)
    return find(tree->right,x);
  else if(tree->value > x)
    return find(tree->left,x);
  else
    return false;
}

int AVLTree::numNodes() const {
  return count;
}

// balance makes sure that the subtree with root n maintains the AVL tree
// property, namely that the balance factor of n is either -1, 0, or 1.
void AVLTree::balance(AVLNode*& n) {
  int balanceFac;
  if( (n->right == NULL) && (n->left == NULL) )
    balanceFac = 0;
  else if (n->right == NULL)
    balanceFac = -(n->left->height);
  else if (n->left == NULL)
    balanceFac = n->right->height;
  else
    balanceFac = (n->right->height)-(n->left->height);
  if (balanceFac == 2){ //adjust height 
    if (height(n->right->right)-height(n->right->left) < 0)
      n->right = rotateRight(n->right);
    n = rotateLeft(n);
  }
  else if (balanceFac == -2){ //adjust height
    if (height(n->left->right)-height(n->left->left) > 0)
      n->left = rotateLeft(n->left);
    n = rotateRight(n);
  }
  else{
  }
  
}

// rotateLeft performs a single rotation on node n with its right child.
AVLNode* AVLTree::rotateLeft(AVLNode*& n) {
  AVLNode* nRightPoint = n->right;
  n->right = n->right->left;
  nRightPoint->left = n;
  if (n->left == NULL && n->right == NULL)
    n->height = 1;
  else
    n->height = 1 + max(height(n->left), height(n->right));
  nRightPoint->height = 1 + max(height(nRightPoint->left),height(nRightPoint->right));
  return nRightPoint;
}

// rotateRight performs a single rotation on node n with its left child.
AVLNode* AVLTree::rotateRight(AVLNode*& n) {
  AVLNode* nLeftPoint = n->left;
  n->left = n->left->right;
  nLeftPoint->right = n; //invalid address...double rotation?
  if (n->left == NULL && n->right == NULL)
    n->height = 1;
  else
    n->height = 1 + max(height(n->left), height(n->right));
  nLeftPoint->height = 1 + max(height(nLeftPoint->left),height(nLeftPoint->right));
  return nLeftPoint;
}

// private helper for remove to allow recursion over different nodes.
// Returns an AVLNode* that is assigned to the original node.
AVLNode* AVLTree::remove(AVLNode*& n, const string& x) {
    if (n == NULL) {
        return NULL;
    }

    // first look for x
    if (x == n->value) {
        // found
        if (n->left == NULL && n->right == NULL) {
            // no children
            delete n;
            n = NULL;
	    count--;
            return NULL;
        } else if (n->left == NULL) {
            // Single child (left)
            AVLNode* temp = n->right;
            n->right = NULL;
            delete n;
            n = NULL;
	    count--;
            return temp;
        } else if (n->right == NULL) {
            // Single child (right)
            AVLNode* temp = n->left;
            n->left = NULL;
            delete n;
            n = NULL;
	    count--;
            return temp;
        } else {
            // two children -- tree may become unbalanced after deleting n
            string sr = min(n->right);
            n->value = sr;
            n->right = remove(n->right, sr);
        }
    } else if (x < n->value) {
        n->left = remove(n->left, x);
    } else {
        n->right = remove(n->right, x);
    }

    // Recalculate heights and balance this subtree
    n->height = 1 + max(height(n->left), height(n->right));
    balance(n);

    return n;
}

// min finds the string with the smallest value in a subtree.
string AVLTree::min(AVLNode* node) const {
    // go to bottom-left node
    if (node->left == NULL) {
        return node->value;
    }
    return min(node->left);
}

// height returns the value of the height field in a node.
// If the node is null, it returns -1.
int AVLTree::height(AVLNode* node) const {
    if (node == NULL) {
        return -1;
    }
    return node->height;
}

// max returns the greater of two integers.
int max(int a, int b) {
    if (a > b) {
        return a;
    }
    return b;
}

// Helper function to print branches of the binary tree
// You do not need to know how this function works.
void showTrunks(Trunk* p) {
    if (p == NULL) return;
    showTrunks(p->prev);
    cout << p->str;
}

// Recursive function to print binary tree
// It uses inorder traversal
void AVLTree::printTree(AVLNode* root, Trunk* prev, bool isRight) {
    if (root == NULL) return;

    string prev_str = "    ";
    Trunk* trunk = new Trunk(prev, prev_str);

    printTree(root->right, trunk, true);

    if (!prev)
        trunk->str = "---";
    else if (isRight) {
        trunk->str = ".---";
        prev_str = "   |";
    } else {
        trunk->str = "`---";
        prev->str = prev_str;
    }

    showTrunks(trunk);
    cout << root->value << endl;

    if (prev) prev->str = prev_str;
    trunk->str = "   |";

    printTree(root->left, trunk, false);

    delete trunk;
}

void AVLTree::printTree() {
    printTree(root, NULL, false);
}
