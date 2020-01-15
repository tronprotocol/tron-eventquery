package org.tron.trongeventquery.contractevents;


import static org.tron.core.Wallet.decode58Check;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.spongycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tron.common.crypto.Crypto;
import org.tron.common.utils.ByteArray;
import org.tron.trongeventquery.query.QueryFactory;

@RestController
public class ContractEventController {
  public static final String ADD_PRE_FIX_STRING_MAINNET = "41";

  @Autowired
  MongoTemplate mongoTemplate;


  @RequestMapping(method = RequestMethod.GET, value = "/healthcheck")
  public String  healthCheck() {
    return "OK";
  }

  @RequestMapping(method = RequestMethod.GET, value = "/events")
  public List<ContractEventTriggerEntity> events(
      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start
  ) {

    QueryFactory query = new QueryFactory();
    if (blocknum != -1) {
      query.setBlockNumGte(blocknum);
    }
    query.setTimestampGreaterEqual(timestamp);
    query.setPageniate(QueryFactory.setPagniateVariable(start, limit, sort));
    List<ContractEventTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);

    return queryResult;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/events/total")
  public Long totalEvent() {
    QueryFactory query = new QueryFactory();
    long total = mongoTemplate.count(query.getQuery(),
        ContractEventTriggerEntity.class);

    return total;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/events/uniqueId/{uniqueId}")
  public ContractEventTriggerEntity getEvent(
      @PathVariable(value = "uniqueId", required = false) String uniqueId
  ) {

    QueryFactory query = new QueryFactory();
    query.setUniqueIdEqual(uniqueId);

    List<ContractEventTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);

    if (queryResult.size() == 0) {
      return null;
    }
    return queryResult.get(0);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/events/confirmed")
  public  List<ContractEventTriggerEntity> verifyEvents(
      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp,
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start
  ) {

    QueryFactory query = new QueryFactory();
    query.setPageniate(QueryFactory.setPagniateVariable(0, 1, "-latestSolidifiedBlockNumber"));
    List<ContractEventTriggerEntity> contractEventTriggerEntityList
        = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);
    if (contractEventTriggerEntityList.isEmpty()) {
      return null;
    }

    long latestSolidifiedBlockNumber =
        contractEventTriggerEntityList.get(0).getLatestSolidifiedBlockNumber();
    query = new QueryFactory();
    query.setBlockNumSmall(latestSolidifiedBlockNumber);
    query.setTimestampGreaterEqual(timestamp);
    query.setRemovedEqual(false);
    query.setPageniate(QueryFactory.setPagniateVariable(start, limit, sort));
    List<ContractEventTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);

