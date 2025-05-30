package com.jsuarez.inventario_api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoData {
    private String id;
    private String type;
    private ProductoAttributes attributes;
}