package org.tron.trongeventquery.solidityevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "solidity")
public class SolidityTriggerEntity implements Serializable {

  private static final long serialVersionUID = -70777625567836430L;

  @Id
  private String id;

  @Field(value = "latestSolidifiedBlockNumber")
  @JsonProperty(value = "latestSolidifiedBlockNumber")
  private long latestSolidifiedBlockNumber;

  @Field(value = "timeStamp")
  @JsonProperty(value = "timeStamp")
  private long timeStamp;

  @Field(value = "triggerName")
  @JsonProperty(value = "triggerName")
  private String triggerName;

  public long getLatestSolidifiedBlockNumber() {
    return latestSolidifiedBlockNumber;
  }

  public SolidityTriggerEntity(Long timeStamp,
      String triggerName, long latestSolidifiedBlockNumber) {
    this.timeStamp = timeStamp;
    this.triggerName = triggerName;
    this.latestSolidifiedBlockNumber = latestSolidifiedBlockNumber;
  }
}
