#!/usr/bin/env python
import os
import subprocess
import sys

num_correct = subprocess.call("./execute_submission_and_assess_output.sh", shell=True)
print ("Score: " + str(num_correct) + " out of 2 correct. \n")
print("*************Original submission************* \n")
with open('subtract.py','r') as fs:
    print(fs.read())

