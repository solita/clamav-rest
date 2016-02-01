#!/bin/bash
set -m

host=${1:-192.168.50.72}
port=${2:-3310}

echo "using clamd server: $host:$port"

# start in background
java -jar /var/clamav-rest/clamav-rest-1.0.0.jar --clamd.host=$host --clamd.port=$port




