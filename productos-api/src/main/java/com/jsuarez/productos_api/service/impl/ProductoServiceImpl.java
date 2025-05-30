package com.jsuarez.productos_api.service.impl;

import com.jsuarez.productos_api.dto.*;
import com.jsuarez.productos_api.entity.*;
import com.jsuarez.productos_api.exception.*;
import com.jsuarez.productos_api.mapper.*;
import com.jsuarez.productos_api.repository.*;
import com.jsuarez.productos_api.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
        log.info("Iniciando búsqueda de productos - Página: {}, Tamaño: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        try {
            Page<Producto> productos = productoRepository.findAll(pageable);
            Page<ProductoResponseDto> result = productos.map(productoMapper::toResponseDto);
            
            log.info("Productos encontrados: {} de {} total", 
                    result.getNumberOfElements(), result.getTotalElements());
            
            return result;
        } catch (Exception e) {
            log.error("❌Error al obtener productos paginados: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public ProductoResponseDto getProductoById(Long id) {
        log.info("Buscando producto con ID: {}", id);
        
        try {
            Producto producto = productoRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("❌ Producto no encontrado con ID: {}", id);
                        return new ProductoNotFoundException("No se encontró el producto con ID: " + id);
                    });
            
            ProductoResponseDto result = productoMapper.toResponseDto(producto);
            log.info("Producto encontrado exitosamente: {} - {}", id, producto.getNombre());
            
            return result;
        } catch (ProductoNotFoundException e) {
            throw e; // Re-lanzar excepciones de negocio
        } catch (Exception e) {
            log.error("❌ Error inesperado al buscar producto ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public ProductoResponseDto createProducto(ProductoRequestDto requestDto) {
        log.info("Iniciando creación de producto: {}", requestDto.getNombre());
        
        try {
            Producto producto = productoMapper.toEntity(requestDto);
            Producto savedProducto = productoRepository.save(producto);
            ProductoResponseDto result = productoMapper.toResponseDto(savedProducto);
            
            log.info("Producto creado exitosamente - ID: {}, Nombre: {}, Precio: {}", 
                    savedProducto.getId(), savedProducto.getNombre(), savedProducto.getPrecio());
            
            return result;
        } catch (Exception e) {
            log.error("❌Error al crear producto '{}': {}", requestDto.getNombre(), e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public ProductoResponseDto updateProducto(Long id, ProductoRequestDto requestDto) {
        log.info("Iniciando actualización de producto ID: {} con datos: {}", id, requestDto.getNombre());
        
        try {
            Producto existingProducto = productoRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("❌Intento de actualizar producto inexistente ID: {}", id);
                        return new ProductoNotFoundException("❌No se puede actualizar. El producto con ID " + id + " no existe");
                    });
            
            String oldName = existingProducto.getNombre();
            productoMapper.updateEntity(existingProducto, requestDto);
            Producto updatedProducto = productoRepository.save(existingProducto);
            ProductoResponseDto result = productoMapper.toResponseDto(updatedProducto);
            
            log.info("Producto actualizado exitosamente - ID: {}, Nombre: '{}' -> '{}', Precio: {}", 
                    id, oldName, updatedProducto.getNombre(), updatedProducto.getPrecio());
            
            return result;
        } catch (ProductoNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("❌Error al actualizar producto ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public void deleteProducto(Long id) {
        log.info("Iniciando eliminación de producto ID: {}", id);
        
        try {
            Producto existingProducto = productoRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("❌Intento de eliminar producto inexistente ID: {}", id);
                        return new ProductoNotFoundException("No se puede eliminar. El producto con ID " + id + " no existe");
                    });
            
            String productName = existingProducto.getNombre();
            productoRepository.delete(existingProducto);
            
            log.info("Producto eliminado exitosamente - ID: {}, Nombre: '{}'", id, productName);
            
        } catch (ProductoNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("❌Error al eliminar producto ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}