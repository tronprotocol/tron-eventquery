package org.tron.trongeventquery.contractlogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

public class LogInfoEntity {

  @Field(value = "address")
  @JsonProperty(value = "address")
  @Getter
  private byte[] address = new byte[]{};

  @Field(value = "topics")
  @JsonProperty(value = "topics")
  @Getter
  private List<DataWordEntity> topics = new ArrayList<DataWordEntity>();

  @Field(value = "data")
  @JsonProperty(value = "data")
  @Getter
  private byte[] data = new byte[]{};

  public LogInfoEntity(byte[] address, List<DataWordEntity> topics,
      byte[] data) {
    this.address = address;
    this.topics = topics;
    this.data = data;
  }
}
