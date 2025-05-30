package com.jsuarez.inventario_api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO que combina información del inventario con datos del producto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioConProductoDTO {
    
    // Datos del inventario
    private Long inventarioId;
    private Long productoId;
    private Integer cantidadEnStock;  // Cambiado de "cantidad"
    
    // Datos del producto (obtenidos del microservicio)
    private String nombreProducto;
    private BigDecimal precioProducto;
    
    // Información calculada
    private BigDecimal valorTotalInventario;
    private String estadoStock;
    
    /**
     * Constructor que calcula valores derivados
     */
    public InventarioConProductoDTO(Long inventarioId, Long productoId, Integer cantidadEnStock, 
                                   String nombreProducto, BigDecimal precioProducto) {
        this.inventarioId = inventarioId;
        this.productoId = productoId;
        this.cantidadEnStock = cantidadEnStock;
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
        
        // Calcular valor total del inventario
        if (precioProducto != null && cantidadEnStock != null) {
            this.valorTotalInventario = precioProducto.multiply(BigDecimal.valueOf(cantidadEnStock));
        }
        
        // Determinar estado del stock
        this.estadoStock = determinarEstadoStock(cantidadEnStock);
    }
    
    /**
     * Determina el estado del stock basado en la cantidad
     */
    private String determinarEstadoStock(Integer cantidadEnStock) {
        if (cantidadEnStock == null || cantidadEnStock <= 0) {
            return "SIN_STOCK";
        } else if (cantidadEnStock <= 50) {
            return "STOCK_BAJO";
        } else if (cantidadEnStock <= 100) {
            return "STOCK_MEDIO";
        } else {
            return "STOCK_ALTO";
        }
    }
}