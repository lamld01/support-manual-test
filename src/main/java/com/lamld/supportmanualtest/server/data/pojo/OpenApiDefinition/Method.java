package com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Method {
    private List<String> tags  = new ArrayList<>();;
    private String operationId;
    private List<Parameter> parameters = new ArrayList<>();
    private Map<String, Response> responses;
    private RequestBody requestBody;
}
