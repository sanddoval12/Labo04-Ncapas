package com.server.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginationMeta {

    private int page;
    private int pageSize;
    private int pageCount;
    private long total;
}