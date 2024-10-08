package com.lamld.supportmanualtest.app.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(@Schema(description = "Dữ liệu của trang") List<T> data, Metadata metadata) {

  public static <T> PageResponse<T> createFrom(Page<T> pageData) {
    return new PageResponse<>(pageData.getContent(), Metadata.createFrom(pageData));
  }
}
