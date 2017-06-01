#!/usr/bin/env bash
username=sd0407
password=sportingsempre
localhostHostName=l040101-ws01.ua.pt
registryPortNum=22460
localhost=localhost
GeneralRepositoryHostName=l040101-ws03.ua.pt
sshpass -p $password  scp $username@$localhostHostName:~/heist_local/deploy/dir_GeneralRepository/Log.txt .
subl Log.txt