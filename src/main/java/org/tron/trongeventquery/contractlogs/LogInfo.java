package org.tron.trongeventquery.contractlogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Field;

public class LogInfo {

  @Field(value = "address")
  @JsonProperty(value = "address")
  private byte[] address = new byte[]{};

  @Field(value = "topics")
  @JsonProperty(value = "topics")
  private List<DataWord> topics = new ArrayList<DataWord>();

  @Field(value = "data")
  @JsonProperty(value = "data")
  private byte[] data = new byte[]{};

  public LogInfo(byte[] address, List<DataWord> topics,
      byte[] data ) {
    this.address = address;
    this.topics = topics;
    this.data = data;
  }
}
