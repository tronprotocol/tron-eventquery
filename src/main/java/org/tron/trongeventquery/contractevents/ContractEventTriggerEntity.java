package org.tron.trongeventquery.contractevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "contractevent")
public class ContractEventTriggerEntity {
  private static final long serialVersionUID = -70777625567836430L;

  @Id
  private String id;

  @Field(value = "eventSignature")
  @JsonProperty(value = "eventSignature")
  private String eventSignature;

  @Field(value = "topicMap")
  @JsonProperty(value = "topicMap")
  private Map<String, String> topicMap;

  @Field(value = "dataMap")
  @JsonProperty(value = "dataMap")
  private Map<String, String> dataMap;

  @Field(value = "transactionId")
  @JsonProperty(value = "transactionId")
  private String transactionId;

  @Field(value = "contractAddress")
  @JsonProperty(value = "contractAddress")
  private String contractAddress;

  @Field(value = "callerAddress")
  @JsonProperty(value = "callerAddress")
  private String callerAddress;

  @Field(value = "originAddress")
  @JsonProperty(value = "originAddress")
  private String originAddress;

  @Field(value = "creatorAddress")
  @JsonProperty(value = "creatorAddress")
  private String creatorAddress;

  @Field(value = "blockNumber")
  @JsonProperty(value = "blockNumber")
  private Long blockNumber;

  @Field(value = "removed")
  @JsonProperty(value = "removed")
  private String removed;

  @Field(value = "latestSolidifiedBlockNumber")
  @JsonProperty(value = "latestSolidifiedBlockNumber")
  private long latestSolidifiedBlockNumber;

  @Field(value = "timeStamp")
  @JsonProperty(value = "timeStamp")
  protected long timeStamp;

  @Field(value = "triggerName")
  @JsonProperty(value = "triggerName")
  private String triggerName;

  @Field(value = "eventSignatureFull")
  @JsonProperty(value = "eventSignatureFull")
  private String eventSignatureFull;

  @Field(value = "eventName")
  @JsonProperty(value = "eventName")
  private String eventName;

  public long getLatestSolidifiedBlockNumber() {
    return latestSolidifiedBlockNumber;
  }

  public Map<String, String> getTopicMap() {
    return topicMap;
  }

  public String getContractAddress() {
    return contractAddress;
  }

  public String getTransactionId() { return transactionId; }

  public Long getBlockNumer() {
    return blockNumber;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public String getEventSignatureFull() {
    return eventSignatureFull;
  }

  public String getEventName() {
    return eventName;
  }

  public Map<String, String> getDataMap() {
    return dataMap;
  }

  public ContractEventTriggerEntity(String eventSignature, Map<String, String> topicMap,
      long latestSolidifiedBlockNumber, Map<String, String> dataMap, String transactionId, String contractAddress,
      String callerAddress, String originAddress,
      String creatorAddress, Long blockNumber,String removed,long timeStamp, String triggerName,
      String eventSignatureFull, String eventName) {
    this.eventSignature = eventSignature;
    this.topicMap = topicMap;
    this.dataMap = dataMap;
    this.transactionId = transactionId;
    this.contractAddress = contractAddress;
    this.callerAddress = callerAddress;
    this.originAddress = originAddress;
    this.creatorAddress = creatorAddress;
    this.blockNumber = blockNumber;
    this.removed = removed;
    this.timeStamp = timeStamp;
    this.triggerName = triggerName;
    this.eventSignatureFull = eventSignatureFull;
    this.eventName = eventName;
    this.latestSolidifiedBlockNumber = latestSolidifiedBlockNumber;
  }

}
