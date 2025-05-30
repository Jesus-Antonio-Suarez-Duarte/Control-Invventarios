package com.jsuarez.inventario_api.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoApiResponse {
    private List<ProductoData> data;
    private String message;
    private Meta meta;
}