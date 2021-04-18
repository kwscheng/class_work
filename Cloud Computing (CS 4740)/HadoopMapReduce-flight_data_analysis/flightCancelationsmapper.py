#!/usr/bin/env python3

import sys


for line in sys.stdin:
    line = line.strip()
    words = line.split(",")
    if words[15] == "1.00":
        print("{0}\t1".format(words[16]))

