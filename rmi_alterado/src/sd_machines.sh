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

sh clean_class_zip.sh
sh compile_source_code.sh

echo "compressing source code..."
zip -qr dir_registry.zip deploy/dir_registry/
zip -qr dir_GeneralRepository.zip deploy/dir_GeneralRepository/
zip -qr dir_Museum.zip deploy/dir_Museum/
zip -qr dir_ConcentrationSite.zip deploy/dir_ConcentrationSite/
zip -qr dir_AssaultParty.zip deploy/dir_AssaultParty/
zip -qr dir_CollectionSite.zip deploy/dir_CollectionSite/
zip -qr dir_Thief.zip deploy/dir_Thief/
zip -qr dir_MasterThief.zip deploy/dir_MasterThief/


sleep 5

echo "Unziping code in machines"
sshpass -p $password  scp  dir_registry.zip $username@$registryHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/heist/ ; unzip -qq dir_registry.zip &"

sshpass -p $password  scp  dir_GeneralRepository.zip $username@$GeneralRepositoryHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$GeneralRepositoryHostName "cd ~/heist/ ; unzip -qq dir_GeneralRepository.zip &"

sshpass -p $password  scp  dir_Museum.zip $username@$MuseumHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MuseumHostName "cd ~/heist/ ; unzip -qq dir_Museum.zip &"

sshpass -p $password  scp  dir_ConcentrationSite.zip $username@$ConcentrationHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ConcentrationHostName "cd ~/heist/ ; unzip -qq dir_ConcentrationSite.zip &"

sshpass -p $password  scp  dir_AssaultParty.zip $username@$AssaultPartyHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$AssaultPartyHostName "cd ~/heist/ ; unzip -qq dir_AssaultParty.zip & "

sshpass -p $password  scp  dir_CollectionSite.zip $username@$ColectionHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ColectionHostName "cd ~/heist/ ; unzip -qq dir_CollectionSite.zip & "

sshpass -p $password  scp  dir_Thief.zip $username@$ThievesHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ThievesHostName "cd ~/heist/ ; unzip -qq dir_Thief.zip & "

sshpass -p $password scp  dir_MasterThief.zip $username@$MasterThiefHostName:~/heist/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MasterThiefHostName "cd ~/heist/ ; unzip -qq dir_MasterThief.zip & "

sh rm -rf *.zip

echo "Setting RMI repository.... "
echo " "
sshpass -p $password scp set_rmiregistry.sh $username@$registryHostName:~/heist/
sshpass -p $password scp -r deploy/interfaces/ $username@$registryHostName:~/Public/classes/
sshpass -p $password scp -r deploy/support/ $username@$registryHostName:~/Public/classes/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "sh ~/heist/set_rmiregistry.sh $registryPortNum" &
sleep 5
echo " "


echo "Setting Service Register.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/heist/deploy/dir_registry/ ; ./registry_com.sh $registryHostName $registryPortNum $url &"
sleep 5
echo " "


echo "Setting General Repository.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$GeneralRepositoryHostName "cd ~/heist/deploy/dir_GeneralRepository/ ; ./serverSideGenRepo_com.sh $registryHostName $registryPortNum $url &"
sleep 5
echo " "


echo "Setting Museum.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MuseumHostName "cd ~/heist/deploy/dir_Museum/ ; ./serverSideMuseum_com.sh $registryHostName $registryPortNum $url &"
sleep 5
echo " "


echo "Setting Concentration Site.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ConcentrationHostName "cd ~/heist/deploy/dir_ConcentrationSite/ ; ./serverSideConcentration_com.sh $registryHostName $registryPortNum $url &"
sleep 5
echo " "


echo "Setting Assault Party Site.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$AssaultPartyHostName "cd ~/heist/deploy/dir_AssaultParty/ ; ./serverSideAssault_com.sh $registryHostName $registryPortNum $url &"
sleep 5
echo " "


echo "Setting Collection Site.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ColectionHostName "cd ~/heist/deploy/dir_CollectionSite/ ; ./serverSideCollection_com.sh $registryHostName $registryPortNum $url &"
sleep 5
echo " "

echo "Setting Ordinary Thieves.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ThievesHostName "cd ~/heist/deploy/dir_Thief/ ; sh clientSideThief_com.sh $registryHostName $registryPortNum &"
sleep 5
echo " "

echo "Setting Master Thief.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MasterThiefHostName "cd ~/heist/deploy/dir_MasterThief/ ; sh clientSideMaster_com.sh $registryHostName $registryPortNum &"
sleep 10
echo " "



#sshpass -p $password  scp $username@$GeneralRepositoryHostName:~/heist/deploy/dir_GeneralRepository/Log.txt .

#rm -rf *.zip
#echo "Cleaning..."
#sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/heist/ ; rm -rf *"
#sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MuseumHostName "cd ~/heist/ ; rm -rf *"
#sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ConcentrationHostName "cd ~/heist/ ; rm -rf *"
#sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$AssaultPartyHostName "cd ~/heist/ ; rm -rf *"
#sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ColectionHostName "cd ~/heist/ ; rm -rf *"
#sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ThievesHostName "cd ~/heist/ ; rm -rf *"
#sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MasterThiefHostName "cd ~/heist/ ; rm -rf *"
#sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$GeneralRepositoryHostName "cd ~/heist/ ; rm -rf *"
#echo " "
#
#echo " "
#
#subl Log.txt