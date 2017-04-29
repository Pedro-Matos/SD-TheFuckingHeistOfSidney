#!/bin/bash

rm -r Master
unzip -qq  Master.zip
cd Master
javac -cp \* DistributedVersion/Actors/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
rm DistributedVersion/Actors/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.Actors.ServerMasterThief 
