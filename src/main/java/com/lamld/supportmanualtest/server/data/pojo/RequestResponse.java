package com.lamld.supportmanualtest.server.data.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestResponse {
  int status;
  Object body; // Can hold a Map or a List of Maps
}
