package com.lamld.supportmanualtest.app.response.testApi;

import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.server.data.pojo.JsonInfo;
import com.lamld.supportmanualtest.server.data.pojo.KeyValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Response for {@link com.lamld.supportmanualtest.server.entities.TestApi}
 */
@Getter
@Setter
@NoArgsConstructor
public class TestApiResponse {
  private Integer id;

  @NotNull
  private ProjectResponse project;

  @Size(max = 255)
  private String apiName;

  @Size(max = 1000)
  private String description;

  @Size(max = 50)
  private String method;

  @Size(max = 500)
  private String path;

  private List<KeyValue> pathVariable;

  private List<KeyValue> param;

  private JsonInfo body;

  private List<KeyValue> header;

  // Constructor
  public TestApiResponse(Integer id, ProjectResponse project, String apiName, String description, String method, String path, List<KeyValue> pathVariable, List<KeyValue> param, JsonInfo body, List<KeyValue> header) {
    this.id = id;
    this.project = project;
    this.apiName = apiName;
    this.description = description;
    this.method = method;
    this.path = path;
    this.pathVariable = pathVariable;
    this.param = param;
    this.body = body;
    this.header = header;
  }

  // Optional: You can add additional methods here if needed
}
