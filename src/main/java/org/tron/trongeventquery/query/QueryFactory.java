package org.tron.trongeventquery.query;

import java.util.regex.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class QueryFactory {
  private Query query;

  public static final String findByContractAndEventSinceTimestamp = "{ 'contractAddress' : ?0, " +
      "'event_name': ?1,  " +
      "'$or' : [ {'block_timestamp' : ?2}, {'block_timestamp' : {$gt : ?2}} ], " +
      "'resource_Node' : {$exists : true} }";

  public static final String findByContractSinceTimeStamp = "{ 'contractAddress' : ?0, " +
      "'$or' : [ {'block_timestamp' : ?1}, {'block_timestamp' : {$gt : ?1}} ], " +
      "'resource_Node' : {$exists : true}}";

  public static Pageable make_pagination(int page_num, int page_size, String sort_property){

    if (sort_property.charAt(0) == '-')
      return PageRequest.of(page_num, page_size, Sort.Direction.DESC, sort_property.substring(1));

    return PageRequest.of(page_num, page_size, Sort.Direction.ASC, sort_property);
  }

  public static boolean isBool(String s) {
    return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
  }

  public QueryFactory(){
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
    criteria.orOperator(Criteria.where("contractType").is("TransferContract"), Criteria.where("contractType").is("TransferAssetContract"));
    this.query.addCriteria(criteria);
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

  public void setTimestampGreaterEqual (long timestamp) {
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

  public void setContractAddress (String addr) {
    this.query.addCriteria(Criteria.where("contractAddress").is(addr));
  }

  public void setPageniate(Pageable page){
    this.query.with(page);
  }

  public void setEventName (String event) {
    this.query.addCriteria(Criteria.where("eventName").is(event));
  }

  public void setBlockNum(long block){
    this.query.addCriteria(Criteria.where("blockNumber").is(block));
  }

  public void setBlockNumGte(long block){
    this.query.addCriteria(Criteria.where("blockNumber").gte(block));
  }

  public void setBlockNumSmall(long block){
    this.query.addCriteria(Criteria.where("blockNumber").lte(block));
  }
  public String toString (){
    return this.query.toString();
  }

  public Query getQuery() { return this.query; }

  public static Pageable setPagniateVariable(int start, int size, String sort) {
    int page = start;
    int pageSize = size;
    return make_pagination(Math.max(0,page - 1),Math.min(200,pageSize),sort);
  }
}
