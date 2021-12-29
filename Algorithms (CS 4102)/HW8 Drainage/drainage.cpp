#include <iostream>
#include <tuple>
#include <vector>
#include <unordered_map>
#include <string>
#include <algorithm>
#include <stdexcept>  


using namespace std;

bool compareElevation(tuple<int,int,int> i, tuple<int,int,int> j){
    return get<0>(i) < get<0>(j) || ((get<0>(i) == get<0>(j)) && (get<1>(i) < get<1>(j))) || ((get<0>(i) == get<0>(j)) && (get<1>(i) == get<1>(j)) && (get<2>(i) < get<2>(j)));
}

void sortGrid(vector< tuple<int,int,int>> &grid){
    //cout << "sorting!" << endl;
    sort(grid.begin(),grid.end(), compareElevation);
}

int main(){

    int numTestCases = 0;
    cin >> numTestCases; 
    vector< tuple<string, int>> output;

    for(int i = 0; i < numTestCases; i++){
        string testTitle;
        int r, c;
        cin >> testTitle;
        cin >> r;
        cin >> c;
        //cout << testTitle << ", r:" << to_string(r) << ", c:" << to_string(c) << endl; 
        vector< tuple<int,int,int>> grid;
        vector< vector<int>> solution(r, vector<int>(c,0));
        vector< vector<int>> elevations(r, vector<int>(c));
        int elevation;
        int longest = 0;
        for(int a = 0; a < r; a++){
            for(int b = 0; b < c; b++){
                cin >> elevation;
                //cout << "elevation: " << to_string(elevation) << ", a:" << to_string(a) << ", b:" << to_string(b) << endl; 
                grid.push_back(make_tuple(elevation, a, b));
                elevations[a][b] = elevation;
            }
        }

        sortGrid(grid); //since we are sorting the grid and solving it from bottom-up, don't need to worry about neighbors having higher elevation, however...
        //cout << "sorted!" << endl;
        for(auto x : grid){ //need some way to make sure neighboring squares aren't of same elevation 
            int soln_up, soln_down, soln_left, soln_right;


                //cout << "elevation: " << get<0>(x) << endl; 
            if(get<1>(x) > 0){
                soln_up = solution[get<1>(x)-1][get<2>(x)];

                if(get<0>(x) == elevations[get<1>(x)-1][get<2>(x)]){
                    soln_up = 0;
                }
            } else {
                    soln_up = 0;
            }

            if(get<1>(x) < r-1){ 
                soln_down = solution[get<1>(x)+1][get<2>(x)];
                if(get<0>(x) == elevations[get<1>(x)+1][get<2>(x)]){
                    soln_down = 0;
                }
            } else {
                soln_down = 0;
            }

            if(get<2>(x) > 0){
                soln_left = solution[get<1>(x)][get<2>(x)-1];
                if(get<0>(x) == elevations[get<1>(x)][get<2>(x)-1]){
                    soln_left = 0;
                }
            } else {
                soln_left = 0;
            }

            if(get<2>(x) < c-1){
                soln_right = solution[get<1>(x)][get<2>(x)+1];
                if(get<0>(x) == elevations[get<1>(x)][get<2>(x)+1]){
                    soln_right = 0;
                }
            } else {
                soln_right = 0;
            }
            int soln = 1+max({soln_up, soln_down, soln_left, soln_right});
            solution[get<1>(x)][get<2>(x)] = soln;
            if (soln > longest){
                longest = soln;
            }
        }

        output.push_back(make_tuple(testTitle,longest));
        
        /*instantiate vector holding tuple of elevation, row, col: vector< tuple<int,int,int>>. Fill using above cin nested for loop

        ascending sort vector by get<0>(x). Lowest elevation first

         for each 
         soln[r,c] = 1+max(
            soln[r+1,c],
            soln[r-1,c],
            soln[r,c+1],
            soln[r,c-1],
         )

         as long as each direction is not out of bounds or same elevation 
        */
    }
    //cout << "TEST" << endl;
    for (auto out : output){
        cout << get<0>(out) << ": " << to_string(get<1>(out)) << endl;
    }
    return 0;
}