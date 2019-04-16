package org.tron.trongeventquery.contractlogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

public class DataWord {
  @Field(value = "data")
  @JsonProperty(value = "data")
  private byte[] data = new byte[32];

  public DataWord(byte[] data) {
    this.data = data;
  }
}
