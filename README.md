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
cd target
nohup java -jar troneventquery-1.0.0-SNAPSHOT.jar &
```
