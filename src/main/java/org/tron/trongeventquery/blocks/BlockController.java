package org.tron.trongeventquery.blocks;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tron.trongeventquery.BlockTriggerEntity;
import org.tron.trongeventquery.QueryFactory;

@RestController
@Component
public class BlockController {
  @Autowired(required = false)
  MongoTemplate mongoTemplate;

  @RequestMapping(method = RequestMethod.GET, value = "/totalblocks")
  public Long totalblock() {
    QueryFactory query = new QueryFactory();
    long number = mongoTemplate.count(query.getQuery(), BlockTriggerEntity.class);
    return number;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/blocks")
  public JSONObject getBlocks(
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
    List<BlockTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        BlockTriggerEntity.class);
    Map map = new HashMap();
    map.put("total", queryResult.size());
    map.put("data", queryResult);
    return new JSONObject(map);
  }
  @RequestMapping(method = RequestMethod.GET, value = "/blocks/{hash}")
  public JSONObject getBlockbyHash(
      @PathVariable String hash
  ) {

    QueryFactory query = new QueryFactory();
    query.setBlockHashEqual(hash);
    List<BlockTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        BlockTriggerEntity.class);
    if (queryResult.size() == 0) {
      return null;
    }
    Map map = new HashMap();
    map.put("block", queryResult.get(0));

    return new JSONObject(map);
  }
  @RequestMapping(method = RequestMethod.GET, value = "/blocks/latestblockNum")
  public long getLatestBlockNumber(
  ) {

    QueryFactory query = new QueryFactory();
    query.setPageniate(QueryFactory.setPagniateVariable(0, 1, "-latestSolidifiedBlockNumber"));
    List<BlockTriggerEntity> blockTriggerEntityList = mongoTemplate.find(query.getQuery(),
        BlockTriggerEntity.class);
    if (blockTriggerEntityList.isEmpty()) return 0;
    return blockTriggerEntityList.get(0).getLatestSolidifiedBlockNumber();
  }
}
