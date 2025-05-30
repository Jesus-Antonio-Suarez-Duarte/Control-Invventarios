package com.jsuarez.productos_api.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

 public class ProductoRequestDtoTest {

    @Test
    void testProductoRequestDtoCreation() {
        // Given
        ProductoRequestDto requestDto = new ProductoRequestDto();
        
        // When
        requestDto.setNombre("Cal viva industrial");
        requestDto.setPrecio(new BigDecimal("28.90"));
        
        // Then
        assertEquals("Cal viva industrial", requestDto.getNombre());
        assertEquals(new BigDecimal("28.90"), requestDto.getPrecio());
    }

    @Test
    void testProductoRequestDtoGettersAndSetters() {
        // Given
        ProductoRequestDto requestDto = new ProductoRequestDto();
        String nombre = "Hipoclorito de sodio";
        BigDecimal precio = new BigDecimal("185.40");
        
        // When
        requestDto.setNombre(nombre);
        requestDto.setPrecio(precio);
        
        // Then
        assertEquals(nombre, requestDto.getNombre());
        assertEquals(precio, requestDto.getPrecio());
    }
}