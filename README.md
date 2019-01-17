# TronEventQuery

TronEventQuery is implemented with tron's new event subscribe model. It uses same query interface with Tron-Grid.Users can 
also subscribe block trigger, transaction trigger, contract log trigger and contract event trigger. TronEvent is 
independent of particular branch of java-tron, the new event subscribe model will be released on version 3.5 of java-tron.

For more information of tron event subscribe model, please refer to https://github.com/tronprotocol/TIPs/issues/12.


## Build
```
mvn package
```

## Run
```
nohup java -jar target/troneventquery-1.0.0-SNAPSHOT.jar &
```

## What is the main HTTP service?
baseUrl: https://test.tronex.io  
 
### Main HTTP Service  
Function: get transaction list
```
subpath: $baseUrl/totaltransactions

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
start: start page, default is 1
block: start block number, default is 0

Example: https://test.tronex.io/totaltransactions
```

Function: get transaction by hash
```
subpath: $baseUrl/transactions/{hash}

parameters   
hash: transaction id

Example: https://test.tronex.io/totaltransactions/9a4f096700672d7420889cd76570ea47bfe9ef815bb2137b0d4c71b3d23309e9
```
Function: get transfers list
```
subpath: $baseUrl/transfers	

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
start: start page, default is 1
from: from address, default is ""
to: to address, default is ""
token: tokenName, default is ""

Example: https://test.tronex.io/transfers?token=trx&limit=1&sort=timeStamp&start=2&block=0&from=TJ7yJNWS8RmvpXcAyXBhvFDfGpV9ZYc3vt&to=TAEcoD8J7P5QjWT32r31gat8L7Sga2qUy8
```
Function: get transfers by transactionId
```
subpath: $baseUrl/transfers/{hash}

parameters   
hash: transfer hash

Example: https://test.tronex.io/transfers/70d655a17e04d6b6b7ee5d53e7f37655974f4e71b0edd6bcb311915a151a4700
```
Function: get events list
```
subpath: $baseUrl/events

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
since: start time of event occurrence, timeStamp >= since will be shown
start: start page, default is 1
block: block number, block number >= block will be shown

Example: https://test.tronex.io/events?limit=1&sort=timeStamp&since=0&block=0
```
Function: get events by transactionId
```
subpath: $baseUrl/events/transaction/{transactionId}

parameters   
transactionId

Example: https://test.tronex.io/events/transaction/cd402e64cad7e69c086649401f6427f5852239f41f51a100abfc7beaa8aa0f9c
```
Function: get events by contract address
```
subpath: $baseUrl/events/{contractAddress}

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
since: start time of event occurrence, timeStamp >= since will be shown
block: block number, block number >= block will be shown
contractAddress: contract address
start: start page, default is 1

Example: https://test.tronex.io/events/TMYcx6eoRXnePKT1jVn25ZNeMNJ6828HWk?limit=1&sort=-timeStamp&since=0&block=0&start=4
```
Function: get events by contract address and event name
```
subpath: $baseUrl/event/contract/{contractAddress}/{eventName}

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
since: start time of event occurrence, timeStamp >= since will be shown
contract`Address`: contract address
start: start page, default is 1
eventName: event name

Example: https://test.tronex.io/event/contract/TMYcx6eoRXnePKT1jVn25ZNeMNJ6828HWk/Bet?limit=1&sort=timeStamp&since=1
```
Function: get events by contract address, event name and block number
```
subpath: $baseUrl/event/contract/{contractAddress}/{eventName}/{blockNumber}

parameters   
contractAddress: contract address
blockNumber: block number, block number >= block will be shown
eventName: event name


Example: https://test.tronex.io/event/contract/TMYcx6eoRXnePKT1jVn25ZNeMNJ6828HWk/Bet/4835773
```
Function: get events by timeStamp
```
subpath: $baseUrl/event/timestamp

parameters   
since: start time of event occurrence, timeStamp >= since will be shown
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
start: start page, default is 1
contract: contract address, default is ""

Example: https://test.tronex.io/event/timestamp?since=1544483426749&limit=1&start=1
```
## Change config
change username, passwork or other information in config.conf
mongo.host=47.90.245.68
mongo.port=27017
mongo.dbname=eventlog
mongo.username=tron
mongo.password=123456

mongo.connectionsPerHost=8
mongo.threadsAllowedToBlockForConnectionMultiplier=4
