package org.tron.trongeventquery.contractlogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "contractlog")
public class ContractLogTriggerEntity {

  @Field(value = "topicList")
  @JsonProperty(value = "topicList")
  private List<String> topicList;

  @Field(value = "data")
  @JsonProperty(value = "data")
  private String data;

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

  @Field(value = "uniqueId")
  @JsonProperty(value = "uniqueId")
  @Getter
  private String uniqueId;

  @Field(value = "abiString")
  @JsonProperty(value = "abiString")
  @Getter
  private String abiString;

  @Field(value = "rawData")
  @JsonProperty(value = "rawData")
  @Getter
  private LogInfoEntity rawData;


  public ContractLogTriggerEntity(
      List<String> topicList, String data, String transactionId,
      String contractAddress, String callerAddress, String originAddress,
      String creatorAddress, Long blockNumber, String removed,
      long latestSolidifiedBlockNumber, long timeStamp, String triggerName, String uniqueId,
      LogInfoEntity rawData, String abiString
  ) {
    this.topicList = topicList;
    this.data = data;
    this.transactionId = transactionId;
    this.contractAddress = contractAddress;
    this.callerAddress = callerAddress;
    this.originAddress = originAddress;
    this.creatorAddress = creatorAddress;
    this.blockNumber = blockNumber;
    this.removed = removed;
    this.latestSolidifiedBlockNumber = latestSolidifiedBlockNumber;
    this.timeStamp = timeStamp;
    this.triggerName = triggerName;
    this.uniqueId = uniqueId;
    this.rawData = rawData;
    this.abiString = abiString;
  }
}
