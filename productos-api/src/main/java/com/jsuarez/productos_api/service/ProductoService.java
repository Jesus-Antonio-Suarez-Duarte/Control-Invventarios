package com.jsuarez.productos_api.service;


import com.jsuarez.productos_api.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoService {
    Page<ProductoResponseDto> getAllProductos(Pageable pageable);
    ProductoResponseDto getProductoById(Long id);
    ProductoResponseDto createProducto(ProductoRequestDto requestDto);
    ProductoResponseDto updateProducto(Long id, ProductoRequestDto requestDto);
    void deleteProducto(Long id);
}