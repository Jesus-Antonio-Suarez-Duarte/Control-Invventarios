package com.jsuarez.productos_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoResponseDto {
    private String id;
    private String type;
    private ProductoAttributes attributes;
    
    @Data
    public static class ProductoAttributes {
        private String nombre;
        private BigDecimal precio;
    }
}