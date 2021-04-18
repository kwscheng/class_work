#!/usr/bin/env python3

from operator import itemgetter
import sys

current_airline = None
current_count = 0
airline = None
totalArrivalDelay = 0.00
arrivalDelay = None
#avgArrivalDict = {}

# input comes from STDIN
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()

    # parse the input we got from mapper.py
    try:
        airline, arrivalDelay = line.split('\t', 1)
    except ValueError:
        continue
    # convert count (currently a string) to int
    try:
        arrivalDelay = float(arrivalDelay)
    except ValueError:
        # count was not a number, so silently
        # ignore/discard this line
        continue

    # this IF-switch only works because Hadoop sorts map output
    # by key (here: word) before it is passed to the reducer
    if current_airline == airline:
        current_count += 1
        totalArrivalDelay += arrivalDelay
    else:
        if current_airline:
            # write result to STDOUT
            print("{0}\t{1}".format(current_airline, totalArrivalDelay/current_count))
            #avgArrivalDict[current_airline] = (totalArrivalDelay/current_count)
        current_count = 1
        current_airline = airline
        totalArrivalDelay = arrivalDelay

# do not forget to output the last word if needed!
if current_airline == airline:
    print("{0}\t{1}".format(current_airline, totalArrivalDelay/current_count))
    #avgArrivalDict[current_airline] = (totalArrivalDelay/current_count)
'''
sorted_dict = sorted(avgArrivalDict.items(), key=itemgetter(1))
print("Best five airlines")
for x in range(5):
    print(sorted_dict[x])
print("Worst five airlines")
for x in range(-1,-6,-1):
    print(sorted_dict[x])'''
