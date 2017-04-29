#!/bin/bash
# kill java process running

PID=`ps -C java -o pid=`
if [[ ! -z "$PID" ]]; then
	kill -9 $PID
fi