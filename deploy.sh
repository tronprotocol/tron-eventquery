#!/bin/bash
while true; do
  pid=`ps -ef | grep troneventquery | grep -v grep | awk '{print $2}'`
  if [ -n "$pid" ]; then
    kill -15 $pid
    echo "ending tron event query process"
    sleep 1
  else
    echo "tron event query killed successfully!"
    break
  fi
done
nohup java -jar target/troneventquery-1.0.0-SNAPSHOT.jar 2>&1 &
sleep 10
echo "ok!"
