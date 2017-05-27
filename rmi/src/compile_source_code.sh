#!/usr/bin/env bash
echo "compiling source code..."

javac -cp \* support/*.java interfaces/*.java registry/*.java serverSide/generalRepository/*.java serverSide/museum/*.java serverSide/concentrationSite/*.java serverSide/assaultParty/*.java serverSide/collectionSite/*.java clientSide/thief/*.java clientSide/masterThief/*.java
echo "Copying Interfaces .class files ... "
cp interfaces/Register.class one_machine_Deploy/dir_registry/interfaces/
cp interfaces/*.class one_machine_Deploy/dir_AssaultParty/interfaces/
cp interfaces/*.class one_machine_Deploy/dir_CollectionSite/interfaces/
cp interfaces/*.class one_machine_Deploy/dir_ConcentrationSite/interfaces/
cp interfaces/*.class one_machine_Deploy/dir_GeneralRepository/interfaces/
cp interfaces/*.class one_machine_Deploy/dir_Museum/interfaces/
cp interfaces/*.class one_machine_Deploy/dir_MasterThief/interfaces/
cp interfaces/*.class one_machine_Deploy/dir_Thief/interfaces/


echo "Copying registry .class files ... "
cp registry/*.class one_machine_Deploy/dir_registry/registry/

echo "Copying Server Side .class files ... "
cp serverSide/generalRepository/*.class one_machine_Deploy/dir_GeneralRepository/serverSide/generalRepository/
cp serverSide/museum/*.class one_machine_Deploy/dir_Museum/serverSide/museum/
cp serverSide/concentrationSite/*.class one_machine_Deploy/dir_ConcentrationSite/serverSide/concentrationSite/
cp serverSide/assaultParty/*.class one_machine_Deploy/dir_AssaultParty/serverSide/assaultParty/
cp serverSide/collectionSite/*.class one_machine_Deploy/dir_CollectionSite/serverSide/collectionSite/

echo "Copying Client Side .class files ... "
cp clientSide/masterThief/*.class one_machine_Deploy/dir_MasterThief/clientSide/masterThief/
cp clientSide/thief/*.class one_machine_Deploy/dir_Thief/clientSide/thief/

echo "Copying support .class files ... "
cp support/*.class one_machine_Deploy/dir_GeneralRepository/support/
cp support/*.class one_machine_Deploy/dir_Museum/support/
cp support/*.class one_machine_Deploy/dir_ConcentrationSite/support/
cp support/*.class one_machine_Deploy/dir_AssaultParty/support/
cp support/*.class one_machine_Deploy/dir_CollectionSite/support/
cp support/*.class one_machine_Deploy/dir_MasterThief/support/
cp support/*.class one_machine_Deploy/dir_Thief/support/
echo "done..."
