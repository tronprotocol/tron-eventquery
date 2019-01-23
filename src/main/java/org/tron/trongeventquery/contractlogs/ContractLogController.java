package org.tron.trongeventquery.contractlogs;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tron.trongeventquery.contractevents.ContractEventTriggerEntity;
import org.tron.trongeventquery.query.QueryFactory;

@RestController
public class ContractLogController {
  @Autowired
  MongoTemplate mongoTemplate;

  @RequestMapping(method = RequestMethod.GET, value = "/contractlogs")
  public JSONObject getContractLogs(
      /******************* Page Parameters ****************************************************/
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
      /****************** Filter parameters *****************************************************/
      @RequestParam(value = "block", required = false, defaultValue = "-1") long block
  ) {
    QueryFactory query = new QueryFactory();
    if (block > 0) {
      query.setBlockNumGte(block);
    }
    query.setPageniate(QueryFactory.setPagniateVariable(start, limit, sort));
    List<ContractLogTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        ContractLogTriggerEntity.class);

    Map map = new HashMap();
    map.put("total", queryResult.size());
    map.put("data", queryResult);
    return new JSONObject(map);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/contractlogs/transaction/{transactionId}")
  public List<ContractLogTriggerEntity> findOneByTransaction(@PathVariable String transactionId) {
    QueryFactory query = new QueryFactory();
    query.setTransactionIdEqual(transactionId);
    List<ContractLogTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        ContractLogTriggerEntity.class);
    return queryResult;
  }

  @RequestMapping(method = RequestMethod.GET,
      value = "/contractlogs/contract/{contractAddress}")
  public List<ContractLogTriggerEntity> findByContractAddressAndEntryName(
      @PathVariable String contractAddress,
      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
      @RequestParam(value = "eventName", required = false, defaultValue = "") String eventName
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

    List<ContractLogTriggerEntity> result = mongoTemplate.find(query.getQuery(),
        ContractLogTriggerEntity.class);
    return result;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/contractlogs/uniqueId/{uniqueId}")
  public ContractLogTriggerEntity getEvent(
      @PathVariable(value = "uniqueId", required = false) String uniqueId
  ) {

    QueryFactory query = new QueryFactory();
    query.setUniqueIdEqual(uniqueId);

    List<ContractLogTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        ContractLogTriggerEntity.class);

    if (queryResult.size() == 0) {
      return null;
    }
    return queryResult.get(0);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/contractlogs/total")
  public Long totalContractLog() {
    QueryFactory query = new QueryFactory();
    long total = mongoTemplate.count(query.getQuery(),
        ContractLogTriggerEntity.class);

    return total;
  }
}
