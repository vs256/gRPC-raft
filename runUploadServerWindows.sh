#!/bin/bash
#
# This script is used to start the server from a supplied config file
#

if [ "$#" -ne 1 ]; then
   echo -e "\nUsage: $0 <config file>\n"
   exit
fi


SVR_HOME="C:/Users/vidas/Git-Projects/gRPC-raft"
echo "** starting server from ${SVR_HOME} **"

echo server home = $SVR_HOME
#exit

#cd ${SVR_HOME}

JAVA_MAIN=' vidas.grpc.route.server.FileServerImpl'
JAVA_ARGS="$1"
echo -e "\n** config: ${JAVA_ARGS} **\n"

# superceded by http://www.oracle.com/technetwork/java/tuning-139912.html
JAVA_TUNE='-server -Xms500m -Xmx1000m -XX:MaxDirectMemorySize=5g'


java ${JAVA_TUNE} -cp .:${SVR_HOME}/lib/'*':${SVR_HOME}/lib-ref2/'*':${SVR_HOME}/classes ${JAVA_MAIN} ${JAVA_ARGS} 
