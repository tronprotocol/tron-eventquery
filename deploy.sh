#!/bin/bash
git pull
mvn package
if [[ "$?" -ne 0 ]] ; then
  echo 'could not perform tests'; exit $rc
else
    while true; do
      pid=`ps -ef |grep trongrid |grep -v grep |awk '{print $2}'`
      if [ -n "$pid" ]; then
        kill -15 $pid
        echo "ending trongrid process"
        sleep 1
      else
        echo "trongrid killed successfully!"
        break
      fi
    done
     nohup java -jar target/trongrid-1.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
    sleep 10
    echo "ok!"
fi

