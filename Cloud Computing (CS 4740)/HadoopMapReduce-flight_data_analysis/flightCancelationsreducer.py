#!/usr/bin/env python3

from operator import itemgetter
import sys

current_reason = None
current_count = 0
count = None
reason = None

# input comes from STDIN
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()

    # parse the input we got from mapper.py
    try:
        reason, count = line.split('\t', 1)
    except ValueError:
        continue
    # convert count (currently a string) to int
    try:
        count = int(count)
    except ValueError:
        # count was not a number, so silently
        # ignore/discard this line
        continue

    # this IF-switch only works because Hadoop sorts map output
    # by key (here: word) before it is passed to the reducer
    if current_reason == reason:
        current_count += count
    else:
        if current_reason:
            # write result to STDOUT
            print("{0}\t{1}".format(current_reason, current_count))
        current_count = 1
        current_reason = reason

# do not forget to output the last word if needed!
if current_reason == reason:
    print("{0}\t{1}".format(reason, current_count))

