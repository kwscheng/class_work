#!/usr/bin/env python3

import sys


for line in sys.stdin:
    line = line.strip()
    words = line.split(",")
    if words[1] == '"MQ"' or words[1] == '"G4"' or words[1] == '"B6"':
        print("{0}:{1}->{2}\t{3}".format(words[1],words[4],words[9], words[14]).replace(chr(34),""))

