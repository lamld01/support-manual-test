package com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestBody {
  private Content content;
  private boolean required; // True if the request body is required

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Content {
    @JsonProperty("application/json") // Mapping to the specific media type
    private MediaType applicationJson;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MediaType {
    private Schema schema; // Reference to the schema for the media type
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Schema {
    @JsonProperty("$ref")
    private String ref; // Reference to the schema
  }
}
