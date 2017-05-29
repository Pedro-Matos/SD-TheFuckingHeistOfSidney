#!/usr/bin/env bash
username=sd0407
password=sportingsempre
url=http://192.168.8.171/sd0407/classes/
registryHostName=l040101-ws01.ua.pt
GeneralRepositoryHostName=l040101-ws03.ua.pt
MuseumHostName=l040101-ws04.ua.pt
ConcentrationHostName=l040101-ws05.ua.pt
AssaultPartyHostName=l040101-ws06.ua.pt
ColectionHostName=l040101-ws07.ua.pt
ThievesHostName=l040101-ws08.ua.pt
MasterThiefHostName=l040101-ws09.ua.pt
registryPortNum=22460

rm -rf *.zip
echo "Cleaning..."
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MuseumHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ConcentrationHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$AssaultPartyHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ColectionHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ThievesHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MasterThiefHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$GeneralRepositoryHostName "cd ~/heist/ ; rm -rf *"
echo " "