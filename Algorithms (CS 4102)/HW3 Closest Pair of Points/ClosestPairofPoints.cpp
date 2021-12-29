#include <iostream>
#include <tuple>
#include <vector>
#include <string>
#include <cmath>
#include <algorithm>
#include <stdlib.h>
#include <iomanip>
#include <chrono>


using namespace std;

double findDistance(tuple<double, double> p1, tuple<double, double> p2) {
    double x1 = get<0>(p1);
    double y1 = get<1>(p1);

    double x2 = get<0>(p2);
    double y2 = get<1>(p2);

    return sqrt(pow((x1-x2),2)+pow((y1-y2),2));
}

double bruteForce(vector<tuple<double, double> > &list) {
	//TODO: IMPLEMENT Brute force CPoP
    // If there are no points closer than a distance of 10,000, you should print out ’infinity’.
    double shortest = findDistance(list[0], list[1]);
    double dist;
    for (int i = 0; i < list.size()-1; i++) {
        for(int j = i+1; j < list.size(); j++){
            dist = findDistance(list[i],list[j]);
            if (dist < shortest)
                shortest = dist;
        }
    }

	return shortest;
}

//returns true if [i.x < j.x] OR [i.x == j.x AND i.y < j.y]
bool compareX (tuple<double, double> i,tuple<double, double> j) { 
    return (get<0>(i) < get<0>(j)) || ((get<0>(i) == get<0>(j)) && get<1>(i) < get<1>(j)); 
}

//returns true if [i.y < j.y] OR [i.y == j.y AND i.x < j.x]
bool compareY (tuple<double, double> i,tuple<double, double> j) { 
    return (get<1>(i) < get<1>(j)) || ((get<1>(i) == get<1>(j)) && get<0>(i) < get<0>(j)); 
}

//returns a list of Points sorted by x 
// if same x coordinate, compare y. y1 < y2, point 1 goes on left
vector<tuple<double, double> > sortX(vector<tuple<double, double> > &list){
    vector<tuple<double, double> > listX = {list.begin(), list.end()};
    sort(listX.begin(), listX.end(), compareX);
    return listX;
}

//returns a list of Points sorted by y 
// if same y coordinate, compare x.
vector<tuple<double, double> > sortY(vector<tuple<double, double> > &list){
    vector<tuple<double, double> > listY = {list.begin(), list.end()};
    sort(listY.begin(), listY.end(), compareY);
    return listY;
}

//input list sorted by y
double solveStrip(vector<tuple<double, double> > &list, double dist){
    double min = dist;
    for(int i = 0; i < list.size(); i++){
        for(int j = i + 1; (abs(get<1>(list[i])-get<1>(list[j])) < dist) && j < list.size() ; j++){
            if(findDistance(list[i], list[j]) < min){
                min = findDistance(list[i], list[j]);
                /*cout << "found smaller distance between (" << get<0>(list[i]) << ", " << get<1>(list[i]) << ")" <<
                " and (" << get<0>(list[j]) << ", " << get<1>(list[j]) << ")" << ": "<< min << endl;*/
            }
        }
    }
    return min;
}

/*inputs:
 list sorted by x 
 list sorted by y

 outputs:
 smallest distance
 */
double divNconq(vector<tuple<double, double> > listX, vector<tuple<double, double> > listY){
    if(listX.size() <= 3){
        return bruteForce(listX);
    }

    int medIndex = listX.size()/2;
    //cout << "median index: " << medIndex << endl;
    int medianX = get<0>(listX[medIndex]);
    int medianY = get<1>(listX[medIndex]);

    vector<tuple<double, double> > listXL = {listX.begin(), listX.begin()+medIndex};
    vector<tuple<double, double> > listXR = {listX.begin()+medIndex+1, listX.end()};

    vector<tuple<double, double> > listYL;
    vector<tuple<double, double> > listYR;


    for( auto x : listY){
        if (get<0>(x) < medianX || ((get<0>(x) == medianX) && (get<1>(x) < medianY))) {
            listYL.push_back(x);
        } else {
            listYR.push_back(x);
        }
    }

    //recursive call
    double distLeft = divNconq(listXL, listYL);
    double distRight= divNconq(listXR, listYR);

    double dist = min(distLeft, distRight);

    vector<tuple<double, double> > strip;

    for(auto x : listY){
        if (abs(get<0>(x) - medianX) < dist){
            strip.push_back(x);
        }
    }

    return solveStrip(strip, dist);
}

double closestPairofPoints(vector<tuple<double, double> > &list){
    vector<tuple<double, double> > listX = sortX(list);
    vector<tuple<double, double> > listY = sortY(list);

    return divNconq(listX, listY);
}

int main(){
    int numStars;
    vector<string> output;
    while(true){
    cin >> numStars;
    if(numStars == 0){break;}

    vector<tuple<double,double> > starList;
    for (int i = 0; i < numStars; i++){
        string x, y;
        cin >> x;
        cin >> y;
        //cout << x << " " << y<< endl;
        starList.push_back(make_tuple(stod(x),stod(y)));
    }
    
    //auto beginBF = chrono::high_resolution_clock::now(); //source: https://levelup.gitconnected.com/8-ways-to-measure-execution-time-in-c-c-48634458d0f9
    //cout << "Brute Force:" << bruteForce(starList) << endl;
    /*
	auto endBF = chrono::high_resolution_clock::now();
	auto BFTime = std::chrono::duration_cast<std::chrono::microseconds>(endBF - beginBF);
    cout << "Brute Force execution time (μs) on " << numStars << ": " << BFTime.count() << endl;
    //cout << endl << "Divide and Conquer: " << closestPairofPoints(starList) << endl;
    */
   
    //auto beginDC = chrono::high_resolution_clock::now(); 
    output.push_back(to_string(closestPairofPoints(starList)));
    /*auto endDC = chrono::high_resolution_clock::now();
	auto DCTime = std::chrono::duration_cast<std::chrono::microseconds>(endDC - beginDC);
    cout << "Divide & Conquer execution time (μs) on " << numStars << ": " << DCTime.count() << endl;*/

    }
    cout << fixed;
    for (auto x : output){
        if(stoi(x) >= 10000){
            cout << "infinity" << endl;
        } else{
            cout << fixed;
            cout << setprecision(4);
            cout << stod(x) << endl;
        }
    }
    return 0;
}