package org.tron.trongeventquery.contractevents;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tron.trongeventquery.query.QueryFactory;

@RestController
public class ContractEventController {
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

    if (queryResult.size() == 0) return null;
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
    List<ContractEventTriggerEntity> contractEventTriggerEntityList = mongoTemplate.find(query.getQuery(),
        ContractEventTriggerEntity.class);
    if (contractEventTriggerEntityList.isEmpty()) return null;

    long latestBlockNumber = contractEventTriggerEntityList.get(0).getLatestSolidifiedBlockNumber();
    query = new QueryFactory();
    query.setBlockNumSmall(latestBlockNumber);
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

//  @RequestMapping(method = RequestMethod.GET, value = "/events/contractAddress/{contractAddress}")
//  public List<ContractEventTriggerEntity> findByContractAddress(@PathVariable String contractAddress,
//      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp,
//      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
//      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
//      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
//      @RequestParam(value = "start", required = false, defaultValue = "0") int start) {
//    QueryFactory query = new QueryFactory();
//    query.setContractAddress(contractAddress);
//    query.setTimestampGreaterEqual(timestamp);
//    if (blocknum != -1) {
//      query.setBlockNumGte(blocknum);
//    }
//    query.setPageniate(QueryFactory.setPagniateVariable(start, limit, sort));
//
//    List<ContractEventTriggerEntity> result = mongoTemplate.find(query.getQuery(),
//        ContractEventTriggerEntity.class);
//    return result;
//  }

  // get event list
  @RequestMapping(method = RequestMethod.GET, value = "/events/{contractAddress}")
  public List<JSONObject> findEventsListByContractAddress
      (@PathVariable String contractAddress,
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
    for(ContractEventTriggerEntity p : result) {
      Map map = new HashMap();
      map.put("transactionId", p.getTransactionId());
      map.put("blockNumber", p.getBlockNumer());
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
      @RequestParam(value = "since", required = false, defaultValue = "0") Long sinceTimestamp,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start) {
    QueryFactory query = new QueryFactory();

    query.setContractAddress(contractAddress);
    query.setEventName(eventName);
    query.setTimestampGreaterEqual(sinceTimestamp);

    query.setPageniate(this.setPagniateVariable(limit, sort, start));
    System.out.println(query.toString());
    List<ContractEventTriggerEntity> result = mongoTemplate.find(query.getQuery(), ContractEventTriggerEntity.class);
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

    List<ContractEventTriggerEntity> contractList = mongoTemplate.find(query.getQuery(), ContractEventTriggerEntity.class);
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

    return QueryFactory.make_pagination(Math.max(0,page - 1),Math.min(200,pageSize),sort);

  }

}
