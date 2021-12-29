#include <iostream>
#include <tuple>
#include <vector>
#include <string>
#include <cmath>
#include <algorithm>
#include <stdlib.h>
#include <iomanip>
#include <random>

using namespace std;

int main(int argc, char* argv[]) {
    int numPoints = stoi(argv[1]);
    cout << numPoints << endl;
    normal_distribution<double> dist(0,4);
    default_random_engine gen;

    for(int i = 0; i < numPoints; i++){
        cout << dist(gen) << " " << dist(gen) << endl;
    }
    return 0;
}