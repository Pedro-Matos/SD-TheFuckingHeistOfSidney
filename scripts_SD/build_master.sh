#!/bin/bash

rm -r Master
unzip Master.zip
cd Master
javac -cp \* DistributedVersion/Actors/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
#rm DistributedVersion/ProblemInformation/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.Actors.ServerMasterThief 
