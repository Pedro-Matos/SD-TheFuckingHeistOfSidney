#!/bin/bash

rm -r Museum
unzip -qq  Museum.zip
cd Museum
javac -cp \* DistributedVersion/Monitors/Museum/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
rm DistributedVersion/Monitors/Museum/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.Monitors.Museum.ServerMuseum
