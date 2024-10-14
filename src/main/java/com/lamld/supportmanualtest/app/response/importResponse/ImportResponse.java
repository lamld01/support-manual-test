package com.lamld.supportmanualtest.app.response.importResponse;

import lombok.Data;

import java.util.List;

@Data
public class ImportResponse {

  private String projectName;
  private String url;

  private List<ApiInfo> apiInfos;
}
