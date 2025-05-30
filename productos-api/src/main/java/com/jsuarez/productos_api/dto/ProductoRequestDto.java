package com.jsuarez.productos_api.dto;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoRequestDto {
    private String nombre;
    private BigDecimal precio;
}