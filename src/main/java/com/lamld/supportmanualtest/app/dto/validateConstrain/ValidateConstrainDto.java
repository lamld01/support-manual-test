package com.lamld.supportmanualtest.app.dto.validateConstrain;

import com.lamld.supportmanualtest.server.data.type.StatusEnum;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link com.lamld.supportmanualtest.server.entities.ValidateConstrain}
 */
public record ValidateConstrainDto(@Size(max = 255) String constrainName, @Size(max = 1000) String description,
                                   String regexValue, StatusEnum status) implements Serializable {
}