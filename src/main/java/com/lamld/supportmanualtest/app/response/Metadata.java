package com.lamld.supportmanualtest.app.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

public record Metadata(@Schema(example = "10", description = "Tổng số bản ghi") long total,
                       @Schema(example = "1", description = "Tổng số trang") long totalPages) {
    public static Metadata createFrom(Page page) {
        return new Metadata(page.getTotalElements(), page.getTotalPages());
    }
}
