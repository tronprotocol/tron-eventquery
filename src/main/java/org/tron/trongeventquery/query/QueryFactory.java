package org.tron.trongeventquery.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.pf4j.util.StringUtils;
import org.spongycastle.util.encoders.Hex;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.tron.common.crypto.Hash;
import org.tron.common.runtime.vm.DataWord;
import org.tron.common.runtime.vm.LogInfo;
import org.tron.trongeventquery.contractevents.ContractEventTriggerEntity;
import org.tron.trongeventquery.contractlogs.ContractLogTriggerEntity;
import org.tron.trongeventquery.contractlogs.DataWordEntity;
import org.tron.trongeventquery.contractlogs.LogInfoEntity;

public class QueryFactory {

  private Query query;

  public static final String findByContractAndEventSinceTimestamp = "{ 'contractAddress' : ?0, "
      + "'event_name': ?1,  "
      + "'$or' : [ {'block_timestamp' : ?2}, {'block_timestamp' : {$gt : ?2}} ], "
      + "'resource_Node' : {$exists : true} }";

  public static final String findByContractSinceTimeStamp = "{ 'contractAddress' : ?0, "
      + "'$or' : [ {'block_timestamp' : ?1}, {'block_timestamp' : {$gt : ?1}} ], "
      + "'resource_Node' : {$exists : true}}";

  public static Pageable make_pagination(int pageNum, int pageSize, String sortProperty) {

    if (sortProperty.charAt(0) == '-') {
      return PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, sortProperty.substring(1));
    }

