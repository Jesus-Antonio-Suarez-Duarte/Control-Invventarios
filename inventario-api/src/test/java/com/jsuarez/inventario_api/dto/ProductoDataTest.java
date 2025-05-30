package com.jsuarez.inventario_api.dto;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para ProductoData")
class ProductoDataTest {
    
    @Test
    @DisplayName("✅ Constructor sin parámetros")
    void constructorSinParametros() {
        // When
        ProductoData data = new ProductoData();
        
        // Then
        assertNull(data.getId());
        assertNull(data.getType());
        assertNull(data.getAttributes());
    }
    
    @Test
    @DisplayName("✅ Constructor con todos los parámetros")
    void constructorConTodosLosParametros() {
        // Given
        String id = "15";
        String type = "productos";
        ProductoAttributes attributes = new ProductoAttributes("Cloro granulado", new BigDecimal("1000.00"));
        
        // When
        ProductoData data = new ProductoData(id, type, attributes);
        
        // Then
        assertEquals(id, data.getId());
        assertEquals(type, data.getType());
        assertEquals(attributes, data.getAttributes());
    }
    
    @Test
    @DisplayName("✅ Setters funcionan correctamente")
    void settersFuncionanCorrectamente() {
        // Given
        ProductoData data = new ProductoData();
        ProductoAttributes newAttributes = new ProductoAttributes("Producto Nuevo", new BigDecimal("250.00"));
        
        // When
        data.setId("25");
        data.setType("productos");
        data.setAttributes(newAttributes);
        
        // Then
        assertEquals("25", data.getId());
        assertEquals("productos", data.getType());
        assertEquals(newAttributes, data.getAttributes());
    }
    
    @Test
    @DisplayName("✅ Test equals y hashCode")
    void testEqualsYHashCode() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Producto Test", new BigDecimal("100.00"));
        
        ProductoData data1 = new ProductoData("10", "productos", attributes);
        ProductoData data2 = new ProductoData("10", "productos", attributes);
        ProductoData data3 = new ProductoData("20", "otros", attributes);
        
        // Then
        assertEquals(data1, data2, "Datos con mismos valores deben ser iguales");
        assertNotEquals(data1, data3, "Datos con valores diferentes no deben ser iguales");
        assertEquals(data1.hashCode(), data2.hashCode(), "HashCode debe ser igual para objetos iguales");
    }
    
    @Test
    @DisplayName("✅ Test toString")
    void testToString() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Producto ToString", new BigDecimal("75.00"));
        ProductoData data = new ProductoData("5", "productos", attributes);
        
        // When
        String toString = data.toString();
        
        // Then
        assertNotNull(toString, "ToString no debe ser null");
        assertTrue(toString.contains("5"), "ToString debe contener el ID");
        assertTrue(toString.contains("productos"), "ToString debe contener el type");
    }
    
    @Test
    @DisplayName("✅ Manejo de valores null")
    void manejoValoresNull() {
        // When
        ProductoData data = new ProductoData(null, null, null);
        
        // Then
        assertNull(data.getId());
        assertNull(data.getType());
        assertNull(data.getAttributes());
        
        // Verificar que no lance excepciones
        assertDoesNotThrow(() -> {
            data.setId(null);
            data.setType(null);
            data.setAttributes(null);
        });
    }
    
    @Test
    @DisplayName("✅ Test con IDs de diferentes formatos")
    void testConIdsDiferentesFormatos() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Test", new BigDecimal("10.00"));
        
        // When
        ProductoData data1 = new ProductoData("1", "productos", attributes);
        ProductoData data2 = new ProductoData("999", "productos", attributes);
        ProductoData data3 = new ProductoData("0", "productos", attributes);
        
        // Then
        assertEquals("1", data1.getId());
        assertEquals("999", data2.getId());
        assertEquals("0", data3.getId());
    }
    
    @Test
    @DisplayName("✅ Test con diferentes tipos")
    void testConDiferentesTipos() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Test", new BigDecimal("10.00"));
        
        // When
        ProductoData data1 = new ProductoData("1", "productos", attributes);
        ProductoData data2 = new ProductoData("1", "servicios", attributes);
        ProductoData data3 = new ProductoData("1", "", attributes);
        
        // Then
        assertEquals("productos", data1.getType());
        assertEquals("servicios", data2.getType());
        assertEquals("", data3.getType());
    }
    
    @Test
    @DisplayName("✅ Test acceso a attributes anidados")
    void testAccesoAttributesAnidados() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Producto Anidado", new BigDecimal("125.75"));
        ProductoData data = new ProductoData("30", "productos", attributes);
        
        // Then
        assertNotNull(data.getAttributes());
        assertEquals("Producto Anidado", data.getAttributes().getNombre());
        assertEquals(new BigDecimal("125.75"), data.getAttributes().getPrecio());
    }
    
    @Test
    @DisplayName("✅ Test modificación de attributes")
    void testModificacionAttributes() {
        // Given
        ProductoAttributes originalAttributes = new ProductoAttributes("Original", new BigDecimal("50.00"));
        ProductoData data = new ProductoData("40", "productos", originalAttributes);
        
        ProductoAttributes newAttributes = new ProductoAttributes("Modificado", new BigDecimal("75.00"));
        
        // When
        data.setAttributes(newAttributes);
        
        // Then
        assertEquals("Modificado", data.getAttributes().getNombre());
        assertEquals(new BigDecimal("75.00"), data.getAttributes().getPrecio());
        assertNotEquals(originalAttributes, data.getAttributes());
    }
    
    @Test
    @DisplayName("✅ Test JSON API structure compliance")
    void testJsonApiStructureCompliance() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("JSON API Test", new BigDecimal("200.00"));
        ProductoData data = new ProductoData("100", "productos", attributes);
        
        // Then - Verificar que sigue estructura JSON API
        assertNotNull(data.getId(), "id es requerido en JSON API");
        assertNotNull(data.getType(), "type es requerido en JSON API");
        assertNotNull(data.getAttributes(), "attributes contiene los datos del objeto");
        
        // Verificar tipos correctos
        assertTrue(data.getId() instanceof String, "id debe ser String en JSON API");
        assertTrue(data.getType() instanceof String, "type debe ser String en JSON API");
        assertTrue(data.getAttributes() instanceof ProductoAttributes, "attributes debe ser objeto");
    }
}