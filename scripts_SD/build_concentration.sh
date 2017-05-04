#!/bin/bash

rm -r Concentration
unzip -qq  Concentration.zip
rm Concentration.zip
cd Concentration
javac -cp \* DistributedVersion/Monitors/ConcentrationSite/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java DistributedVersion/Support/*.java
rm DistributedVersion/Monitors/ConcentrationSite/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java DistributedVersion/Support/*.java
java -cp .:\* DistributedVersion.Monitors.ConcentrationSite.ServerConcentrationSite
