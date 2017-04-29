#!/bin/bash

echo "Log"
sshpass -p "sportingsempre" scp Log.zip sd0407@l040101-ws01.ua.pt:/home/sd0407/ 
sleep 2

#echo "Museu"
#sshpass -p "sportingsempre" scp Museum.zip sd0407@l040101-ws02.ua.pt:/home/sd0407/
#sleep 2

echo "Conc"
sshpass -p "sportingsempre" scp Concentration.zip sd0407@l040101-ws03.ua.pt:/home/sd0407/ 
sleep 2

echo "Assault"
sshpass -p "sportingsempre" scp AssaultParty.zip sd0407@l040101-ws04.ua.pt:/home/sd0407/ 
sleep 2

echo "Coll"
sshpass -p "sportingsempre" scp Collection.zip sd0407@l040101-ws05.ua.pt:/home/sd0407/ 
sleep 2

#echo "Thief"
#sshpass -p "sportingsempre" scp Thief.zip sd0407@l040101-ws06.ua.pt:/home/sd0407/ 
#sleep 2

#echo "Master"
#sshpass -p "sportingsempre" scp Master.zip sd0407@l040101-ws08.ua.pt:/home/sd0407/ 