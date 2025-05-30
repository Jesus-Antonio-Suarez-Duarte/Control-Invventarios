package com.jsuarez.inventario_api.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para CompraResponse")
class CompraResponseTest {
    
    @Test
    @DisplayName("✅ Constructor con parámetros principales")
    void constructorConParametrosPrincipales() {
        // Given
        Long inventarioId = 1L;
        Long productoId = 10L;
        String nombreProducto = "Producto Test";
        Integer cantidadComprada = 25;
        Integer cantidadRestante = 75;
        
        // When
        CompraResponse response = new CompraResponse(
            inventarioId, productoId, nombreProducto, cantidadComprada, cantidadRestante
        );
        
        // Then
        assertEquals(inventarioId, response.getInventarioId());
        assertEquals(productoId, response.getProductoId());
        assertEquals(nombreProducto, response.getNombreProducto());
        assertEquals(cantidadComprada, response.getCantidadComprada());
        assertEquals(cantidadRestante, response.getCantidadRestante());
        assertEquals("EXITOSA", response.getEstadoCompra());
        assertEquals("🛒 Compra procesada correctamente", response.getMensaje());
    }
    
    @Test
    @DisplayName("✅ Constructor sin parámetros")
    void constructorSinParametros() {
        // When
        CompraResponse response = new CompraResponse();
        
        // Then
        assertNull(response.getInventarioId());
        assertNull(response.getProductoId());
        assertNull(response.getNombreProducto());
        assertNull(response.getCantidadComprada());
        assertNull(response.getCantidadRestante());
        assertNull(response.getEstadoCompra());
        assertNull(response.getMensaje());
    }
    
    @Test
    @DisplayName("✅ Constructor AllArgs completo")
    void constructorAllArgsCompleto() {
        // Given
        Long inventarioId = 2L;
        Long productoId = 20L;
        String nombreProducto = "Producto AllArgs";
        Integer cantidadComprada = 15;
        Integer cantidadRestante = 85;
        String estadoCompra = "EXITOSA";
        String mensaje = "Mensaje personalizado";
        
        // When
        CompraResponse response = new CompraResponse(
            inventarioId, productoId, nombreProducto, cantidadComprada, 
            cantidadRestante, estadoCompra, mensaje
        );
        
        // Then
        assertEquals(inventarioId, response.getInventarioId());
        assertEquals(productoId, response.getProductoId());
        assertEquals(nombreProducto, response.getNombreProducto());
        assertEquals(cantidadComprada, response.getCantidadComprada());
        assertEquals(cantidadRestante, response.getCantidadRestante());
        assertEquals(estadoCompra, response.getEstadoCompra());
        assertEquals(mensaje, response.getMensaje());
    }
    
    @Test
    @DisplayName("✅ Setters funcionan correctamente")
    void settersFuncionanCorrectamente() {
        // Given
        CompraResponse response = new CompraResponse();
        
        // When
        response.setInventarioId(3L);
        response.setProductoId(30L);
        response.setNombreProducto("Producto Setter");
        response.setCantidadComprada(10);
        response.setCantidadRestante(90);
        response.setEstadoCompra("PROCESADA");
        response.setMensaje("Mensaje setter");
        
        // Then
        assertEquals(3L, response.getInventarioId());
        assertEquals(30L, response.getProductoId());
        assertEquals("Producto Setter", response.getNombreProducto());
        assertEquals(10, response.getCantidadComprada());
        assertEquals(90, response.getCantidadRestante());
        assertEquals("PROCESADA", response.getEstadoCompra());
        assertEquals("Mensaje setter", response.getMensaje());
    }
    
    @Test
    @DisplayName("✅ Test equals y hashCode")
    void testEqualsYHashCode() {
        // Given
        CompraResponse response1 = new CompraResponse(
            1L, 10L, "Producto Test", 25, 75, "EXITOSA", "Mensaje test"
        );
        CompraResponse response2 = new CompraResponse(
            1L, 10L, "Producto Test", 25, 75, "EXITOSA", "Mensaje test"
        );
        CompraResponse response3 = new CompraResponse(
            2L, 20L, "Otro Producto", 50, 50, "EXITOSA", "Otro mensaje"
        );
        
        // Then
        assertEquals(response1, response2, "Responses con mismos datos deben ser iguales");
        assertNotEquals(response1, response3, "Responses con datos diferentes no deben ser iguales");
        assertEquals(response1.hashCode(), response2.hashCode(), "HashCode debe ser igual para objetos iguales");
    }
    
    @Test
    @DisplayName("✅ Test toString")
    void testToString() {
        // Given
        CompraResponse response = new CompraResponse(
            1L, 10L, "Producto Test", 25, 75
        );
        
        // When
        String toString = response.toString();
        
        // Then
        assertNotNull(toString, "ToString no debe ser null");
        assertTrue(toString.contains("1"), "ToString debe contener inventarioId");
        assertTrue(toString.contains("10"), "ToString debe contener productoId");
        assertTrue(toString.contains("Producto Test"), "ToString debe contener nombreProducto");
        assertTrue(toString.contains("25"), "ToString debe contener cantidadComprada");
        assertTrue(toString.contains("75"), "ToString debe contener cantidadRestante");
    }
    
    @Test
    @DisplayName("✅ Valores por defecto en constructor principal")
    void valoresPorDefectoEnConstructorPrincipal() {
        // When
        CompraResponse response = new CompraResponse(
            1L, 10L, "Producto Test", 25, 75
        );
        
        // Then
        assertEquals("EXITOSA", response.getEstadoCompra(), 
            "Estado por defecto debe ser EXITOSA");
        assertEquals("🛒 Compra procesada correctamente", response.getMensaje(),
            "Mensaje por defecto debe incluir emoji y texto correcto");
    }
    
    @Test
    @DisplayName("✅ Manejo de valores null")
    void manejoValoresNull() {
        // When
        CompraResponse response = new CompraResponse(
            null, null, null, null, null
        );
        
        // Then
        assertNull(response.getInventarioId());
        assertNull(response.getProductoId());
        assertNull(response.getNombreProducto());
        assertNull(response.getCantidadComprada());
        assertNull(response.getCantidadRestante());
        assertEquals("EXITOSA", response.getEstadoCompra());
        assertEquals("🛒 Compra procesada correctamente", response.getMensaje());
    }
    
    @Test
    @DisplayName("✅ Test con cantidades cero")
    void testConCantidadesCero() {
        // When
        CompraResponse response = new CompraResponse(
            1L, 10L, "Producto Test", 0, 0
        );
        
        // Then
        assertEquals(0, response.getCantidadComprada());
        assertEquals(0, response.getCantidadRestante());
        assertEquals("EXITOSA", response.getEstadoCompra());
        assertEquals("🛒 Compra procesada correctamente", response.getMensaje());
    }
}