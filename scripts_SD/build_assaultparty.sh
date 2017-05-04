#!/bin/bash

rm -r AssaultParty
unzip -qq AssaultParty.zip
rm AssaultParty.zip
cd AssaultParty
javac -nowarn -cp \* DistributedVersion/Monitors/AssaultParty/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
rm DistributedVersion/Monitors/AssaultParty/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.Monitors.AssaultParty.ServerAssaultParty
