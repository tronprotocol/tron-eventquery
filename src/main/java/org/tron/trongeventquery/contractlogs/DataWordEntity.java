package org.tron.trongeventquery.contractlogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

public class DataWordEntity {

  @Field(value = "data")
  @JsonProperty(value = "data")
  @Getter
  private byte[] data = new byte[32];

  public DataWordEntity(byte[] data) {
    this.data = data;
  }
}
