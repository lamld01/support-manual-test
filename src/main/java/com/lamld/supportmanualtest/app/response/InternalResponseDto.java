package com.lamld.supportmanualtest.app.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for internal response messages.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternalResponseDto {
    private String message;
}
