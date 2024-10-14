package com.lamld.supportmanualtest.app.dto.testApi;

import com.lamld.supportmanualtest.server.data.pojo.JsonInfo;
import com.lamld.supportmanualtest.server.data.pojo.KeyValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link com.lamld.supportmanualtest.server.entities.TestApi}
 */
@Data
@NoArgsConstructor
public class TestApiDto implements Serializable {
  @NotNull
  private Integer projectId;

  @Size(max = 255)
  private String apiName;

  @Size(max = 500)
  private String path;

  @Size(max = 1000)
  private String description;

  @Size(max = 50)
  private String method;
  private List<KeyValue> pathVariable;
  private List<KeyValue> param;

  private JsonInfo body;

  private List<KeyValue> header;


}
