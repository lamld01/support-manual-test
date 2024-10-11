package com.lamld.supportmanualtest.app.dto.testApi;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO for {@link com.lamld.supportmanualtest.server.entities.TestApi}
 */
public record TestApiDto(@NotNull Integer projectId,
                         @Size(max = 255) String apiName,
                         @Size(max = 1000) String description,
                         @Size(max = 50) String method,
                         Map<String, String> param,
                         Object body,
                         Map<String, String> header) {
}