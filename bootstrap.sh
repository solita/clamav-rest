#!/bin/bash
set -m

host=${CLAMD_HOST:-127.0.0.1}
port=${CLAMD_PORT:-3310}
filesize=${MAXSIZE:-10240MB}
timeout=${TIMEOUT:-10000}
loglevel=${LOGLEVEL:-debug}

echo "using clamd server: $host:$port"
echo "loglevel: $loglevel"

java -jar /var/clamav-rest/clamav-rest-1.0.2.jar --clamd.host=$host --clamd.port=$port --clamd.maxfilesize=$filesize --clamd.maxrequestsize=$filesize --clamd.timeout=$timeout --$loglevel --server.shutdown=graceful --spring.lifecycle.timeout-per-shutdown-phase=60s
