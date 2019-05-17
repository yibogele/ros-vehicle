#!/bin/bash
dir=`dirname $PWD`
echo $dir
LIB=.
for jar in $dir/lib/*.jar
   do
     LIB=$LIB:$jar
   done

LIB=$LIB:$dir/config/
echo $LIB
########## home
#JDWP=-Xdebug -Xrunjdwp:transport=dt_socket,address=9998,server=y,suspend=n
VM_OPTS="-Xms1g -Xmx1g -XX:+HeapDumpOnOutOfMemoryError"
GATEWAY_OPTS="-Dapp.path=$dir/"
java -server $VM_OPTS $GATEWAY_OPTS -classpath $LIB: com.venusiot.vehicle.VehicleGateway