    return queryResult;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/events/transaction/{transactionId}")
  public List<ContractEventTriggerEntity> findOneByTransaction(@PathVariable String transactionId) {
    QueryFactory query = new QueryFactory();
    query.setTransactionIdEqual(transactionId);
    List<ContractEventTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);
      return queryResult;
  }

  @RequestMapping(method = RequestMethod.GET,
      value = "/events/contract/{contractAddress}/{eventName}")
  public List<ContractEventTriggerEntity> findByContractAddressAndEntryName(
      @PathVariable String contractAddress,
      @PathVariable String eventName,
      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start) {

    QueryFactory query = new QueryFactory();
    query.setTimestampGreaterEqual(timestamp);
    if (blocknum != -1) {
      query.setBlockNumGte(blocknum);
    }
    query.setContractAddress(contractAddress);
    query.setEventName(eventName);
    query.setPageniate(QueryFactory.setPagniateVariable(start, limit, sort));

    List<ContractEventTriggerEntity> result = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);
    return result;
  }

  @RequestMapping(method = RequestMethod.GET,
      value = "/events/contract/{contractAddress}/{eventName}/{blockNumber}")
  public List<ContractEventTriggerEntity> findByContractAddressAndEntryNameAndBlockNumber(
      @PathVariable String contractAddress,
      @PathVariable String eventName,
      @PathVariable long blockNumber) {

    QueryFactory query = new QueryFactory();
    query.setContractAddress(contractAddress);
    query.setEventName(eventName);

    if (blockNumber != -1) {
      query.setBlockNum(blockNumber);
    }

    List<ContractEventTriggerEntity> result = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);
    return result;

  }

  @RequestMapping(method = RequestMethod.GET,
      value = "/events/filter/contract/{contractAddress}/{eventName}")
  public List<ContractEventTriggerEntity> filterevent(
      @PathVariable String contractAddress,
      @PathVariable String eventName,
      @RequestParam(value = "since", required = false, defaultValue = "0") Long timestamp,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start) {
    QueryFactory query = new QueryFactory();

    query.setContractAddress(contractAddress);
    query.setEventName(eventName);
    query.setTimestampGreaterEqual(timestamp);
    if (blocknum != -1) {
      query.setBlockNum(blocknum);
    }

    query.setPageniate(this.setPagniateVariable(limit, sort, start));
    List<ContractEventTriggerEntity> result =
        mongoTemplate.find(query.getQuery(), ContractEventTriggerEntity.class);
    return result;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/events/timestamp")
  public List<ContractEventTriggerEntity> findByBlockTimestampGreaterThan(
      @RequestParam(value = "contract", required = false, defaultValue = "") String contractAddress,
      @RequestParam(value = "since", required = false, defaultValue = "0") Long timestamp,
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start) {
    QueryFactory query = new QueryFactory();
    if (contractAddress.length() != 0) {
      query.setContractAddress(contractAddress);
    }
    query.setTimestampGreaterEqual(timestamp);
    query.setPageniate(this.setPagniateVariable(limit, sort, start));
    List<ContractEventTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);
    return queryResult;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/trc20/getholder/{contractAddress}")
  public  List<String> totalholder(
      @PathVariable String contractAddress
  ) {
    QueryFactory query = new QueryFactory();
    query.findAllTransferByAddress(contractAddress);

    List<ContractEventTriggerEntity> contractList =
        mongoTemplate.find(query.getQuery(), ContractEventTriggerEntity.class);
    Set<String> addressSet = new HashSet<>();
    for (ContractEventTriggerEntity contract : contractList) {
      Map<String, String> topMap = contract.getTopicMap();
      if (topMap.containsKey("_to") && topMap.containsKey("_from")) {
        addressSet.add(topMap.get("_to"));
        addressSet.add(topMap.get("_from"));
      }
    }

    return  addressSet.stream().collect(Collectors.toList());
  }

  private Pageable setPagniateVariable(int limit, String sort, int start) {

    // variables for pagniate
    int page = start;
    int pageSize = limit;

    return QueryFactory.make_pagination(Math.max(0,page - 1), Math.min(200,pageSize), sort);

  }

  // get event list
  @RequestMapping(method = RequestMethod.GET, value = "/events/{contractAddress}")
  public List<JSONObject> findEventsListByContractAddress(
      @PathVariable String contractAddress,
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp
  ) {

    QueryFactory query = new QueryFactory();
    if (blocknum != -1) {
      query.setBlockNumGte(blocknum);
    }
    query.setContractAddress(contractAddress);
    query.setTimestampGreaterEqual(timestamp);
    query.setPageniate(this.setPagniateVariable(limit, sort, start));
    List<ContractEventTriggerEntity> result = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);

    List<JSONObject> array = new ArrayList<>();
    for (ContractEventTriggerEntity p : result) {
      Map map = new HashMap();
      map.put("transactionId", p.getTransactionId());
      map.put("blockNumber", p.getBlockNumber());
      map.put("timeStamp", p.getTimeStamp());
      map.put("eventSignatureFull", p.getEventSignatureFull());
      map.put("eventName", p.getEventName());
      map.put("contractAddress", p.getContractAddress());
      int i = 0;
      Map<String, String> dataMap = p.getDataMap();
      Map<String, String> topicMap = p.getTopicMap();
      for (String topic : topicMap.keySet()) {
        dataMap.put(topic, topicMap.get(topic));
      }

      while (dataMap.containsKey(String.valueOf(i))) {
        map.put(String.valueOf(i), dataMap.get(String.valueOf(i)));
        i++;
      }
      array.add(new JSONObject(map));
    }

    return array;
  }

  // for tron web

  @RequestMapping(method = RequestMethod.GET, value = "/event/transaction/{transactionId}")
  public List<JSONObject> findOneByTransactionTronGri (@PathVariable String transactionId) {

    QueryFactory query = new QueryFactory();
    query.setTransactionIdEqual(transactionId);
    List<ContractEventTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);

    List<JSONObject> array = new ArrayList<>();
    for (ContractEventTriggerEntity p : queryResult) {
      Map map = new HashMap();
      map.put("transaction_id", p.getTransactionId());
      map.put("block_timestamp", String.valueOf(p.getTimeStamp()));
      map.put("block_number", p.getBlockNumber());
      map.put("result_type", getResultType(p.getEventSignatureFull(), p.getEventName()));
      map.put("result", getResult(p.getEventSignatureFull(), p.getEventName(), p.getTopicMap(), p.getDataMap()));
      map.put("event_index", getIndex(p.getUniqueId()));
      map.put("event_name", p.getEventName());
      map.put("contract_address", p.getContractAddress());
      map.put("caller_contract_address", p.getOriginAddress());

      array.add(new JSONObject(map));
    }

    return array;
  }

  // get event list
  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}")
  public List<JSONObject> findEventsByContractAddressTronGrid (
      @PathVariable String contractAddress,
      @RequestParam(value = "size", required = false, defaultValue = "20") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp,
      @RequestParam(value = "fromTimestamp", required = false, defaultValue = "0") long fromTimestamp,
      @RequestParam(value = "fingerprint", required = false, defaultValue = "") String fingerprint
  ) {
    if (sort.contains("block_timestamp")) {
      sort = sort.replace("block_timestamp", "timeStamp");
    }
    if (fromTimestamp > 0) {
      timestamp = fromTimestamp;
    }

    if (fingerprint.length() != 0) {
      start = Crypto.decrypt(fingerprint) > 0 ? Crypto.decrypt(fingerprint) : 0;
    }

    QueryFactory query = new QueryFactory();
    if (blocknum != -1) {
      query.setBlockNumGte(blocknum);
    }
    query.setContractAddress(contractAddress);
    query.setTimestampGreaterEqual(timestamp);
    query.setPageniate(this.setPagniateVariable(limit, sort, start));
    List<ContractEventTriggerEntity> result = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);

    List<JSONObject> array = new ArrayList<>();
    int count = 1;
    for (ContractEventTriggerEntity p : result) {
      Map map = new HashMap();
      map.put("transaction_id", p.getTransactionId());
      map.put("block_timestamp", String.valueOf(p.getTimeStamp()));
      map.put("block_number", p.getBlockNumber());
      map.put("result_type", getResultType(p.getEventSignatureFull(), p.getEventName()));
      map.put("result", getResult(p.getEventSignatureFull(), p.getEventName(), p.getTopicMap(), p.getDataMap()));
      map.put("event_index", getIndex(p.getUniqueId()));
      map.put("event_name", p.getEventName());
      map.put("contract_address", p.getContractAddress());
      map.put("caller_contract_address", p.getOriginAddress());

      if (count++ == result.size()) {
        map.put("_fingerprint", Crypto.encrypt(String.format("%d", start + 1)));
      }
      array.add(new JSONObject(map));
    }

    return array;
  }

  // get event list
  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}/{eventName}")
  public List<JSONObject> findEventsByContractAddressAndEventNameTronGrid (
      @PathVariable String contractAddress,
      @PathVariable String eventName,
      @RequestParam(value = "size", required = false, defaultValue = "20") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp,
      @RequestParam(value = "fromTimestamp", required = false, defaultValue = "0") long fromTimestamp,
      @RequestParam(value = "fingerprint", required = false, defaultValue = "") String fingerprint
  ) {
    if (sort.contains("block_timestamp")) {
      sort = sort.replace("block_timestamp", "timeStamp");
    }
    if (fromTimestamp > 0) {
      timestamp = fromTimestamp;
    }

    if (fingerprint.length() != 0) {
      start = Crypto.decrypt(fingerprint) > 0 ? Crypto.decrypt(fingerprint) : 0;
    }

    QueryFactory query = new QueryFactory();
    if (blocknum != -1) {
      query.setBlockNumGte(blocknum);
    }
    query.setContractAddress(contractAddress);
    query.setEventName(eventName);

    query.setTimestampGreaterEqual(timestamp);
    query.setPageniate(this.setPagniateVariable(limit, sort, start));
    List<ContractEventTriggerEntity> result = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);

    List<JSONObject> array = new ArrayList<>();
    int count = 1;
    for (ContractEventTriggerEntity p : result) {
      Map map = new HashMap();
      map.put("transaction_id", p.getTransactionId());
      map.put("block_timestamp", String.valueOf(p.getTimeStamp()));
      map.put("block_number", p.getBlockNumber());
      map.put("result_type", getResultType(p.getEventSignatureFull(), p.getEventName()));
      map.put("result", getResult(p.getEventSignatureFull(), p.getEventName(), p.getTopicMap(), p.getDataMap()));
      map.put("event_index", getIndex(p.getUniqueId()));
      map.put("event_name", p.getEventName());
      map.put("contract_address", p.getContractAddress());
      map.put("caller_contract_address", p.getOriginAddress());

      if (count++ == result.size()) {
        map.put("_fingerprint", Crypto.encrypt(String.format("%d", start + 1)));
      }
      array.add(new JSONObject(map));
    }

    return array;
  }

  // get event list
  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}/{eventName}/{blockNum}")
  public List<JSONObject> findEventsByContractAddressAndEventNameAndBlockNumTronGrid (
      @PathVariable String contractAddress,
      @PathVariable String eventName,
      @PathVariable Long blockNum,
      @RequestParam(value = "size", required = false, defaultValue = "20") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp,
      @RequestParam(value = "fromTimestamp", required = false, defaultValue = "0") long fromTimestamp,
      @RequestParam(value = "fingerprint", required = false, defaultValue = "") String fingerprint
  ) {
    if (sort.contains("block_timestamp")) {
      sort = sort.replace("block_timestamp", "timeStamp");
    }

    if (fromTimestamp > 0) {
      timestamp = fromTimestamp;
    }

    if (fingerprint.length() != 0) {
      start = Crypto.decrypt(fingerprint) > 0 ? Crypto.decrypt(fingerprint) : 0;
    }

    QueryFactory query = new QueryFactory();
    if (blocknum != -1) {
      query.setBlockNumGte(blocknum);
    }
    query.setContractAddress(contractAddress);
    query.setEventName(eventName);
    query.setBlockNum(blockNum);

    query.setTimestampGreaterEqual(timestamp);
    query.setPageniate(this.setPagniateVariable(limit, sort, start));
    List<ContractEventTriggerEntity> result = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);

    List<JSONObject> array = new ArrayList<>();
    int count = 1;
    for (ContractEventTriggerEntity p : result) {
      Map map = new HashMap();
      map.put("transaction_id", p.getTransactionId());
      map.put("block_timestamp", String.valueOf(p.getTimeStamp()));
      map.put("block_number", p.getBlockNumber());
      map.put("result_type", getResultType(p.getEventSignatureFull(), p.getEventName()));
      map.put("result", getResult(p.getEventSignatureFull(), p.getEventName(), p.getTopicMap(), p.getDataMap()));
      map.put("event_index", getIndex(p.getUniqueId()));
      map.put("event_name", p.getEventName());
      map.put("contract_address", p.getContractAddress());
      map.put("caller_contract_address", p.getOriginAddress());

      if (count++ == result.size()) {
        map.put("_fingerprint", Crypto.encrypt(String.format("%d", start + 1)));
      }
      array.add(new JSONObject(map));
    }

    return array;
  }

  List<JSONObject> getResultType(String fullName, String eventSignature) {
    int num = eventSignature.length();
    String newSignature = fullName.substring(num + 1, fullName.length() - 1);
    String[] arrayList = Strings.split(newSignature, ',');
    Map map = new HashMap();
    List<JSONObject> array = new ArrayList<>();
    for (String str : arrayList) {
      String[] type = str.split(" ");
      map.put(type[1], type[0]);
    }
    array.add(new JSONObject(map));
    return array;
  }

  List<JSONObject> getResult(String fullName, String eventSignature, Map<String, String> topMap, Map<String, String> dataMap) {
    int num = eventSignature.length();
    String newSignature = fullName.substring(num + 1, fullName.length() - 1);
    String[] arrayList = Strings.split(newSignature, ',');
    Map map = new HashMap();
    List<JSONObject> array = new ArrayList<>();
    int i = 0;
    for (String str : arrayList) {
      String[] type = str.split(" ");

      String ans;
      String key = String.format("%d", i);
      if (topMap.containsKey(key)) {
        ans = topMap.get(key);
      } else {
        ans = dataMap.get(key);
      }

      String tmp = ans;
      if (type[0].equals("address")) {
        ans = ByteArray.toHexString(decode58Check(ans));
        if (StringUtils.startsWith(ans, ADD_PRE_FIX_STRING_MAINNET)) {
          ans =  "0x" + ans.substring(2);
        } else {
          ans = tmp;
        }
      }

      i ++;
      map.put(type[1], ans);
    }
    array.add(new JSONObject(map));
    return array;
  }

  int getIndex(String unique) {
    String[]id = unique.split("_");
    return Integer.parseInt(id[1]) - 1;
  }

}
