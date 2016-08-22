#!/bin/bash

fo=$(curl -s -F "name=blabla" -F "file=@./testi.sh" 192.168.99.100:8080/scan)

if [ "$fo" != "Everything ok : true" ]
  then
    echo "not ok.. $fo"
    exit -1
fi
