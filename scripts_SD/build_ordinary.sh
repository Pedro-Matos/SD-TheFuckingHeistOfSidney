#!/bin/bash

rm -r Thief
unzip Thief.zip
cd Thief
javac -cp \* DistributedVersion/Actors/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
#rm DistributedVersion/ProblemInformation/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.Actors.ServerThief 
