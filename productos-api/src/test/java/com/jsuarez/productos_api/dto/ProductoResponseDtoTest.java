package com.jsuarez.productos_api.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class  ProductoResponseDtoTest {

    @Test
    void testProductoResponseDtoCreation() {
        // Given
        ProductoResponseDto dto = new ProductoResponseDto();
        ProductoResponseDto.ProductoAttributes attributes = new ProductoResponseDto.ProductoAttributes();
        
        // When
        dto.setId("1");
        dto.setType("productos");
        attributes.setNombre("Cloro granulado");
        attributes.setPrecio(new BigDecimal("45.50"));
        dto.setAttributes(attributes);
        
        // Then
        assertEquals("1", dto.getId());
        assertEquals("productos", dto.getType());
        assertEquals("Cloro granulado", dto.getAttributes().getNombre());
        assertEquals(new BigDecimal("45.50"), dto.getAttributes().getPrecio());
    }

    @Test
    void testProductoAttributesGettersAndSetters() {
        // Given
        ProductoResponseDto.ProductoAttributes attributes = new ProductoResponseDto.ProductoAttributes();
        String nombre = "Sulfato de aluminio";
        BigDecimal precio = new BigDecimal("320.75");
        
        // When
        attributes.setNombre(nombre);
        attributes.setPrecio(precio);
        
        // Then
        assertEquals(nombre, attributes.getNombre());
        assertEquals(precio, attributes.getPrecio());
    }
}