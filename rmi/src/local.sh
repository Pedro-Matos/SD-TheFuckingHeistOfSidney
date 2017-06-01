#!/usr/bin/env bash
username=sd0407
password=sportingsempre
localhostHostName=l040101-ws01.ua.pt
registryPortNum=22460
localhost=localhost

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
sshpass -p $password  scp  dir_registry.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_registry.zip &"

sshpass -p $password  scp  dir_GeneralRepository.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_GeneralRepository.zip &"

sshpass -p $password  scp  dir_Museum.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_Museum.zip & "

sshpass -p $password  scp  dir_ConcentrationSite.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_ConcentrationSite.zip &"

sshpass -p $password  scp  dir_AssaultParty.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_AssaultParty.zip &"

sshpass -p $password  scp  dir_CollectionSite.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_CollectionSite.zip &"

sshpass -p $password  scp  dir_Thief.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_Thief.zip & "

sshpass -p $password scp  dir_MasterThief.zip $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/ ; unzip -qq dir_MasterThief.zip &"
sleep 5


echo "Running... "
echo " "

echo "Setting RMI repository.... "
sshpass -p $password scp set_rmiregistry_alt.sh $username@$localhostHostName:~/heist_local/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "sh ~/heist_local/set_rmiregistry_alt.sh $registryPortNum & "
sleep 5
echo " "


echo "Setting Service Register.... "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/deploy/dir_registry/ ; ./registry_com_alt.sh $localhost $registryPortNum & "
sleep 5
echo " "


echo "Setting General Repository.... "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/deploy/dir_GeneralRepository/ ; ./serverSideGenRepo_com_alt.sh $localhost $registryPortNum & "
sleep 5
echo " "


echo "Setting Museum.... "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/deploy/dir_Museum/ ; ./serverSideMuseum_com_alt.sh $localhost $registryPortNum & "
sleep 5
echo " "


echo "Setting Concentration Site.... "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/deploy/dir_ConcentrationSite/ ; ./serverSideConcentration_com_alt.sh $localhost $registryPortNum & "
sleep 5
echo " "


echo "Setting Assault Party Site.... "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/deploy/dir_AssaultParty/ ; ./serverSideAssault_com_alt.sh $localhost $registryPortNum & "
sleep 5
echo " "


echo "Setting Collection Site.... "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/deploy/dir_CollectionSite/ ; ./serverSideCollection_com_alt.sh $localhost $registryPortNum &"
sleep 5
echo " "

echo "Setting Ordinary Thieves.... "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/deploy/dir_Thief/ ; sh clientSideThief_com_alt.sh $localhost $registryPortNum & "
sleep 5
echo " "

echo "Setting Master Thief.... "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$localhostHostName "cd ~/heist_local/deploy/dir_MasterThief/ ; sh clientSideMaster_com_alt.sh $localhost $registryPortNum & "
sleep 5
echo " "


echo "Waiting ...."
