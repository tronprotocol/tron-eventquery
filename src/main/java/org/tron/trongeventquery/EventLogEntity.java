package org.tron.trongeventquery;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "eventLog")
public class EventLogEntity implements Serializable {

  private static final long serialVersionUID = -70777625567836430L;

  @Id
  private String id;

  @Field(value = "block_number")
  @JsonProperty(value = "block_number")
  private long blockNumber;

  @Field(value = "block_timestamp")
  @JsonProperty(value = "block_timestamp")
  private long blockTimestamp;

  @Field(value = "contract_address")
  @JsonProperty(value = "contract_address")
  private String contractAddress;

  @Field(value = "event_index")
  @JsonProperty(value = "event_index")
  private int eventIndex;

  @Field(value = "event_name")
  @JsonProperty(value = "event_name")
  private String entryName;

  @Field(value = "result")
  @JsonProperty(value = "result")
  private Object resultJsonArray;

  @Field(value = "result_type")
  @JsonProperty(value = "result_type")
  private JSONObject resultType;

  @Field(value = "transaction_id")
  @JsonProperty(value = "transaction_id")
  private String transactionId;

  @Field(value = "resource_Node")
  @JsonProperty(value = "resource_Node")
  private String resource;



  public EventLogEntity(long blockNumber, long blockTimestamp, String contractAddress,
      int eventIndex, String entryName, Object resultJsonArray,String transactionId,
      JSONObject resultType, String resource
  ) {
    this.blockNumber = blockNumber;
    this.blockTimestamp = blockTimestamp;
    this.contractAddress = contractAddress;
    this.entryName = entryName;
    this.resultJsonArray = resultJsonArray;
    this.transactionId = transactionId;
    this.resultType = resultType;
    this.resource = resource;
    this.eventIndex = eventIndex;

  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public long getBlockNumber() {
    return blockNumber;
  }

  public void setBlockNumber(long blockNumber) {
    this.blockNumber = blockNumber;
  }

  public long getBlockTimestamp() {
    return blockTimestamp;
  }

  public void setBlockTimestamp(long blockTimestamp) {
    this.blockTimestamp = blockTimestamp;
  }

  public String getContractAddress() {
    return contractAddress;
  }

  public void setContractAddress(String contractAddress) {
    this.contractAddress = contractAddress;
  }

  public String getEntryName() {
    return entryName;
  }

  public void setEntryName(String entryName) {
    this.entryName = entryName;
  }

  public Object getResultJsonArray() {
    return resultJsonArray;
  }

  public void setResultJsonArray(Object resultJsonArray) {
    this.resultJsonArray = resultJsonArray;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public void setResultType(JSONObject res) {
    this.resultType = res;
  }

  public JSONObject getResultType() {
    return this.resultType;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public String getResource() {
    return this.resource;
  }

  public void setEventIndex(int idx) {
    this.eventIndex = idx;
  }

  public int getEventIndex() {
    return this.eventIndex;
  }
}
