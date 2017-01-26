#!/bin/bash
set -m

host=${CLAMD_HOST:-192.168.50.72}
port=${CLAMD_PORT:-3310}
maxfilesize=${CLAMD_MAX_FILE_SIZE:-20000KB}
maxrequestsize=${CLAMD_MAX_REQUEST_SIZE:-20000KB}

echo "using clamd server: $host:$port"
echo "Max file size: $maxfilesize"
echo "Max request size: $maxrequestsize"

# start in background
java -jar /var/clamav-rest/clamav-rest-1.0.2.jar --clamd.host=$host --clamd.port=$port --clamd.maxfilesize=$maxfilesize --clamd.maxrequestsize=$maxrequestsize




