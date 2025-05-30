package com.jsuarez.inventario_api.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para ProductoApiResponse")
class ProductoApiResponseTest {
    
    @Test
    @DisplayName("✅ Constructor sin parámetros")
    void constructorSinParametros() {
        // When
        ProductoApiResponse response = new ProductoApiResponse();
        
        // Then
        assertNull(response.getData());
        assertNull(response.getMessage());
        assertNull(response.getMeta());
    }
    
    @Test
    @DisplayName("✅ Constructor con todos los parámetros")
    void constructorConTodosLosParametros() {
        // Given
        ProductoAttributes attributes1 = new ProductoAttributes("Producto 1", new BigDecimal("10.00"));
        ProductoAttributes attributes2 = new ProductoAttributes("Producto 2", new BigDecimal("20.00"));
        
        ProductoData data1 = new ProductoData("1", "productos", attributes1);
        ProductoData data2 = new ProductoData("2", "productos", attributes2);
        List<ProductoData> dataList = Arrays.asList(data1, data2);
        
        String message = "Productos obtenidos exitosamente";
        Meta meta = new Meta(1, 10, 25, 3, true, false);
        
        // When
        ProductoApiResponse response = new ProductoApiResponse(dataList, message, meta);
        
        // Then
        assertEquals(dataList, response.getData());
        assertEquals(message, response.getMessage());
        assertEquals(meta, response.getMeta());
        assertEquals(2, response.getData().size());
    }
    
    @Test
    @DisplayName("✅ Constructor con lista vacía")
    void constructorConListaVacia() {
        // Given
        List<ProductoData> emptyList = Collections.emptyList();
        String message = "No hay productos";
        Meta meta = new Meta(1, 10, 0, 0, true, true);
        
        // When
        ProductoApiResponse response = new ProductoApiResponse(emptyList, message, meta);
        
        // Then
        assertTrue(response.getData().isEmpty());
        assertEquals(message, response.getMessage());
        assertEquals(meta, response.getMeta());
    }
    
    @Test
    @DisplayName("✅ Setters funcionan correctamente")
    void settersFuncionanCorrectamente() {
        // Given
        ProductoApiResponse response = new ProductoApiResponse();
        
        ProductoAttributes attributes = new ProductoAttributes("Producto Setter", new BigDecimal("150.00"));
        ProductoData data = new ProductoData("10", "productos", attributes);
        List<ProductoData> newData = Arrays.asList(data);
        String newMessage = "Mensaje actualizado";
        Meta newMeta = new Meta(2, 5, 50, 10, false, false);
        
        // When
        response.setData(newData);
        response.setMessage(newMessage);
        response.setMeta(newMeta);
        
        // Then
        assertEquals(newData, response.getData());
        assertEquals(newMessage, response.getMessage());
        assertEquals(newMeta, response.getMeta());
    }
    
    @Test
    @DisplayName("✅ Test equals y hashCode")
    void testEqualsYHashCode() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Producto Test", new BigDecimal("100.00"));
        ProductoData data = new ProductoData("5", "productos", attributes);
        List<ProductoData> dataList = Arrays.asList(data);
        String message = "Test message";
        Meta meta = new Meta(1, 10, 1, 1, true, true);
        
        ProductoApiResponse response1 = new ProductoApiResponse(dataList, message, meta);
        ProductoApiResponse response2 = new ProductoApiResponse(dataList, message, meta);
        ProductoApiResponse response3 = new ProductoApiResponse(Collections.emptyList(), "Otro mensaje", null);
        
