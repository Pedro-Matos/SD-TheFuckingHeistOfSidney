#!/bin/bash

rm -r AssaultParty
unzip -qq AssaultParty.zip
cd AssaultParty
javac -cp \* DistributedVersion/Monitors/AssaultParty/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
rm DistributedVersion/Monitors/AssaultParty/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.Monitors.AssaultParty.ServerAssaultParty
