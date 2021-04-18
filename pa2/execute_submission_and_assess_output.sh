#!/bin/bash

CORRECT=0

tmpoutput=`echo -e 5 '\n' 6 | python subtract.py`
f1=`echo $tmpoutput | grep -q '[-]1'`
if [ $? = 0 ]; then
    let CORRECT=CORRECT+1
fi

tmpoutput=`echo -e 15 '\n' 8 | python subtract.py`
f1=`echo $tmpoutput | grep -q '7'`
if [ $? = 0 ]; then
    let CORRECT=CORRECT+1
fi

exit $CORRECT
