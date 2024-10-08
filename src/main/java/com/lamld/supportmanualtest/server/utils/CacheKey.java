package com.lamld.supportmanualtest.server.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CacheKey {

  @Value("${redis.prefix-key}")
  public String redisPrefixKey;

}
