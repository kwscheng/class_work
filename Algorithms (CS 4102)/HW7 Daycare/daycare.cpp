#include <iostream>
#include <tuple>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;


/*
    Greedy choice comparison for rooms with increasing capacity, start with rooms with smallest initial capacity
*/
bool compareInc(tuple<int, int> i,tuple<int, int> j){ 
    return (get<0>(i) < get<0>(j)) || ((get<0>(i) == get<0>(j)) && (get<1>(i) < get<1>(i))); 
}

/*
    Greedy choice comparison for rooms with decreasing capacity, start with rooms with smallest decrease in available space
*/


bool compareDec(tuple<int, int> i,tuple<int, int> j){ 
    return (get<1>(i) > get<1>(j)) || ((get<1>(i) == get<1>(j)) && (get<0>(i) == get<0>(j)));
    //return (get<0>(i)-get<1>(i)) < (get<0>(j)-get<1>(j)) || ((get<0>(i)-get<1>(i)) == (get<0>(j)-get<1>(j)) && (get<0>(i) > get<0>(j))); 
}

/*
    Sorts rooms with increasing capacity by the greedy choice 
*/
void sortInc(vector<tuple<int,int>> &rooms){ //probably don't need to return, rooms should get sorted anyway, passed by reference
    sort(rooms.begin(), rooms.end(), compareInc);
}

/*
    Sorts rooms with decreasing capacity by the greedy choice 
*/  
void sortDec(vector<tuple<int,int>> &rooms){
    sort(rooms.begin(), rooms.end(), compareDec);
}


int capacityToPurchase(vector<tuple<int,int>> &incRooms, vector<tuple<int,int>> &staticRooms, vector<tuple<int,int>> &decRooms){
    sortInc(incRooms);
    sortDec(decRooms);
    int trailerSpace = 0;
    int roomSpace = 0; //total available space = trailerSpace + roomSpace

    for(auto x : incRooms){
        if ((roomSpace+trailerSpace) < get<0>(x)){
            trailerSpace = get<0>(x)-roomSpace;
            //cout << "Insufficient space (INC): trailerSpace is now " << to_string(trailerSpace) << endl;
        }
        roomSpace += get<1>(x)-get<0>(x);
    }

    for(auto x : staticRooms){
        if ((roomSpace+trailerSpace) < get<0>(x)){
            trailerSpace = get<0>(x)-roomSpace;
            //cout << "Insufficient space (STATIC): trailerSpace is now " << to_string(trailerSpace) << endl;
        } 
        // get<1>(x)-get<0>(x) should == 0 
    }

    for(auto x : decRooms){
        //cout << get<0>(x) << " to " << get<1>(x) << endl;
        if ((roomSpace+trailerSpace) < get<0>(x)){
            trailerSpace = get<0>(x)-roomSpace;
            //cout << "Insufficient space (DEC): trailerSpace is now " << to_string(trailerSpace) << endl;
        }
        roomSpace += get<1>(x)-get<0>(x);
    }

    return trailerSpace;
}

int main(){
    int numRooms;
    vector<int> output;
    while(cin >> numRooms){
        //cin >> numRooms;
        vector<tuple<int,int>> incRooms;
        vector<tuple<int,int>> staticRooms;
        vector<tuple<int,int>> decRooms;
        for(int i = 0; i < numRooms; i++){
            int before, after; 
            cin >> before;
            cin >> after;
            //before = stoi(before);
            //after = stoi(after);
            if((after-before) > 0 ){
                incRooms.push_back(make_tuple(before,after));
            } else if ((after-before) == 0){
                staticRooms.push_back(make_tuple(before,after));
            } else if ((after-before) < 0){
                decRooms.push_back(make_tuple(before,after));
            }
        }

        output.push_back(capacityToPurchase(incRooms,staticRooms,decRooms));

    }

    for(auto x : output){
        cout << to_string(x) << endl;
    }

    return 0;
}