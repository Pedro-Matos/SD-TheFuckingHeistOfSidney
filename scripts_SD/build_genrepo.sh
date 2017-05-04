#!/bin/bash

rm -r Log
unzip -qq  Log.zip
rm Log.zip
cd Log
javac -cp \* DistributedVersion/ProblemInformation/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
rm DistributedVersion/ProblemInformation/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.ProblemInformation.ServerGeneralRepo 
