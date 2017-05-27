#!/usr/bin/env bash
echo "compiling source code..."
javac -cp \* support/*.java interfaces/*.java registry/*.java serverSide/generalRepository/*.java serverSide/museum/*.java serverSide/concentrationSite/*.java serverSide/assaultParty/*.java serverSide/collectionSite/*.java clientSide/thief/*.java clientSide/masterThief/*.java
echo "Copying source code ... "
cp interfaces/Register.class dir_registry/interfaces/
cp registry/*.class dir_registry/registry/
cp interfaces/*.class dir_serverSide/interfaces/
cp interfaces/*.class dir_clientSide/interfaces/
cp serverSide/generalRepository/*.class dir_serverSide/serverSide/generalRepository/
cp serverSide/museum/*.class dir_serverSide/serverSide/museum/
cp serverSide/concentrationSite/*.class dir_serverSide/serverSide/concentrationSite/
cp serverSide/assaultParty/*.class dir_serverSide/serverSide/assaultParty/
cp serverSide/collectionSite/*.class dir_serverSide/serverSide/collectionSite/
cp clientSide/masterThief/*.class dir_clientSide/clientSide/masterThief/
cp clientSide/thief/*.class dir_clientSide/clientSide/thief/
cp support/*.class dir_serverSide/support/
cp support/*.class dir_clientSide/support/
echo "done..."
#mkdir -p /home/ruib/Public/classes
#mkdir -p /home/ruib/Public/classes/interfaces
#mkdir -p /home/ruib/Public/classes/clientSide
#cp interfaces/*.class /home/ruib/Public/classes/interfaces
#cp clientSide/Pi.class /home/ruib/Public/classes/clientSide
#cp set_rmiregistry.sh /home/ruib
#cp set_rmiregistry_alt.sh /home/ruib
