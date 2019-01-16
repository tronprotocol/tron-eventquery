package org.tron.trongeventquery;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "block")
public class BlockTriggerEntity implements Serializable {

  private static final long serialVersionUID = -70777625567836430L;

  @Id
  private String id;

  @Field(value = "blockNumber")
  @JsonProperty(value = "blockNumber")
  private long blockNumber;

  @Field(value = "blockHash")
  @JsonProperty(value = "blockHash")
  private String blockHash;

  @Field(value = "transactionSize")
  @JsonProperty(value = "transactionSize")
  private long transactionSize;

  @Field(value="transactionList")
  @JsonProperty(value="transactionList")
  private List<String> transactionList;

  @Field(value = "timeStamp")
  @JsonProperty(value = "timeStamp")
  private Long timeStamp;

  @Field(value = "triggerName")
  @JsonProperty(value = "triggerName")
  private String triggerName;


  public BlockTriggerEntity(long blockNumber, String blockHash,  long transactionSize,
      List<String> transactionList, Long timeStamp,String triggerName) {
    this.blockNumber = blockNumber;
    this.blockHash = blockHash;
    this.transactionSize = transactionSize;
    this.transactionList = transactionList;
    this.timeStamp = timeStamp;
    this.triggerName = triggerName;
  }

}
