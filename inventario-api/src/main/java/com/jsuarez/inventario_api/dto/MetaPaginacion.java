package com.jsuarez.inventario_api.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para metadatos de paginación
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaPaginacion {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}