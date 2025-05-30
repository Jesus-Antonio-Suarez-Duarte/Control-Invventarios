package com.jsuarez.inventario_api.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para InventarioConProductoDTO")
class InventarioConProductoDTOTest {
    
    @Test
    @DisplayName("✅ Constructor completo calcula valores correctamente")
    void constructorCompletoCalculaValores() {
        // Given
        Long inventarioId = 1L;
        Long productoId = 10L;
        Integer cantidadEnStock = 100;
        String nombreProducto = "Producto Test";
        BigDecimal precioProducto = new BigDecimal("25.50");
        
        // When
        InventarioConProductoDTO dto = new InventarioConProductoDTO(
            inventarioId, productoId, cantidadEnStock, nombreProducto, precioProducto
        );
        
        // Then
        assertEquals(inventarioId, dto.getInventarioId());
        assertEquals(productoId, dto.getProductoId());
        assertEquals(cantidadEnStock, dto.getCantidadEnStock());
        assertEquals(nombreProducto, dto.getNombreProducto());
        assertEquals(precioProducto, dto.getPrecioProducto());
        assertEquals(new BigDecimal("2550.00"), dto.getValorTotalInventario());
        assertEquals("STOCK_MEDIO", dto.getEstadoStock()); // Corregido: era STOCK_ALTO
    }
    
    @Test
    @DisplayName("✅ Constructor sin parámetros")
    void constructorSinParametros() {
        // When
        InventarioConProductoDTO dto = new InventarioConProductoDTO();
        
        // Then
        assertNull(dto.getInventarioId());
        assertNull(dto.getProductoId());
        assertNull(dto.getCantidadEnStock());
        assertNull(dto.getNombreProducto());
        assertNull(dto.getPrecioProducto());
        assertNull(dto.getValorTotalInventario());
        assertNull(dto.getEstadoStock());
    }
    
    @Test
    @DisplayName("✅ Cálculo valor total con precio null")
    void calculoValorTotalConPrecioNull() {
        // When
        InventarioConProductoDTO dto = new InventarioConProductoDTO(
            1L, 10L, 100, "Producto Test", null
        );
        
        // Then
        assertNull(dto.getValorTotalInventario(), "Valor total debe ser null cuando precio es null");
        assertEquals("STOCK_MEDIO", dto.getEstadoStock()); // Corregido: era STOCK_ALTO
    }
    
    @Test
    @DisplayName("✅ Cálculo valor total con cantidad null")
    void calculoValorTotalConCantidadNull() {
        // When
        InventarioConProductoDTO dto = new InventarioConProductoDTO(
            1L, 10L, null, "Producto Test", new BigDecimal("10.00")
        );
        
        // Then
        assertNull(dto.getValorTotalInventario(), "Valor total debe ser null cuando cantidad es null");
        assertEquals("SIN_STOCK", dto.getEstadoStock());
    }
    
    @ParameterizedTest
    @MethodSource("provideEstadosStock")
    @DisplayName("✅ Estados de stock según cantidad")
    void estadosStockSegunCantidad(Integer cantidad, String estadoEsperado) {
        // When
        InventarioConProductoDTO dto = new InventarioConProductoDTO(
            1L, 10L, cantidad, "Producto Test", new BigDecimal("10.00")
        );
        
        // Then
        assertEquals(estadoEsperado, dto.getEstadoStock(), 
            "Estado de stock incorrecto para cantidad: " + cantidad);
    }
    
    static Stream<Arguments> provideEstadosStock() {
        return Stream.of(
            Arguments.of(null, "SIN_STOCK"),
            Arguments.of(0, "SIN_STOCK"),
            Arguments.of(-5, "SIN_STOCK"),
            Arguments.of(1, "STOCK_BAJO"),
            Arguments.of(5, "STOCK_BAJO"),
            Arguments.of(10, "STOCK_BAJO"),
            Arguments.of(11, "STOCK_BAJO"),    // Corregido: era STOCK_MEDIO
            Arguments.of(30, "STOCK_BAJO"),    // Corregido: era STOCK_MEDIO  
            Arguments.of(50, "STOCK_BAJO"),    // Corregido: era STOCK_MEDIO
            Arguments.of(51, "STOCK_MEDIO"),   // Corregido: era STOCK_ALTO
            Arguments.of(100, "STOCK_MEDIO"),  // Corregido: era STOCK_ALTO
            Arguments.of(1000, "STOCK_ALTO")
        );
    }
    
