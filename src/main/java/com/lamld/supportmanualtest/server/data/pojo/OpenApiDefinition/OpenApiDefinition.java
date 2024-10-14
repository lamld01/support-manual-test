package com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiDefinition {

    private String openapi;
    private Info info;
    private List<Server> servers;
    private List<Map<String, Object>> security;
    private Map<String, Path> paths;
    private Components components;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String title;
        private String version;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Server {
        private String url;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Path {
        private Method post;
        private Method get;
        private Method put;
        private Method delete;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Components {
        private Map<String, ComponentSchema> schemas;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentSchema {
        private String type;
        private List<String> required;
        private Map<String, DataSchema> properties;
    }


}
