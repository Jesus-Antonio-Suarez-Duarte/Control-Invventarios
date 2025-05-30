package com.jsuarez.productos_api.service.impl;

import com.jsuarez.productos_api.dto.*;
import com.jsuarez.productos_api.entity.*;
import com.jsuarez.productos_api.exception.*;
import com.jsuarez.productos_api.mapper.*;
import com.jsuarez.productos_api.repository.*;
import com.jsuarez.productos_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {
    
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    
    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }
    
    @Override
    public Page<ProductoResponseDto> getAllProductos(Pageable pageable) {
        Page<Producto> productos = productoRepository.findAll(pageable);
        return productos.map(productoMapper::toResponseDto);
    }
    
    @Override
    public ProductoResponseDto getProductoById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("No se encontró el producto con ID: " + id));
        return productoMapper.toResponseDto(producto);
    }
    
    @Override
    public ProductoResponseDto createProducto(ProductoRequestDto requestDto) {
        Producto producto = productoMapper.toEntity(requestDto);
        Producto savedProducto = productoRepository.save(producto);
        return productoMapper.toResponseDto(savedProducto);
    }
    
    @Override
    public ProductoResponseDto updateProducto(Long id, ProductoRequestDto requestDto) {
        Producto existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("No se puede actualizar. El producto con ID " + id + " no existe"));
        
        productoMapper.updateEntity(existingProducto, requestDto);
        Producto updatedProducto = productoRepository.save(existingProducto);
        return productoMapper.toResponseDto(updatedProducto);
    }
    
    @Override
    public void deleteProducto(Long id) {
        Producto existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("No se puede eliminar. El producto con ID " + id + " no existe"));
        productoRepository.delete(existingProducto);
    }
}