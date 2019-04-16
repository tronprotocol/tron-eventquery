package org.tron.trongeventquery.contractlogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tron.trongeventquery.query.QueryFactory;

@RestController
public class ContractWithAbiController {

  @Autowired
  MongoTemplate mongoTemplate;

  @RequestMapping(method = RequestMethod.POST,
      value = "/contractwithabi/contract/{contractAddress}")
  public List<ContractLogTriggerEntity> findByContractAddressAndEntryName(
      @PathVariable String contractAddress,
      @RequestParam(value = "since", required = false, defaultValue = "0") long timestamp,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long blocknum,
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
      @RequestParam(value = "eventName", required = false, defaultValue = "") String eventName,
      @RequestParam(value = "abi", required = false, defaultValue = "") String abi
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

    if (abi.length() != 0) {
      result = QueryFactory.parseLogWithAbi(result, abi);
    }

    return result;
  }
}