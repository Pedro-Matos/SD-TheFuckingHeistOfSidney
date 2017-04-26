#!/bin/bash

rm -r Concentration
unzip Concentration.zip
cd Concentration
javac -cp \* DistributedVersion/Monitors/ConcentrationSite/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java DistributedVersion/Support/*.java
java -cp .:\* DistributedVersion.Monitors.ConcentrationSite.ServerConcentrationSite
