package com.jsuarez.productos_api.mapper;


import com.jsuarez.productos_api.dto.*;
import com.jsuarez.productos_api.entity.*;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    
    public ProductoResponseDto toResponseDto(Producto producto) {
        if (producto == null) {
            return null;
        }
        
        ProductoResponseDto dto = new ProductoResponseDto();
        dto.setId(producto.getId().toString());
        dto.setType("productos");
        
        ProductoResponseDto.ProductoAttributes attributes = new ProductoResponseDto.ProductoAttributes();
        attributes.setNombre(producto.getNombre());
        attributes.setPrecio(producto.getPrecio());
        dto.setAttributes(attributes);
        
        return dto;
    }
    
    public Producto toEntity(ProductoRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        
        Producto producto = new Producto();
        producto.setNombre(requestDto.getNombre());
        producto.setPrecio(requestDto.getPrecio());
        return producto;
    }
    
    public void updateEntity(Producto producto, ProductoRequestDto requestDto) {
        if (producto == null || requestDto == null) {
            return; // No hacer nada si alguno es null
        }
        
        producto.setNombre(requestDto.getNombre());
        producto.setPrecio(requestDto.getPrecio());
    }
}