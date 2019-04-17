package org.tron.trongeventquery.contractlogs;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tron.trongeventquery.contractevents.ContractEventTriggerEntity;
import org.tron.trongeventquery.query.QueryFactory;

@RestController
public class ContractWithAbiController {

  @Autowired
  MongoTemplate mongoTemplate;

  @RequestMapping(method = RequestMethod.POST,
      value = "/contract/contractAddress/{contractAddress}")
  public JSONObject findByContractAddressAndEntryName(
      @PathVariable String contractAddress,
      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
      @RequestParam(value = "eventName", required = false, defaultValue = "") String eventName,
      @RequestBody Map<String, String> hmap
  ) {

    QueryFactory query = new QueryFactory();
    query.setTimestampGreaterEqual(timestamp);
    if (blocknum != -1) {
      query.setBlockNumGte(blocknum);
    }
    query.setContractAddress(contractAddress);

    if (eventName.length() != 0) {
      query.setEventName(eventName);
    }
    query.setPageniate(QueryFactory.setPagniateVariable(start, limit, sort));

    List<ContractLogTriggerEntity> contractLogTriggerList = mongoTemplate.find(query.getQuery(),
        ContractLogTriggerEntity.class);

    if (contractLogTriggerList.size() == 0) {
      return null;
    }

    if (hmap.containsKey("abi") == false || hmap.get("abi").length() == 0) {
      return null;
    }

    String abi =  hmap.get("abi");

    List<ContractEventTriggerEntity> contractEventTriggerList = null;
    if (abi.length() != 0) {
      contractEventTriggerList = QueryFactory.parseEventWithAbi(contractLogTriggerList, abi);
      contractLogTriggerList = QueryFactory.parseLogWithAbi(contractLogTriggerList, abi);
    }

    Map map = new HashMap();
    if (contractLogTriggerList != null && contractLogTriggerList.size() != 0) {
      map.put("contractLogTriggers", contractLogTriggerList);
    }

    if (contractEventTriggerList != null && contractEventTriggerList.size() != 0) {
      map.put("contractEventTriggers", contractEventTriggerList);
    }

    return new JSONObject(map);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/contract/uniqueId/{uniqueId}")
  public JSONObject getEvent(
      @PathVariable(value = "uniqueId", required = false) String uniqueId,
      @RequestBody Map<String, String> hmap
  ) {

    QueryFactory query = new QueryFactory();
    query.setUniqueIdEqual(uniqueId);

    List<ContractLogTriggerEntity> contractLogTriggerList = mongoTemplate.find(query.getQuery(),
        ContractLogTriggerEntity.class);
    List<ContractEventTriggerEntity> contractEventTriggerList = null;

    if (contractLogTriggerList.size() == 0) {
      return null;
    }

    if (hmap.containsKey("abi") == false || hmap.get("abi").length() == 0) {
      return null;
    }

    String abi =  hmap.get("abi");

    if (abi.length() != 0) {
      contractEventTriggerList = QueryFactory.parseEventWithAbi(contractLogTriggerList, abi);
      contractLogTriggerList = QueryFactory.parseLogWithAbi(contractLogTriggerList, abi);
    }

    Map map = new HashMap();
    if (contractLogTriggerList != null && contractLogTriggerList.size() != 0) {
      map.put("contractLogTriggers", contractLogTriggerList);
    }

    if (contractEventTriggerList != null && contractEventTriggerList.size() != 0) {
      map.put("contractEventTriggers", contractEventTriggerList);
    }

    return new JSONObject(map);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/contract/transaction/{transactionId}")
  public JSONObject findOneByTransaction(@PathVariable String transactionId,
      @RequestBody Map<String, String> hmap) {
    QueryFactory query = new QueryFactory();
    query.setTransactionIdEqual(transactionId);
    List<ContractLogTriggerEntity> contractLogTriggerList = mongoTemplate.find(query.getQuery(),
        ContractLogTriggerEntity.class);
    List<ContractEventTriggerEntity> contractEventTriggerList = null;

    if (contractLogTriggerList.size() == 0) {
      return null;
    }

    if (hmap.containsKey("abi") == false || hmap.get("abi").length() == 0) {
      return null;
    }

    String abi =  hmap.get("abi");
    if (abi.length() != 0) {
      contractEventTriggerList = QueryFactory.parseEventWithAbi(contractLogTriggerList, abi);
      contractLogTriggerList = QueryFactory.parseLogWithAbi(contractLogTriggerList, abi);
    }

    Map map = new HashMap();
    if (contractLogTriggerList != null && contractLogTriggerList.size() != 0) {
      map.put("contractLogTriggers", contractLogTriggerList);
    }

    if (contractEventTriggerList != null && contractEventTriggerList.size() != 0) {
      map.put("contractEventTriggers", contractEventTriggerList);
    }

    return new JSONObject(map);
  }
}
