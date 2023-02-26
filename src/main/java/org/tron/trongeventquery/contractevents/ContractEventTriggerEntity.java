package org.tron.trongeventquery.contractevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.tron.trongeventquery.contractlogs.LogInfoEntity;

@Document(collection = "contractevent")
public class ContractEventTriggerEntity {

  private static final long serialVersionUID = -70777625567836430L;

  @Id
  private String id;

  @Field(value = "eventSignature")
  @JsonProperty(value = "eventSignature")
  @Getter
  private String eventSignature;

  @Field(value = "topicMap")
  @JsonProperty(value = "topicMap")
  @Getter
  private Map<String, String> topicMap;

  @Field(value = "dataMap")
  @JsonProperty(value = "dataMap")
  @Getter
  private Map<String, String> dataMap;

  @Field(value = "transactionId")
  @JsonProperty(value = "transactionId")
  @Getter
  private String transactionId;

  @Field(value = "contractAddress")
  @JsonProperty(value = "contractAddress")
  @Getter
  private String contractAddress;

  @Field(value = "callerAddress")
  @JsonProperty(value = "callerAddress")
  @Getter
  private String callerAddress;

  @Field(value = "originAddress")
  @JsonProperty(value = "originAddress")
  @Getter
  private String originAddress;

  @Field(value = "creatorAddress")
  @JsonProperty(value = "creatorAddress")
  @Getter
  private String creatorAddress;

  @Field(value = "blockNumber")
  @JsonProperty(value = "blockNumber")
  @Getter
  private Long blockNumber;

  @Field(value = "removed")
  @JsonProperty(value = "removed")
  @Getter
  private String removed;

  @Field(value = "latestSolidifiedBlockNumber")
  @JsonProperty(value = "latestSolidifiedBlockNumber")
  @Getter
  private long latestSolidifiedBlockNumber;

  @Field(value = "timeStamp")
  @JsonProperty(value = "timeStamp")
  @Getter
  protected long timeStamp;

  @Field(value = "triggerName")
  @JsonProperty(value = "triggerName")
  @Getter
  private String triggerName;

  @Field(value = "eventSignatureFull")
  @JsonProperty(value = "eventSignatureFull")
  @Getter
  private String eventSignatureFull;

  @Field(value = "eventName")
  @JsonProperty(value = "eventName")
  @Getter
  private String eventName;

  @Field(value = "uniqueId")
  @JsonProperty(value = "uniqueId")
  @Getter
  private String uniqueId;

  @Field(value = "rawData")
  @JsonProperty(value = "rawData")
  @Getter
  private LogInfoEntity rawData;

  public ContractEventTriggerEntity(String eventSignature, Map<String, String> topicMap,
      long latestSolidifiedBlockNumber, Map<String, String> dataMap,
      String transactionId, String contractAddress,
      String callerAddress, String originAddress,
      String creatorAddress, Long blockNumber, String removed, long timeStamp, String triggerName,
      String eventSignatureFull, String eventName, String uniqueId,
      LogInfoEntity rawData) {
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
    this.uniqueId = uniqueId;
    this.rawData = rawData;
  }
}
