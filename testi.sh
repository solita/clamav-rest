#!/bin/bash

echo "testing by scanning this file.."

fo=$(curl -s -F "name=blabla" -F "file=@./testi.sh" localhost:8080/scan)

if [ "$fo" != "Everything ok : true" ]
  then
    echo "not ok.. $fo"
    exit -1
fi