        // Then
        assertEquals(response1, response2, "Responses con mismos datos deben ser iguales");
        assertNotEquals(response1, response3, "Responses con datos diferentes no deben ser iguales");
        assertEquals(response1.hashCode(), response2.hashCode(), "HashCode debe ser igual para objetos iguales");
    }
    
    @Test
    @DisplayName("✅ Test toString")
    void testToString() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Producto ToString", new BigDecimal("75.00"));
        ProductoData data = new ProductoData("8", "productos", attributes);
        List<ProductoData> dataList = Arrays.asList(data);
        String message = "Test toString";
        
        ProductoApiResponse response = new ProductoApiResponse(dataList, message, null);
        
        // When
        String toString = response.toString();
        
        // Then
        assertNotNull(toString, "ToString no debe ser null");
        assertTrue(toString.contains("Test toString"), "ToString debe contener el mensaje");
        assertTrue(toString.contains("8"), "ToString debe contener datos del producto");
    }
    
    @Test
    @DisplayName("✅ Manejo de valores null")
    void manejoValoresNull() {
        // When
        ProductoApiResponse response1 = new ProductoApiResponse(null, null, null);
        ProductoApiResponse response2 = new ProductoApiResponse(null, "Mensaje con data null", null);
        
        // Then
        assertNull(response1.getData());
        assertNull(response1.getMessage());
        assertNull(response1.getMeta());
        
        assertNull(response2.getData());
        assertEquals("Mensaje con data null", response2.getMessage());
        assertNull(response2.getMeta());
    }
    
    @Test
    @DisplayName("✅ Test con múltiples productos")
    void testConMultiplesProductos() {
        // Given
        ProductoAttributes attr1 = new ProductoAttributes("Cloro", new BigDecimal("25.50"));
        ProductoAttributes attr2 = new ProductoAttributes("Floculante", new BigDecimal("195.50"));
        ProductoAttributes attr3 = new ProductoAttributes("Carbón activado", new BigDecimal("156.75"));
        
        ProductoData data1 = new ProductoData("18", "productos", attr1);
        ProductoData data2 = new ProductoData("9", "productos", attr2);
        ProductoData data3 = new ProductoData("10", "productos", attr3);
        
        List<ProductoData> productos = Arrays.asList(data1, data2, data3);
        Meta meta = new Meta(1, 10, 13, 2, true, false);
        
        // When
        ProductoApiResponse response = new ProductoApiResponse(productos, "Productos químicos", meta);
        
        // Then
        assertEquals(3, response.getData().size());
        assertEquals("Productos químicos", response.getMessage());
        assertEquals(13L, response.getMeta().getTotalElements());
        assertEquals("Cloro", response.getData().get(0).getAttributes().getNombre());
        assertEquals("Floculante", response.getData().get(1).getAttributes().getNombre());
        assertEquals("Carbón activado", response.getData().get(2).getAttributes().getNombre());
    }
    
    @Test
    @DisplayName("✅ Test con metadatos de paginación")
    void testConMetadatosPaginacion() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Producto Paginado", new BigDecimal("50.00"));
        ProductoData data = new ProductoData("1", "productos", attributes);
        List<ProductoData> dataList = Arrays.asList(data);
        
        // Meta para página 2 de 5
        Meta meta = new Meta(2, 5, 25, 5, false, false);
        
        // When
        ProductoApiResponse response = new ProductoApiResponse(dataList, "Página 2", meta);
        
        // Then
        assertEquals(2, response.getMeta().getPage());
        assertEquals(5, response.getMeta().getSize());
        assertEquals(25, response.getMeta().getTotalElements());
        assertEquals(5, response.getMeta().getTotalPages());
        assertFalse(response.getMeta().isFirst());
        assertFalse(response.getMeta().isLast());
    }
    
    @Test
    @DisplayName("✅ Test estructura de datos consistente")
    void testEstructuraDatosConsistente() {
        // Given
        ProductoAttributes attributes = new ProductoAttributes("Test Producto", new BigDecimal("99.99"));
        ProductoData data = new ProductoData("123", "productos", attributes);
        List<ProductoData> dataList = Arrays.asList(data);
        
        ProductoApiResponse response = new ProductoApiResponse(dataList, "Test", null);
        
        // Then - Verificar estructura JSON API
        assertNotNull(response.getData());
        assertNotNull(response.getMessage());
        // Meta puede ser null
        
        // Verificar estructura de ProductoData
        ProductoData firstProduct = response.getData().get(0);
        assertEquals("123", firstProduct.getId());
        assertEquals("productos", firstProduct.getType());
        assertNotNull(firstProduct.getAttributes());
        assertEquals("Test Producto", firstProduct.getAttributes().getNombre());
        assertEquals(new BigDecimal("99.99"), firstProduct.getAttributes().getPrecio());
    }
}