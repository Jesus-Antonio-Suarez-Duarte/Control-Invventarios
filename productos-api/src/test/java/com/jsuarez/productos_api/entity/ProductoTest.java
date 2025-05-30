package com.jsuarez.productos_api.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    @Test
    void testProductoCreation() {
        // Given
        Producto producto = new Producto();
        
        // When
        producto.setId(1L);
        producto.setNombre("Cloro granulado 25kg");
        producto.setPrecio(new BigDecimal("45.50"));
        
        // Then
        assertEquals(1L, producto.getId());
        assertEquals("Cloro granulado 25kg", producto.getNombre());
        assertEquals(new BigDecimal("45.50"), producto.getPrecio());
    }

    @Test
    void testProductoGettersAndSetters() {
        // Given
        Producto producto = new Producto();
        String nombre = "Sulfato de aluminio";
        BigDecimal precio = new BigDecimal("320.75");
        Long id = 2L;
        
        // When
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        
        // Then
        assertEquals(id, producto.getId());
        assertEquals(nombre, producto.getNombre());
        assertEquals(precio, producto.getPrecio());
    }

    @Test
    void testProductoToString() {
        // Given
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Test Producto");
        producto.setPrecio(new BigDecimal("100.00"));
        
        // When
        String toStringResult = producto.toString();
        
        // Then
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Test Producto"));
    }

    @Test
    void testProductoEquals() {
        // Given
        Producto producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Producto Test");
        producto1.setPrecio(new BigDecimal("50.00"));

        Producto producto2 = new Producto();
        producto2.setId(1L);
        producto2.setNombre("Producto Test");
        producto2.setPrecio(new BigDecimal("50.00"));

        // When & Then
        assertEquals(producto1, producto2);
        assertEquals(producto1.hashCode(), producto2.hashCode());
    }

    @Test
    void testProductoNotEquals() {
        // Given
        Producto producto1 = new Producto();
        producto1.setId(1L);
        
        Producto producto2 = new Producto();
        producto2.setId(2L);
        
        // When & Then
        assertNotEquals(producto1, producto2);
    }
}