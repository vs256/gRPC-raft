#!/bin/bash
#


if [ "$#" -ne 1 ]; then
   echo -e "\nUsage: $0 <config file>\n"
   exit
fi

export SVR_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

JAVA_MAIN=' vidas.grpc.route.client.UploadClient'
JAVA_ARGS="$1"
JAVA_TUNE='-client -Xms96m -Xmx512m -XX:MaxDirectMemorySize=2g'

java ${JAVA_TUNE} -cp .:${SVR_HOME}/lib/'*':${SVR_HOME}/lib-ref2/'*':${SVR_HOME}/classes ${JAVA_MAIN} ${JAVA_ARGS} 

