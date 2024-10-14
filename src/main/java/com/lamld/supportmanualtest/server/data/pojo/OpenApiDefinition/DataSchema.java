package com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataSchema {
  private String type;
  private String format;
  private List<String> enumValues;
  @JsonProperty("default")
  private String defaultValue;
  private DataSchema items;
  @JsonProperty("$ref")
  private String ref;
  private Integer minLength;
  private Integer maxLength;
}
