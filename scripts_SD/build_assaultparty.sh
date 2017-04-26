#!/bin/bash

rm -r AssaultParty
unzip AssaultParty.zip
cd AssaultParty
javac -cp \* DistributedVersion/Monitors/AssaultParty/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
#rm DistributedVersion/ProblemInformation/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.Monitors.AssaultParty.ServerAssaultParty
