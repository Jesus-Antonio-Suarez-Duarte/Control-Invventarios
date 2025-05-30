package com.jsuarez.inventario_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DTO para respuestas en formato JSON API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonApiResponse<T> {
    private List<T> data;
    private String message;
    private MetaPaginacion meta;
    
    // Constructor para respuesta simple sin paginación
    public JsonApiResponse(List<T> data, String message) {
        this.data = data;
        this.message = message;
        this.meta = null;
    }
    
    // Constructor para respuesta con paginación
    public JsonApiResponse(Page<T> page, String message) {
        this.data = page.getContent();
        this.message = message;
        this.meta = new MetaPaginacion(
            page.getNumber() + 1, // Spring usa 0-based, JSON API usa 1-based
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast()
        );
    }
    
    // Constructor para un solo elemento
    public JsonApiResponse(T singleItem, String message) {
        this.data = List.of(singleItem);
        this.message = message;
        this.meta = null;
    }
}