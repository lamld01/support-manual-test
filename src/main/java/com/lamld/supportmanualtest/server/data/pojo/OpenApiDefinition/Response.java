package com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String description;
    private Map<String, MediaType> content;
}
