#!/bin/bash

rm -r Collection
unzip -qq  Collection.zip
cd Collection
javac -cp \* DistributedVersion/Monitors/CollectionSite/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
rm DistributedVersion/Monitors/CollectionSite/*.java DistributedVersion/ComInfo/*.java DistributedVersion/Messages/*.java
java -cp .:\* DistributedVersion.Monitors.CollectionSite.ServerCollectionSite
