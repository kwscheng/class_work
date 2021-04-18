#!/usr/bin/env python3

from operator import itemgetter
import sys

current_airline_route = None
current_count = 0
airline_route = None
totalArrivalDelay = 0.00
arrivalDelay = None

# input comes from STDIN
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()

    # parse the input we got from mapper.py
    try:
        airline_route, arrivalDelay = line.split('\t', 1)
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
    if current_airline_route == airline_route:
        current_count += 1
        totalArrivalDelay += arrivalDelay
    else:
        if current_airline_route:
            # write result to STDOUT
            print("{0}\t{1}".format(current_airline_route, totalArrivalDelay/current_count))
        current_count = 1
        current_airline_route = airline_route
        totalArrivalDelay = arrivalDelay

# do not forget to output the last word if needed!
if current_airline_route == airline_route:
    print("{0}\t{1}".format(current_airline_route, totalArrivalDelay/current_count))
