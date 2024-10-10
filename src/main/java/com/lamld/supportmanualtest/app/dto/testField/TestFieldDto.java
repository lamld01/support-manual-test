package com.lamld.supportmanualtest.app.dto.testField;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.lamld.supportmanualtest.server.entities.TestField}
 */
public record TestFieldDto(@Size(max = 255) String fieldName,
                           @NotNull Integer projectId,
                           @Size(max = 500) String description,
                           @NotNull @Size(max = 255) String fieldCode,
                           List<Integer> validateConstrainIds){
}