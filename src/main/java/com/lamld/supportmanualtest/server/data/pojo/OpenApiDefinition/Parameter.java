package com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {
    private String name;
    private String in;
    private boolean required;
    private DataSchema schema;
}
