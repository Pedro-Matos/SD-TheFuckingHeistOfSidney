#!/usr/bin/env bash

sh compile_source_code.sh

echo "running... "
sh set_rmiregistry_alt.sh 22460 &
sleep 2
sh scripts/run_registry.sh localhost 22460 &
sleep 2
sh scripts/run_general.sh localhost 22460 & PID_Logging=$!
sleep 2
sh scripts/run_museum.sh localhost 22460 &
sleep 2
sh scripts/run_conc.sh localhost 22460 &
sleep 2
sh scripts/run_ass.sh localhost 22460 &
sleep 2
sh scripts/run_col.sh localhost 22460 &
sleep 2
sh scripts/run_thief.sh localhost 22460 &
sleep 2
sh scripts/run_master.sh localhost 22460 &

wait $PID_Logging

echo " "
echo "Leaving and cleaning"


kill `lsof -t -i:22460`
kill `lsof -t -i:22461`