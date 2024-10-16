package com.lamld.supportmanualtest.server.data.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyValue {

  private String key;

  private String value;

  public  static KeyValue createAuthorityToken(String value) {
    KeyValue keyValue = new KeyValue();
    keyValue.setKey("Authorization");
    keyValue.setValue("Bearer " + value);
    return keyValue;
  }
}
