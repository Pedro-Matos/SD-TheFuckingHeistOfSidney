#!/usr/bin/env bash
echo "running... "
sh set_rmiregistry_alt.sh 22460 &
sleep 2
sh scripts/run_registry.sh &
sleep 2
sh scripts/run_general.sh & PID_Logging=$!
sleep 2
sh scripts/run_museum.sh &
sleep 2
sh scripts/run_conc.sh &
sleep 2
sh scripts/run_ass.sh &
sleep 2
sh scripts/run_col.sh &
sleep 2
sh scripts/run_thief.sh &
sleep 2
sh scripts/run_master.sh &

wait $PID_Logging
sudo kill `sudo lsof -t -i:22460`
sudo kill `sudo lsof -t -i:22461`