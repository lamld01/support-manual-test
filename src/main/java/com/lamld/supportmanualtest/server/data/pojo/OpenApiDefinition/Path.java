package com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Path {
    private Method post;
    private Method get;
    private Method put;
    private Method delete;
}