    return PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, sortProperty);
  }

  public static boolean isBool(String s) {
    return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
  }

  public QueryFactory() {
    this.query = new Query();
  }

  public void setTransactionIdEqual(String hash) {
    this.query.addCriteria(Criteria.where("transactionId").is(hash));
  }

  public void setBlockHashEqual(String hash) {
    this.query.addCriteria(Criteria.where("blockHash").is(hash));
  }

  public void setTransferType() {
    Criteria criteria = new Criteria();
    criteria.orOperator(Criteria.where("contractType").is("TransferContract"),
        Criteria.where("contractType").is("TransferAssetContract"));
    this.query.addCriteria(criteria);
  }

  public void setContractTypeEqual(String contractType) {
    this.query.addCriteria(Criteria.where("contractType").is(contractType));
  }

  public void setTransactionFromAddr(String fromAddr) {
    this.query.addCriteria(Criteria.where("fromAddress").is(fromAddr));
  }

  public void setTransactionToAddr(String toAddr) {
    this.query.addCriteria(Criteria.where("toAddress").is(toAddr));
  }

  public void setTransactionToken(String token) {
    this.query.addCriteria(Criteria.where("assetName").is(token));
  }

  public void setTimestampGreaterEqual(long timestamp) {
    this.query.addCriteria(Criteria.where("timeStamp").gte(timestamp));
  }

  public void setUniqueIdEqual(String uniqueId) {
    this.query.addCriteria(Criteria.where("uniqueId").is(uniqueId));
  }

  public void setRemovedEqual(boolean removed) {
    this.query.addCriteria(Criteria.where("removed").is(removed));
  }

  public void findAllTransferByAddress(String address) {
    setTransferType();

    this.query.addCriteria(Criteria.where("contractAddress").is(address));
  }

  public void setContractAddress(String addr) {
    this.query.addCriteria(Criteria.where("contractAddress").is(addr));
  }

  public void setPageniate(Pageable page) {
    this.query.with(page);
  }

  public void setEventName(String event) {
    this.query.addCriteria(Criteria.where("eventName").is(event));
  }

  public void setBlockNum(long block) {
    this.query.addCriteria(Criteria.where("blockNumber").is(block));
  }

  public void setBlockNumGte(long block) {
    this.query.addCriteria(Criteria.where("blockNumber").gte(block));
  }

  public void setBlockNumGt(long block) {
    this.query.addCriteria(Criteria.where("blockNumber").gt(block));
  }

  public void setBlockNumLte(long block) {
    this.query.addCriteria(Criteria.where("blockNumber").lte(block));
  }

  public String toString() {
    return this.query.toString();
  }

  public Query getQuery() {
    return this.query;
  }

  public static List<ContractLogTriggerEntity> parseLogWithAbiByLog(
      List<ContractLogTriggerEntity> triggers,
      String abi) {
    List<ContractLogTriggerEntity> result = new ArrayList<>();
    Map<String, String> abiStrMap = new HashMap<>();
    Map<String, JSONObject> abiJsonMap = new HashMap<>();

    parseAbi(abi, abiStrMap, abiJsonMap);

    for (ContractLogTriggerEntity trigger : triggers) {
      LogInfoEntity logInfoEntity = trigger.getRawData();
      if (logInfoEntity == null){
        result.add(trigger);
        continue;
      }
      List<DataWordEntity> topics = logInfoEntity.getTopics();
      List<DataWord> mTopics = new ArrayList<>();
      for (DataWordEntity t : topics) {
        mTopics.add(new DataWord(t.getData()));
      }
      LogInfo logInfo = new LogInfo(Hex.decode(logInfoEntity.getAddress()), mTopics, Hex.decode(logInfoEntity.getData()));
      String logHash = getLogHash(logInfo);

      if (abiStrMap.get(logHash) == null) {
        ContractLogTriggerEntity event = new ContractLogTriggerEntity(
            logInfo.getHexTopics(),
            logInfo.getHexData(),
            trigger.getTransactionId(),
            trigger.getContractAddress(),
            trigger.getCallerAddress(),
            trigger.getOriginAddress(),
            trigger.getCreatorAddress(),
            trigger.getBlockNumber(),
            trigger.getRemoved(),
            trigger.getLatestSolidifiedBlockNumber(),
            trigger.getTimeStamp(),
            trigger.getTriggerName(),
            trigger.getUniqueId(),
            trigger.getRawData(),
            trigger.getAbiString()
        );

        result.add(event);
      }
    }

    return result;
  }

  public static List<ContractEventTriggerEntity> parseEventWithAbiByLog(
      List<ContractLogTriggerEntity> triggers,
      String abi) {
    List<ContractEventTriggerEntity> result = new ArrayList<>();
    Map<String, String> abiStrMap = new HashMap<>();
    Map<String, JSONObject> abiJsonMap = new HashMap<>();

    parseAbi(abi, abiStrMap, abiJsonMap);

    for (ContractLogTriggerEntity trigger : triggers) {
      LogInfoEntity logInfoEntity = trigger.getRawData();
      if (logInfoEntity == null){
        continue;
      }
      List<DataWordEntity> topics = logInfoEntity.getTopics();
      List<DataWord> mTopics = new ArrayList<>();
      for (DataWordEntity t : topics) {
        mTopics.add(new DataWord(t.getData()));
      }
      LogInfo logInfo = new LogInfo(Hex.decode(logInfoEntity.getAddress()), mTopics, Hex.decode(logInfoEntity.getData()));
      String logHash = getLogHash(logInfo);

      if (abiStrMap.get(logHash) != null) {
        List<byte[]> topicList = logInfo.getClonedTopics();
        byte[] data = logInfo.getClonedData();

        Map<String, String> topicMap = ContractEventParserJson
            .parseTopics(topicList, abiJsonMap.get(logHash));
        Map<String, String> dataMap = ContractEventParserJson
            .parseEventData(data, topicList, abiJsonMap.get(logHash));
        ContractEventTriggerEntity event = new ContractEventTriggerEntity(
            abiStrMap.get(logHash),
            topicMap,
            trigger.getLatestSolidifiedBlockNumber(),
            dataMap,
            trigger.getTransactionId(),
            trigger.getContractAddress(),
            trigger.getCallerAddress(),
            trigger.getOriginAddress(),
            trigger.getCreatorAddress(),
            trigger.getBlockNumber(),
            trigger.getRemoved(),
            trigger.getTimeStamp(),
            trigger.getTriggerName(),
            abiStrMap.get(logHash + "_full"),
            abiStrMap.get(logHash + "_name"),
            trigger.getUniqueId(),
            trigger.getRawData(),
            trigger.getAbiString()
        );
        result.add(event);
      }
    }

    return result;
  }

  public static List<ContractLogTriggerEntity> parseLogWithAbiByEvent(
      List<ContractEventTriggerEntity> triggers,
      String abi) {
    List<ContractLogTriggerEntity> result = new ArrayList<>();
    Map<String, String> abiStrMap = new HashMap<>();
    Map<String, JSONObject> abiJsonMap = new HashMap<>();

    parseAbi(abi, abiStrMap, abiJsonMap);

    for (ContractEventTriggerEntity trigger : triggers) {
      LogInfoEntity logInfoEntity = trigger.getRawData();
      if (logInfoEntity == null){
        continue;
      }
      List<DataWordEntity> topics = logInfoEntity.getTopics();
      List<DataWord> mTopics = new ArrayList<>();
      for (DataWordEntity t : topics) {
        mTopics.add(new DataWord(t.getData()));
      }
      LogInfo logInfo = new LogInfo(Hex.decode(logInfoEntity.getAddress()), mTopics, Hex.decode(logInfoEntity.getData()));
      String logHash = getLogHash(logInfo);

      if (abiStrMap.get(logHash) == null) {
        ContractLogTriggerEntity event = new ContractLogTriggerEntity(
            logInfo.getHexTopics(),
            logInfo.getHexData(),
            trigger.getTransactionId(),
            trigger.getContractAddress(),
            trigger.getCallerAddress(),
            trigger.getOriginAddress(),
            trigger.getCreatorAddress(),
            trigger.getBlockNumber(),
            trigger.getRemoved(),
            trigger.getLatestSolidifiedBlockNumber(),
            trigger.getTimeStamp(),
            trigger.getTriggerName(),
            trigger.getUniqueId(),
            trigger.getRawData(),
            trigger.getAbiString()
        );

        result.add(event);
      }
    }

    return result;
  }

  public static List<ContractEventTriggerEntity> parseEventWithAbiByEvent(
      List<ContractEventTriggerEntity> triggers,
      String abi) {
    List<ContractEventTriggerEntity> result = new ArrayList<>();
    Map<String, String> abiStrMap = new HashMap<>();
    Map<String, JSONObject> abiJsonMap = new HashMap<>();

    parseAbi(abi, abiStrMap, abiJsonMap);

    for (ContractEventTriggerEntity trigger : triggers) {
      LogInfoEntity logInfoEntity = trigger.getRawData();
      if (logInfoEntity == null){
        result.add(trigger);
        continue;
      }
      List<DataWordEntity> topics = logInfoEntity.getTopics();
      List<DataWord> mTopics = new ArrayList<>();
      for (DataWordEntity t : topics) {
        mTopics.add(new DataWord(t.getData()));
      }
      LogInfo logInfo = new LogInfo(Hex.decode(logInfoEntity.getAddress()), mTopics, Hex.decode(logInfoEntity.getData()));
      String logHash = getLogHash(logInfo);

      if (abiStrMap.get(logHash) != null) {
        List<byte[]> topicList = logInfo.getClonedTopics();
        byte[] data = logInfo.getClonedData();

        Map<String, String> topicMap = ContractEventParserJson
            .parseTopics(topicList, abiJsonMap.get(logHash));
        Map<String, String> dataMap = ContractEventParserJson
            .parseEventData(data, topicList, abiJsonMap.get(logHash));
        ContractEventTriggerEntity event = new ContractEventTriggerEntity(
            abiStrMap.get(logHash),
            topicMap,
            trigger.getLatestSolidifiedBlockNumber(),
            dataMap,
            trigger.getTransactionId(),
            trigger.getContractAddress(),
            trigger.getCallerAddress(),
            trigger.getOriginAddress(),
            trigger.getCreatorAddress(),
            trigger.getBlockNumber(),
            trigger.getRemoved(),
            trigger.getTimeStamp(),
            trigger.getTriggerName(),
            abiStrMap.get(logHash + "_full"),
            abiStrMap.get(logHash + "_name"),
            trigger.getUniqueId(),
            trigger.getRawData(),
            trigger.getAbiString()
        );
        result.add(event);
      }
    }

    return result;
  }

  public static void parseAbi(String abiString,
      Map<String, String> abiStrMap,
      Map<String, JSONObject> abiJsonMap) {

    JSONObject abi = null;
    JSONArray entrys = null;

    Object abiObj = JSON.parse(abiString);
    if (abiObj instanceof JSONObject) {
      abi = (JSONObject) abiObj;
      entrys = abi.getJSONArray("entrys");
    } else if (abiObj instanceof JSONArray) {
      entrys = (JSONArray) abiObj;
    }

    if (entrys != null) {
      for (int i = 0; i < entrys.size(); i++) {
        JSONObject entry = entrys.getJSONObject(i);

        String funcType = entry.getString("type");
        Boolean anonymous = entry.getBoolean("anonymous");
        if (funcType == null || !funcType.equalsIgnoreCase("event")) {
          continue;
        }

        if (anonymous != null && anonymous) {
          continue;
        }

        String inputStr = entry.getString("name") + "(";
        String inputFullStr = entry.getString("name") + "(";
        StringBuilder inputBuilder = new StringBuilder();
        StringBuilder inputFullBuilder = new StringBuilder();
        JSONArray inputs = entry.getJSONArray("inputs");
        if (inputs != null) {
          for (int j = 0; j < inputs.size(); j++) {
            if (inputBuilder.length() > 0) {
              inputBuilder.append(",");
              inputFullBuilder.append(",");
            }
            String type = inputs.getJSONObject(j).getString("type");
            String name = inputs.getJSONObject(j).getString("name");
            inputBuilder.append(type);
            inputFullBuilder.append(type);
            if (StringUtils.isNotNullOrEmpty(name)) {
              inputFullBuilder.append(" ").append(name);
            }
          }
        }
        inputStr += inputBuilder.toString() + ")";
        inputFullStr += inputFullBuilder.toString() + ")";
        String inputSha3 = Hex.toHexString(Hash.sha3(inputStr.getBytes()));

        abiStrMap.put(inputSha3, inputStr);
        abiStrMap.put(inputSha3 + "_full", inputFullStr);
        abiStrMap.put(inputSha3 + "_name", entry.getString("name"));
        abiJsonMap.put(inputSha3, entry);
      }
    }
  }

  public static String getLogHash(LogInfo logInfo) {
    String logHash = "";
    List<DataWord> topics = logInfo.getTopics();
    if (topics != null && !topics.isEmpty() && !ArrayUtils.isEmpty(topics.get(0).getData())) {
      logHash = topics.get(0).toString();
    }

    return logHash;
  }

  public static Pageable setPagniateVariable(int start, int size, String sort) {
    int page = start;
    int pageSize = size;
    return make_pagination(Math.max(0, page - 1), Math.min(200, pageSize), sort);
  }
}
