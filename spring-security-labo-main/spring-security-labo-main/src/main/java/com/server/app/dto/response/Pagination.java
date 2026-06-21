package com.server.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Pagination<T> {

    private List<T> data;
    private PaginationMeta pagination;

    public static <T> Pagination<T> from(Page<T> page) {
        PaginationMeta meta = new PaginationMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );

        return new Pagination<>(
                page.getContent(),
                meta
        );
    }
}