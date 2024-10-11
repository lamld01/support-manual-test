package com.lamld.supportmanualtest.app.response.testField;

import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.app.response.validateConstrain.ValidateConstrainResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Response for {@link com.lamld.supportmanualtest.server.entities.TestField}
 */
public record TestFieldResponse(Integer id,
                                @Size(max = 255) String fieldName,
                                @NotNull ProjectResponse project,
                                @Size(max = 500) String description,
                                @NotNull @Size(max = 255) String fieldCode,
                                @Size(max = 255) String defaultRegexValue,
                                List<ValidateConstrainResponse> validateConstrains) {
}