package org.tron.trongeventquery.contractlogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
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

  public ContractLogTriggerEntity(
      List<String> topicList, String data,  String transactionId,
      String contractAddress, String callerAddress, String originAddress,
      String creatorAddress,  Long blockNumber, String removed,
      long latestSolidifiedBlockNumber, long timeStamp, String triggerName
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
  }
}
