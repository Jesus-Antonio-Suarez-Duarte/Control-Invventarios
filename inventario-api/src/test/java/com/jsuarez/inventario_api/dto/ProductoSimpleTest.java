package com.jsuarez.inventario_api.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para ProductoSimple")
class ProductoSimpleTest {
    
    @Test
    @DisplayName("✅ Constructor sin parámetros")
    void constructorSinParametros() {
        // When
        ProductoSimple producto = new ProductoSimple();
        
        // Then
        assertNull(producto.getId());
        assertNull(producto.getNombre());
        assertNull(producto.getPrecio());
    }
    
    @Test
    @DisplayName("✅ Constructor con todos los parámetros")
    void constructorConTodosLosParametros() {
        // Given
        Long id = 10L;
        String nombre = "Producto Test";
        BigDecimal precio = new BigDecimal("125.50");
        
        // When
        ProductoSimple producto = new ProductoSimple(id, nombre, precio);
        
        // Then
        assertEquals(id, producto.getId());
        assertEquals(nombre, producto.getNombre());
        assertEquals(precio, producto.getPrecio());
    }
    
    @Test
    @DisplayName("✅ Constructor desde ProductoData")
    void constructorDesdeProductoData() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Cloro granulado", new BigDecimal("150.75"));
        ProductoData data = new ProductoData("25", "productos", attributes);
        
        // When
        ProductoSimple producto = new ProductoSimple(data);
        
        // Then
        assertEquals(25L, producto.getId(), "ID debe ser convertido de String a Long");
        assertEquals("Cloro granulado", producto.getNombre());
        assertEquals(new BigDecimal("150.75"), producto.getPrecio());
    }
    
    @Test
    @DisplayName("✅ Constructor desde ProductoData con ID numérico string")
    void constructorDesdeProductoDataConIdNumerico() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Producto Numérico", new BigDecimal("99.99"));
        ProductoData data = new ProductoData("999", "productos", attributes);
        
        // When
        ProductoSimple producto = new ProductoSimple(data);
        
        // Then
        assertEquals(999L, producto.getId());
        assertEquals("Producto Numérico", producto.getNombre());
        assertEquals(new BigDecimal("99.99"), producto.getPrecio());
    }
    
    @Test
    @DisplayName("❌ Constructor desde ProductoData con ID inválido")
    void constructorDesdeProductoDataConIdInvalido() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Producto Inválido", new BigDecimal("50.00"));
        ProductoData data = new ProductoData("abc", "productos", attributes);
        
        // When & Then
        assertThrows(NumberFormatException.class, () -> {
            new ProductoSimple(data);
        }, "Debe lanzar NumberFormatException cuando el ID no es numérico");
    }
    
    @Test
    @DisplayName("❌ Constructor desde ProductoData null")
    void constructorDesdeProductoDataNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new ProductoSimple(null);
        }, "Debe lanzar NullPointerException cuando ProductoData es null");
    }
    
    @Test
    @DisplayName("❌ Constructor desde ProductoData con attributes null")
    void constructorDesdeProductoDataConAttributesNull() {
        // Given
        ProductoData data = new ProductoData("10", "productos", null);
        
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new ProductoSimple(data);
        }, "Debe lanzar NullPointerException cuando attributes es null");
    }
    
    @Test
    @DisplayName("✅ Setters funcionan correctamente")
    void settersFuncionanCorrectamente() {
        // Given
        ProductoSimple producto = new ProductoSimple();
        
        // When
        producto.setId(15L);
        producto.setNombre("Producto Setter");
        producto.setPrecio(new BigDecimal("200.00"));
        
        // Then
        assertEquals(15L, producto.getId());
        assertEquals("Producto Setter", producto.getNombre());
        assertEquals(new BigDecimal("200.00"), producto.getPrecio());
    }
    
    @Test
    @DisplayName("✅ Test equals y hashCode")
    void testEqualsYHashCode() {
        // Given
        ProductoSimple producto1 = new ProductoSimple(10L, "Producto Test", new BigDecimal("100.00"));
        ProductoSimple producto2 = new ProductoSimple(10L, "Producto Test", new BigDecimal("100.00"));
        ProductoSimple producto3 = new ProductoSimple(20L, "Otro Producto", new BigDecimal("200.00"));
        
        // Then
        assertEquals(producto1, producto2, "Productos con mismos datos deben ser iguales");
        assertNotEquals(producto1, producto3, "Productos con datos diferentes no deben ser iguales");
        assertEquals(producto1.hashCode(), producto2.hashCode(), "HashCode debe ser igual para objetos iguales");
    }
    
    @Test
    @DisplayName("✅ Test toString")
    void testToString() {
        // Given
        ProductoSimple producto = new ProductoSimple(5L, "Producto ToString", new BigDecimal("75.25"));
        
        // When
        String toString = producto.toString();
        
        // Then
        assertNotNull(toString, "ToString no debe ser null");
        assertTrue(toString.contains("5"), "ToString debe contener el ID");
        assertTrue(toString.contains("Producto ToString"), "ToString debe contener el nombre");
        assertTrue(toString.contains("75.25"), "ToString debe contener el precio");
    }
    
    @Test
    @DisplayName("✅ Manejo de valores null")
    void manejoValoresNull() {
        // Given
        ProductoSimple producto = new ProductoSimple(null, null, null);
        
        // Then
        assertNull(producto.getId());
        assertNull(producto.getNombre());
        assertNull(producto.getPrecio());
        
        // Verificar que no lance excepciones
        assertDoesNotThrow(() -> {
            producto.setId(null);
            producto.setNombre(null);
            producto.setPrecio(null);
        });
    }
    
    @Test
    @DisplayName("✅ Test con precios extremos")
    void testConPreciosExtremos() {
        // Given
        BigDecimal precioMuyPequeno = new BigDecimal("0.01");
        BigDecimal precioMuyGrande = new BigDecimal("999999999.99");
        BigDecimal precioCero = BigDecimal.ZERO;
        
        // When
        ProductoSimple producto1 = new ProductoSimple(1L, "Producto Barato", precioMuyPequeno);
        ProductoSimple producto2 = new ProductoSimple(2L, "Producto Caro", precioMuyGrande);
        ProductoSimple producto3 = new ProductoSimple(3L, "Producto Gratis", precioCero);
        
        // Then
        assertEquals(precioMuyPequeno, producto1.getPrecio());
        assertEquals(precioMuyGrande, producto2.getPrecio());
        assertEquals(precioCero, producto3.getPrecio());
    }
    
    @Test
    @DisplayName("✅ Test con nombres especiales")
    void testConNombresEspeciales() {
        // Given
        String nombreVacio = "";
        String nombreConEspacios = "   Producto con espacios   ";
        String nombreConCaracteresEspeciales = "Producto@#$%&";
        String nombreMuyLargo = "A".repeat(1000);
        
        // When
        ProductoSimple producto1 = new ProductoSimple(1L, nombreVacio, new BigDecimal("10.00"));
        ProductoSimple producto2 = new ProductoSimple(2L, nombreConEspacios, new BigDecimal("20.00"));
        ProductoSimple producto3 = new ProductoSimple(3L, nombreConCaracteresEspeciales, new BigDecimal("30.00"));
        ProductoSimple producto4 = new ProductoSimple(4L, nombreMuyLargo, new BigDecimal("40.00"));
        
        // Then
        assertEquals(nombreVacio, producto1.getNombre());
        assertEquals(nombreConEspacios, producto2.getNombre());
        assertEquals(nombreConCaracteresEspeciales, producto3.getNombre());
        assertEquals(nombreMuyLargo, producto4.getNombre());
    }
}