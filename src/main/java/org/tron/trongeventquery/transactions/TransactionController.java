package org.tron.trongeventquery.transactions;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tron.trongeventquery.QueryFactory;
import org.tron.trongeventquery.TransactionTriggerEntity;

@RestController
@Component
@PropertySource("classpath:tronscan.properties")
public class TransactionController {
  @Autowired(required = false)
  MongoTemplate mongoTemplate;

  @RequestMapping(method = RequestMethod.GET, value = "/totaltransactions")
  public Long totaltransaction() {
    QueryFactory query = new QueryFactory();
    long number = mongoTemplate.count(query.getQuery(), TransactionTriggerEntity.class);
    return number;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/transactions")
  public JSONObject getTranssactions(
      /******************* Page Parameters ****************************************************/
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "count", required = false, defaultValue = "true") boolean count,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
      @RequestParam(value = "total", required = false, defaultValue = "0") Long total,
      /****************** Filter parameters *****************************************************/
      @RequestParam(value = "block", required = false, defaultValue = "-1") long block
  ) {
    QueryFactory query = new QueryFactory();
    if (block > 0) {
      query.setBlockNumGte(block);
    }
    query.setPageniate(QueryFactory.setPagniateVariable(start, limit, sort));
    List<TransactionTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        TransactionTriggerEntity.class);
    Map map = new HashMap();
    if (count) {
      map.put("total", queryResult.size());
    }
    map.put("data", queryResult);
    return new JSONObject(map);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/transactions/{hash}")
  public JSONObject getTransactionbyHash(
      @PathVariable String hash
  ) {

    QueryFactory query = new QueryFactory();
    query.setTransactionIdEqual(hash);
    List<TransactionTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        TransactionTriggerEntity.class);
    if (queryResult.size() == 0) {
      return null;
    }
    Map map = new HashMap();
    map.put("transaction", queryResult.get(0));

    return new JSONObject(map);
  }
}
