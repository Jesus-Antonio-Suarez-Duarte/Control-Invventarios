package com.jsuarez.productos_api.dto;

import lombok.Data;

@Data
public class Meta {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    
    public static Meta of(int page, int size, long totalElements, int totalPages, boolean first, boolean last) {
        Meta meta = new Meta();
        meta.setPage(page);
        meta.setSize(size);
        meta.setTotalElements(totalElements);
        meta.setTotalPages(totalPages);
        meta.setFirst(first);
        meta.setLast(last);
        return meta;
    }
}