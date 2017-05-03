#!/bin/bash

echo "-------- Heist to the Museum --------"
echo " Sistemas Distribuídos 2017 - P4G07"
echo "Pedro Matos 71902, Tiago Bastos 71770"
echo ""

echo "Copying source code to the machines..."
echo "General Repository"
sshpass -p "sportingsempre" scp ../Zips_Jar/Log.zip sd0407@l040101-ws01.ua.pt:~
sleep 1
echo "Museum"
sshpass -p "sportingsempre" scp ../Zips_Jar/Museum.zip sd0407@l040101-ws02.ua.pt:~
sleep 1
echo "Concentration Site"
sshpass -p "sportingsempre" scp ../Zips_Jar/Concentration.zip sd0407@l040101-ws03.ua.pt:~
sleep 1
echo "Assault Party"
sshpass -p "sportingsempre" scp ../Zips_Jar/AssaultParty.zip sd0407@l040101-ws04.ua.pt:~
sleep 1
echo "Collection Site"
sshpass -p "sportingsempre" scp ../Zips_Jar/Collection.zip sd0407@l040101-ws05.ua.pt:~
sleep 1
echo "Thief"
sshpass -p "sportingsempre" scp ../Zips_Jar/Thief.zip sd0407@l040101-ws06.ua.pt:~
sleep 1
echo "Master Thief"
sshpass -p "sportingsempre" scp ../Zips_Jar/Master.zip sd0407@l040101-ws08.ua.pt:~
sleep 1
echo ""
echo ""


echo "Starting to Build General Repository..."
echo "Compiling & Cleaning General Repository source code..."
sshpass -p "sportingsempre" ssh -o StrictHostKeyChecking=no sd0407@l040101-ws01.ua.pt 'bash -s' < build_genrepo.sh & PID_Logging=$!
sleep 5


echo ""
echo ""
echo "Starting to Build Museum..."
echo "Compiling & Cleaning Museum source code..."
sshpass -p "sportingsempre" ssh -o StrictHostKeyChecking=no sd0407@l040101-ws02.ua.pt 'bash -s' < build_museum.sh &
sleep 5


echo ""
echo ""
echo "Starting to Build Concentration Site..."
echo "Compiling & Cleaning Concentration Site source code..."
sshpass -p "sportingsempre" ssh -o StrictHostKeyChecking=no sd0407@l040101-ws03.ua.pt 'bash -s' < build_concentration.sh &
sleep 5


echo ""
echo ""
echo "Starting to Build Assault Party..."
echo "Compiling & Cleaning Assault Party source code..."
sshpass -p "sportingsempre" ssh -o StrictHostKeyChecking=no sd0407@l040101-ws04.ua.pt 'bash -s' < build_assaultparty.sh &
sleep 5


echo ""
echo ""
echo "Starting to Build Collection Site..."
echo "Compiling & Cleaning Collection Site source code..."
sshpass -p "sportingsempre" ssh -o StrictHostKeyChecking=no sd0407@l040101-ws05.ua.pt 'bash -s' < build_collection.sh &
sleep 5

echo ""
echo ""
echo "Starting to Build Ordinary Thief..."
echo "Compiling & Cleaning Ordinary Thief source code..."
sshpass -p "sportingsempre" ssh -o StrictHostKeyChecking=no sd0407@l040101-ws06.ua.pt 'bash -s' < build_ordinary.sh &
sleep 5


echo ""
echo ""
echo "Starting to Build Master Thief..."
echo "Compiling & Cleaning Master Thief source code..."
sshpass -p "sportingsempre" ssh -o StrictHostKeyChecking=no sd0407@l040101-ws08.ua.pt 'bash -s' < build_master.sh &

echo ""
echo ""
echo "Waiting for simulation to end (generate a logging file).."
wait $PID_Logging

echo ""
echo ""
echo "Simulation ended, copying log file to the local machine"
sshpass -p "sportingsempre" scp -o StrictHostKeyChecking=no sd0407@l040101-ws01.ua.pt:/home/sd0407/Log/Log.txt .
