package com.lamld.supportmanualtest.app.response.testApi;

import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.server.entities.Project;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Setter;

import java.util.Map;

/**
 * Response for {@link com.lamld.supportmanualtest.server.entities.TestApi}
 */
public record TestApiResponse(Integer id,
                              @NotNull ProjectResponse project,
                              @Size(max = 255) String apiName,
                              @Size(max = 1000) String description,
                              @Size(max = 50) String method,
                              Map<String, String> param,
                              Object body,
                              Map<String, String> header) {
}