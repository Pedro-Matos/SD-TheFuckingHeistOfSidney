#!/bin/bash

rm -r Thief
unzip -qq  Thief.zip
rm Thief.zip
cd Thief
javac -cp \* DistributedVersion/Actors/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
rm DistributedVersion/Actors/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.Actors.ServerThief 
