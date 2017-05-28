#!/usr/bin/env bash
username=sd0407
password=sportingsempre
registryHostName=l040101-ws01.ua.pt
GeneralRepositoryHostName=l040101-ws03.ua.pt
MuseumHostName=l040101-ws04.ua.pt
ConcentrationHostName=l040101-ws05.ua.pt
AssaultPartyHostName=l040101-ws06.ua.pt
ColectionHostName=l040101-ws07.ua.pt
ThievesHostName=l040101-ws08.ua.pt
MasterThiefHostName=l040101-ws09.ua.pt
registryPortNum=22460

echo "Cleaning..."
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$GeneralRepositoryHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MuseumHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ConcentrationHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$AssaultPartyHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ColectionHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ThievesHostName "cd ~/heist/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MasterThiefHostName "cd ~/heist/ ; rm -rf *"
echo " "

echo "Setting RMI repository.... "
echo " "
sshpass -p $password scp set_rmiregistry.sh $username@$registryHostName:~/heist/
sshpass -p $password scp -r one_machine_Deploy/interfaces/ $username@$registryHostName:~/Public/classes/
sshpass -p $password scp -r one_machine_Deploy/support/ $username@$registryHostName:~/Public/classes/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "sh ~/heist/set_rmiregistry.sh $registryPortNum" &
sleep 5
echo " "


echo "Setting Service Register.... "
echo " "
sshpass -p $password  scp -r one_machine_Deploy/dir_registry/ $username@$registryHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/heist/dir_registry/ ; ./registry_com.sh $registryHostName $registryPortNum &"
sleep 7
echo " "


echo "Setting General Repository.... "
echo " "
sshpass -p $password  scp -r one_machine_Deploy/dir_GeneralRepository/ $username@$GeneralRepositoryHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$GeneralRepositoryHostName "cd ~/heist/dir_GeneralRepository/ ; ./serverSideGenRepo_com.sh $registryHostName $registryPortNum &" PID_Logging=$!
sleep 7
echo " "


echo "Setting Museum.... "
echo " "
sshpass -p $password  scp -r one_machine_Deploy/dir_Museum/ $username@$MuseumHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MuseumHostName "cd ~/heist/dir_Museum/ ; ./serverSideMuseum_com.sh $registryHostName $registryPortNum &"
sleep 7
echo " "


echo "Setting Concentration Site.... "
echo " "
sshpass -p $password  scp -r one_machine_Deploy/dir_ConcentrationSite/ $username@$ConcentrationHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ConcentrationHostName "cd ~/heist/dir_ConcentrationSite/ ; ./serverSideConcentration_com.sh $registryHostName $registryPortNum &"
sleep 7
echo " "


echo "Setting Assault Party Site.... "
echo " "
sshpass -p $password  scp -r one_machine_Deploy/dir_AssaultParty/ $username@$AssaultPartyHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$AssaultPartyHostName "cd ~/heist/dir_AssaultParty/ ; ./serverSideAssault_com.sh $registryHostName $registryPortNum &"
sleep 7
echo " "


echo "Setting Collection Site.... "
echo " "
sshpass -p $password  scp -r one_machine_Deploy/dir_CollectionSite/ $username@$ColectionHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ColectionHostName "cd ~/heist/dir_CollectionSite/ ; ./serverSideCollection_com.sh $registryHostName $registryPortNum &"
sleep 7
echo " "

echo "Setting Ordinary Thieves.... "
echo " "
sshpass -p $password  scp -r one_machine_Deploy/dir_Thief/ $username@$ThievesHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ThievesHostName "cd ~/heist/dir_Thief/ ; sh clientSideThief_com.sh $registryHostName $registryPortNum &"
sleep 10
echo " "

echo "Setting Master Thief.... "
echo " "
sshpass -p $password  scp -r one_machine_Deploy/dir_MasterThief/ $username@$MasterThiefHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MasterThiefHostName "cd ~/heist/dir_MasterThief/ ; sh clientSideMaster_com.sh $registryHostName $registryPortNum &"
sleep 10
echo " "



echo "Waiting"
wait $PID_Logging


echo " "
echo "Leaving "