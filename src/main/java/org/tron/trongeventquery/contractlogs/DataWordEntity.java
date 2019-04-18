package org.tron.trongeventquery.contractlogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

public class DataWordEntity {

  @Field(value = "data")
  @JsonProperty(value = "data")
  @Getter
  private String data;

  public DataWordEntity(String data) {
    this.data = data;
  }
}
