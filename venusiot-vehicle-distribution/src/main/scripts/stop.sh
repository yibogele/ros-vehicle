#!/bin/bash
NODE_NAME=`dirname $PWD`
ps -ef | grep -v grep | grep java | grep "${NODE_NAME}" | grep com.venusiot.vehicle.VehicleGateway | awk '{print $2}' | xargs kill -9 2>/dev/null