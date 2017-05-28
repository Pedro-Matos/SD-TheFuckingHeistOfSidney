#!/usr/bin/env bash
java -Djava.rmi.server.codebase="http://192.168.8.171/sd0407/classes/"\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     serverSide.collectionSite.CollectionSiteServer $1 $2