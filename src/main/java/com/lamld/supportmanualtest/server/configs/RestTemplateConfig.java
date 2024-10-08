package com.lamld.supportmanualtest.server.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
@Slf4j
public class RestTemplateConfig {

  @Bean
  public RestTemplate httpRestTemplate() {
    return new RestTemplate();
  }

}
