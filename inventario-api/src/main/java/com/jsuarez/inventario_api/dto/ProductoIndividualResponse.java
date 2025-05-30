package com.jsuarez.inventario_api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de un producto individual (GET /productos/{id})
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoIndividualResponse {
    private ProductoData data;  // UN SOLO producto, no una lista
    private String message;
    private Meta meta;          // Incluimos meta aunque sea null para que coincida exactamente
}