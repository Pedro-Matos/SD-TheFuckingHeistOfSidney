#!/usr/bin/env bash
username=sd0407
password=sportingsempre
localhostHostName=l040101-ws01.ua.pt
registryPortNum=22460
localhost=localhost

sh compile_source_code.sh

echo "compressing source code..."
zip -qr dir_registry.zip one_machine_Deploy/dir_registry/
zip -qr dir_GeneralRepository.zip one_machine_Deploy/dir_GeneralRepository/
zip -qr dir_Museum.zip one_machine_Deploy/dir_Museum/
zip -qr dir_ConcentrationSite.zip one_machine_Deploy/dir_ConcentrationSite/
zip -qr dir_AssaultParty.zip one_machine_Deploy/dir_AssaultParty/
zip -qr dir_CollectionSite.zip one_machine_Deploy/dir_CollectionSite/
zip -qr dir_Thief.zip one_machine_Deploy/dir_Thief/
zip -qr dir_MasterThief.zip one_machine_Deploy/dir_MasterThief/

echo "Unziping code in machines"
sshpass -p $password  scp  dir_registry.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_registry.zip"

sshpass -p $password  scp  dir_GeneralRepository.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_GeneralRepository.zip"

sshpass -p $password  scp  dir_Museum.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_Museum.zip"

sshpass -p $password  scp  dir_ConcentrationSite.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_ConcentrationSite.zip"

sshpass -p $password  scp  dir_AssaultParty.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_AssaultParty.zip"

sshpass -p $password  scp  dir_CollectionSite.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_CollectionSite.zip"

sshpass -p $password  scp  dir_Thief.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_Thief.zip"

sshpass -p $password scp  dir_MasterThief.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_MasterThief.zip"


echo "running... "

echo "Setting RMI repository.... "
echo " "
sshpass -p $password scp set_rmiregistry_alt.sh $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "sh ~/heist_local/set_rmiregistry_alt.sh $registryPortNum" &
sleep 5
echo " "


echo "Setting Service Register.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/one_machine_Deploy/dir_registry/ ; ./registry_com_alt.sh $localhost $registryPortNum &"
sleep 7
echo " "


echo "Setting General Repository.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/one_machine_Deploy/dir_GeneralRepository/ ; ./serverSideGenRepo_com_alt.sh $localhost $registryPortNum $url &" PID_Logging=$!
sleep 7
echo " "


echo "Setting Museum.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/one_machine_Deploy/dir_Museum/ ; ./serverSideMuseum_com_alt.sh $localhost $registryPortNum $url &"
sleep 7
echo " "


echo "Setting Concentration Site.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/one_machine_Deploy/dir_ConcentrationSite/ ; ./serverSideConcentration_com_alt.sh $localhost $registryPortNum $url &"
sleep 7
echo " "


echo "Setting Assault Party Site.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/one_machine_Deploy/dir_AssaultParty/ ; ./serverSideAssault_com_alt.sh $localhost $registryPortNum $url &"
sleep 7
echo " "


echo "Setting Collection Site.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/one_machine_Deploy/dir_CollectionSite/ ; ./serverSideCollection_com_alt.sh $localhost $registryPortNum $url &"
sleep 7
echo " "

echo "Setting Ordinary Thieves.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/one_machine_Deploy/dir_Thief/ ; sh clientSideThief_com_alt.sh $localhost $registryPortNum &"
sleep 10
echo " "

echo "Setting Master Thief.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/one_machine_Deploy/dir_MasterThief/ ; sh clientSideMaster_com_alt.sh $localhost $registryPortNum &"
sleep 10
echo " "



echo "Waiting"
wait $PID_Logging

sshpass -p $password  scp $username@$localhostHostName:~/heist_local/one_machine_Deploy/dir_GeneralRepository/Log.txt .
echo 4

rm -rf *.zip
echo "Cleaning..."
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; rm -rf *"
echo " "

echo " "

subl Log.txt