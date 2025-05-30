package com.jsuarez.inventario_api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de compra procesada
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraResponse {
    private Long inventarioId;
    private Long productoId;
    private String nombreProducto;
    private Integer cantidadComprada;
    private Integer cantidadRestante;  // Cambiado de "cantidad" 
    private String estadoCompra;
    private String mensaje;
    
    public CompraResponse(Long inventarioId, Long productoId, String nombreProducto, 
                         Integer cantidadComprada, Integer cantidadRestante) {
        this.inventarioId = inventarioId;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidadComprada = cantidadComprada;
        this.cantidadRestante = cantidadRestante;
        this.estadoCompra = "EXITOSA";
        this.mensaje = "🛒 Compra procesada correctamente";
    }
}