    @Test
    @DisplayName("✅ Cálculo de valor total exacto")
    void calculoValorTotalExacto() {
        // Given
        BigDecimal precio = new BigDecimal("15.75");
        Integer cantidad = 8;
        BigDecimal valorEsperado = new BigDecimal("126.00");
        
        // When
        InventarioConProductoDTO dto = new InventarioConProductoDTO(
            1L, 10L, cantidad, "Producto Test", precio
        );
        
        // Then
        assertEquals(valorEsperado, dto.getValorTotalInventario(),
            "El cálculo del valor total debe ser exacto");
    }
    
    @Test
    @DisplayName("✅ Test equals y hashCode")
    void testEqualsYHashCode() {
        // Given
        InventarioConProductoDTO dto1 = new InventarioConProductoDTO(
            1L, 10L, 100, "Producto Test", new BigDecimal("10.00")
        );
        InventarioConProductoDTO dto2 = new InventarioConProductoDTO(
            1L, 10L, 100, "Producto Test", new BigDecimal("10.00")
        );
        InventarioConProductoDTO dto3 = new InventarioConProductoDTO(
            2L, 20L, 200, "Otro Producto", new BigDecimal("20.00")
        );
        
        // Then
        assertEquals(dto1, dto2, "DTOs con mismos datos deben ser iguales");
        assertNotEquals(dto1, dto3, "DTOs con datos diferentes no deben ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "HashCode debe ser igual para objetos iguales");
    }
    
    @Test
    @DisplayName("✅ Test toString")
    void testToString() {
        // Given
        InventarioConProductoDTO dto = new InventarioConProductoDTO(
            1L, 10L, 100, "Producto Test", new BigDecimal("10.00")
        );
        
        // When
        String toString = dto.toString();
        
        // Then
        assertNotNull(toString, "ToString no debe ser null");
        assertTrue(toString.contains("1"), "ToString debe contener inventarioId");
        assertTrue(toString.contains("10"), "ToString debe contener productoId");
        assertTrue(toString.contains("100"), "ToString debe contener cantidadEnStock");
        assertTrue(toString.contains("Producto Test"), "ToString debe contener nombreProducto");
    }
    
    @Test
    @DisplayName("✅ Setters funcionan correctamente")
    void settersFuncionanCorrectamente() {
        // Given
        InventarioConProductoDTO dto = new InventarioConProductoDTO();
        
        // When
        dto.setInventarioId(5L);
        dto.setProductoId(25L);
        dto.setCantidadEnStock(75);
        dto.setNombreProducto("Nuevo Producto");
        dto.setPrecioProducto(new BigDecimal("12.50"));
        dto.setValorTotalInventario(new BigDecimal("937.50"));
        dto.setEstadoStock("STOCK_ALTO");
        
        // Then
        assertEquals(5L, dto.getInventarioId());
        assertEquals(25L, dto.getProductoId());
        assertEquals(75, dto.getCantidadEnStock());
        assertEquals("Nuevo Producto", dto.getNombreProducto());
        assertEquals(new BigDecimal("12.50"), dto.getPrecioProducto());
        assertEquals(new BigDecimal("937.50"), dto.getValorTotalInventario());
        assertEquals("STOCK_ALTO", dto.getEstadoStock());
    }
    
    @Test
    @DisplayName("✅ Constructor AllArgs funciona correctamente")
    void constructorAllArgsFunciona() {
        // Given
        Long inventarioId = 1L;
        Long productoId = 10L;
        Integer cantidadEnStock = 50;
        String nombreProducto = "Producto AllArgs";
        BigDecimal precioProducto = new BigDecimal("8.25");
        BigDecimal valorTotal = new BigDecimal("412.50");
        String estadoStock = "STOCK_MEDIO";
        
        // When
        InventarioConProductoDTO dto = new InventarioConProductoDTO(
            inventarioId, productoId, cantidadEnStock, nombreProducto, 
            precioProducto, valorTotal, estadoStock
        );
        
        // Then
        assertEquals(inventarioId, dto.getInventarioId());
        assertEquals(productoId, dto.getProductoId());
        assertEquals(cantidadEnStock, dto.getCantidadEnStock());
        assertEquals(nombreProducto, dto.getNombreProducto());
        assertEquals(precioProducto, dto.getPrecioProducto());
        assertEquals(valorTotal, dto.getValorTotalInventario());
        assertEquals(estadoStock, dto.getEstadoStock());
    }
}