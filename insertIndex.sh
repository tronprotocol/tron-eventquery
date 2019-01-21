#!/bin/bash
mongoIp=`cat config.conf | grep mongo.host | awk -F '=' '{print $2}'`
mongoPort=`cat config.conf | grep mongo.port | awk -F '=' '{print $2}'`
dbname=`cat config.conf | grep mongo.dbname | awk -F '=' '{print $2}'`
userName=`cat config.conf | grep mongo.username | awk -F '=' '{print $2}'`
password=`cat config.conf | grep mongo.password | awk -F '=' '{print $2}'`

#mongo ip+port
mongodb='mongo '$mongoIp':'$mongoPort
$mongodb <<EOF
use ${dbname}
db.auth("${userName}", "${password}")
db.contractevent.ensureIndex({contractAddress:-1})
db.transaction.ensureIndex({fromAddress:-1})
db.transaction.ensureIndex({contractType:-1})
db.transaction.ensureIndex({toAddress:-1})
db.transaction.ensureIndex({timeStamp:-1})
db.transaction.ensureIndex({transactionId:-1})
db.transaction.ensureIndex({blockHash:-1})
db.transaction.ensureIndex({blockNumber:-1})
db.transaction.ensureIndex({contractAddress:-1})
db.transaction.ensureIndex({assetName:-1})
db.contractevent.ensureIndex({eventSignature:-1})
db.contractevent.ensureIndex({transactionId:-1})
db.contractevent.ensureIndex({contractAddress:-1})
db.contractevent.ensureIndex({blockNumber:-1})
db.contractevent.ensureIndex({timeStamp:-1})
db.contractevent.ensureIndex({eventName:-1})
db.block.ensureIndex({blockNumber:-1})
db.block.ensureIndex({blockHash:-1})
db.block.ensureIndex({timeStamp:-1})
db.block.ensureIndex({latestSolidifiedBlockNumber:-1})
EOF
