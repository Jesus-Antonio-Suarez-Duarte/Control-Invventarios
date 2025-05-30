package com.jsuarez.productos_api.mapper;


import com.jsuarez.productos_api.dto.*;
import com.jsuarez.productos_api.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductoMapperTest {

    private ProductoMapper productoMapper;

    @BeforeEach
    void setUp() {
        productoMapper = new ProductoMapper();
    }

    @Test
    void testToResponseDto() {
        // Given
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Cloro granulado 25kg");
        producto.setPrecio(new BigDecimal("45.50"));

        // When
        ProductoResponseDto responseDto = productoMapper.toResponseDto(producto);

        // Then
        assertNotNull(responseDto);
        assertEquals("1", responseDto.getId());
        assertEquals("productos", responseDto.getType());
        assertNotNull(responseDto.getAttributes());
        assertEquals("Cloro granulado 25kg", responseDto.getAttributes().getNombre());
        assertEquals(new BigDecimal("45.50"), responseDto.getAttributes().getPrecio());
    }

    @Test
    void testToEntity() {
        // Given
        ProductoRequestDto requestDto = new ProductoRequestDto();
        requestDto.setNombre("Sulfato de aluminio");
        requestDto.setPrecio(new BigDecimal("320.75"));

        // When
        Producto producto = productoMapper.toEntity(requestDto);

        // Then
        assertNotNull(producto);
        assertEquals("Sulfato de aluminio", producto.getNombre());
        assertEquals(new BigDecimal("320.75"), producto.getPrecio());
        assertNull(producto.getId()); // ID no debe estar seteado en creación
    }

    @Test
    void testUpdateEntity() {
        // Given
        Producto existingProducto = new Producto();
        existingProducto.setId(1L);
        existingProducto.setNombre("Nombre original");
        existingProducto.setPrecio(new BigDecimal("100.00"));

        ProductoRequestDto requestDto = new ProductoRequestDto();
        requestDto.setNombre("Nombre actualizado");
        requestDto.setPrecio(new BigDecimal("150.00"));

        // When
        productoMapper.updateEntity(existingProducto, requestDto);

        // Then
        assertEquals(1L, existingProducto.getId()); // ID no debe cambiar
        assertEquals("Nombre actualizado", existingProducto.getNombre());
        assertEquals(new BigDecimal("150.00"), existingProducto.getPrecio());
    }

    @Test
    void testToResponseDtoCompleteMapping() {
        // Given
        Producto producto = new Producto();
        producto.setId(999L);
        producto.setNombre("Producto de prueba completo");
        producto.setPrecio(new BigDecimal("999.99"));

        // When
        ProductoResponseDto responseDto = productoMapper.toResponseDto(producto);

        // Then
        assertNotNull(responseDto);
        assertNotNull(responseDto.getAttributes());
        
        // Verificar que todos los campos se mapean correctamente
        assertEquals("999", responseDto.getId());
        assertEquals("productos", responseDto.getType());
        assertEquals("Producto de prueba completo", responseDto.getAttributes().getNombre());
        assertEquals(new BigDecimal("999.99"), responseDto.getAttributes().getPrecio());
    }

    @Test
    void testMapperInstanceNotNull() {
        // When & Then
        assertNotNull(productoMapper);
    }
    
    // Tests adicionales para validar el manejo de nulls (con ProductoMapper actualizado)
    @Test
    void testToResponseDtoWithNullProduct() {
        // Given
        Producto producto = null;

        // When
        ProductoResponseDto responseDto = productoMapper.toResponseDto(producto);

        // Then
        assertNull(responseDto);
    }

    @Test
    void testToEntityWithNullRequest() {
        // Given
        ProductoRequestDto requestDto = null;

        // When
        Producto producto = productoMapper.toEntity(requestDto);

        // Then
        assertNull(producto);
    }

    @Test
    void testUpdateEntityWithNullRequest() {
        // Given
        Producto existingProducto = new Producto();
        existingProducto.setId(1L);
        existingProducto.setNombre("Nombre original");
        existingProducto.setPrecio(new BigDecimal("100.00"));

        ProductoRequestDto requestDto = null;

        // When
        productoMapper.updateEntity(existingProducto, requestDto);

        // Then - No debe cambiar nada
        assertEquals(1L, existingProducto.getId());
        assertEquals("Nombre original", existingProducto.getNombre());
        assertEquals(new BigDecimal("100.00"), existingProducto.getPrecio());
    }

    @Test
    void testUpdateEntityWithNullProduct() {
        // Given
        Producto existingProducto = null;
        ProductoRequestDto requestDto = new ProductoRequestDto();
        requestDto.setNombre("Test");
        requestDto.setPrecio(new BigDecimal("50.00"));

        // When & Then - No debe lanzar excepción
        assertDoesNotThrow(() -> productoMapper.updateEntity(existingProducto, requestDto));
    }
}