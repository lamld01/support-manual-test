package com.lamld.supportmanualtest.server.data.pojo;

import com.lamld.supportmanualtest.server.data.type.ObjectType;
import lombok.Data;

import java.util.List;

@Data
public class JsonInfo {

  private String id;

  private ObjectType type;

  private Integer value;

  private String name;

  private List<JsonInfo> children;
}
