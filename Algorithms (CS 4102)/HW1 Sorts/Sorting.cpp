#include <iostream>
#include <vector>

using namespace std;

void swapList(vector<int> &list, int i, int j);

void insertionSort(vector<int> &list, int start, int end) {
	//TODO: IMPLEMENT INSERTION SORT
	for(int i = start+1; i<=end; i++ ){
		int j = i; 
		int focus = list[j];
		//cout << focus << endl;
		while(focus < list[j-1] && j > start){
			list[j] = list[j-1];
			j--;
		}
		list[j] = focus;
	}
}

int partition(vector<int> &list, int i, int j) { //Lomuto's partition
	int ind = rand() % (j-i) + i; //generate random index in sublist
	int pivot = list[ind];
	//int pivot = list[j];
	swapList(list, ind, j); //place random pivot in last index

	int pos = i-1; //pos = correct position of pivot 

	for(int x = i; x < j; x++){ //iterate through list until end-1
	
		if(list[x] < pivot){ //if current element is smaller than pivot,
			pos++; //increment pos (adding more space to "sorted area")
			swapList(list, pos, x); //swap "correct" position with current element		
		}

	}
	swapList(list, pos+1, j); //put pivot in correct position, everything lower is smaller, everything greater is higher
	return pos+1;
}

void swapList(vector<int> &list, int i, int j){
	int temp = list[i];
	list[i] = list[j];
	list[j] = temp;
}

void quickSort(vector<int> &list, int i, int j, int minSize) {
	//TODO: IMPLEMENT QUICKSORT
	if(minSize > 1 && (j-i <= minSize )){
		insertionSort(list, i, j);
		return;
	}
	if(j <= i){
		
	} else {
		int q = partition(list, i, j);
		//cout << q << endl;
		//cout << "line 57" << endl;
		quickSort(list, i, q-1,minSize);
		quickSort(list, q+1 , j,minSize);
	}
}