package com.lamld.supportmanualtest.app.response.importResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition.DataSchema;
import com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition.OpenApiDefinition;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiInfo {
  private String router;
  private List<String> pathVariables = new ArrayList<>();
  private List<String> parameters = new ArrayList<>();
  private String mappingBodyName;
  private List<FieldValidate> body;

  @Data
  public static class FieldValidate {
    private String name;
    private String type;
    private Integer minLength;
    private Integer maxLength;
    private List<String> enumValues;
    private String format;
    private String defaultValue;
    private DataSchema items;
    private String ref;
    private List<FieldValidate> body;

    public FieldValidate(String name, DataSchema properties) {
      this.name = name;
      this.type = properties.getType();
      this.minLength = properties.getMinLength();
      this.maxLength = properties.getMaxLength();
      this.enumValues = properties.getEnumValues();
      this.format = properties.getFormat();
      this.defaultValue = properties.getDefaultValue();
      this.items = properties.getItems();
      this.ref = properties.getRef();
    }
  }
}
