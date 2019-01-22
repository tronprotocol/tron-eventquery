#!/bin/bash
mongoIp=`cat config.conf | grep mongo.host | awk -F '=' '{print $2}'`
mongoPort=`cat config.conf | grep mongo.port | awk -F '=' '{print $2}'`
dbname=`cat config.conf | grep mongo.dbname | awk -F '=' '{print $2}'`
userName=`cat config.conf | grep mongo.username | awk -F '=' '{print $2}'`
password=`cat config.conf | grep mongo.password | awk -F '=' '{print $2}'`
deadline=`cat config.conf | grep mongo.deadline | awk -F '=' '{print $2}'`
historyTimeStamp=0
if [[ $foo != *[!0-9]* ]];
then
   let historyTimeStamp=$(date +%s)*1000
   let historyTimeStamp=historyTimeStamp-24*3600*deadline*1000
fi
echo $historyTimeStamp
mongodb='mongo '$mongoIp':'$mongoPort
$mongodb <<EOF
use ${dbname}
db.auth("${userName}", "${password}")
db.transaction.remove({"timeStamp":{\$lt: ${historyTimeStamp}}})
db.block.remove({"timeStamp":{\$lt: ${historyTimeStamp}}})
db.contractevent.remove({"timeStamp":{\$lt: ${historyTimeStamp}}})
db.contractlog.remove({"timeStamp":{\$lt: ${historyTimeStamp}}})
EOF
