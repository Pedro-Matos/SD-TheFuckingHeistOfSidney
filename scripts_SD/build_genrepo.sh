#!/bin/bash

rm -r Log
unzip Log.zip
cd Log
javac -cp \* DistributedVersion/ProblemInformation/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.ProblemInformation.ServerGeneralRepo 
