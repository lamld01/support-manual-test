package com.lamld.supportmanualtest.app.response.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link com.lamld.supportmanualtest.server.entities.Project}
 */
public record ProjectResponse(Integer id, @NotNull Integer accountId, @NotNull Integer parentProjectId,
                              @NotNull Integer rootProjectId, @Size(max = 255) String projectName,
                              @Size(max = 255) String apiBaseUrl,
                              @Size(max = 1000) String token) implements Serializable {
}