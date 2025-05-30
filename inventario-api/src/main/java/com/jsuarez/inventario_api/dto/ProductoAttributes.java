package com.jsuarez.inventario_api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoAttributes {
    private String nombre;
    private BigDecimal precio;
}