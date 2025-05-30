package com.jsuarez.inventario_api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoSimple {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    
    // Constructor para convertir desde ProductoData
    public ProductoSimple(ProductoData data) {
        this.id = Long.parseLong(data.getId());
        this.nombre = data.getAttributes().getNombre();
        this.precio = data.getAttributes().getPrecio();
    }
}