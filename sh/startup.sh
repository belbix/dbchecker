#!/bin/bash
nohup java -Xmx128m -Dlogback.configurationFile=logback.xml -cp dbchecker.jar pro.belbix.dbchecker.Main checker.cfg > /dev/null 2>&1 &
MyPID=$!
echo Start DB Checker with PID: $MyPID
echo $MyPID > ./pid
