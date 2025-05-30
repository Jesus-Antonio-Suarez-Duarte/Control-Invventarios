package com.jsuarez.inventario_api.dto;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para JsonApiResponse")
class JsonApiResponseTest {
    
    @Test
    @DisplayName("✅ Constructor sin parámetros")
    void constructorSinParametros() {
        // When
        JsonApiResponse<String> response = new JsonApiResponse<>();
        
        // Then
        assertNull(response.getData());
        assertNull(response.getMessage());
        assertNull(response.getMeta());
    }
    
    @Test
    @DisplayName("✅ Constructor AllArgs")
    void constructorAllArgs() {
        // Given
        List<String> data = Arrays.asList("item1", "item2");
        String message = "Mensaje test";
        MetaPaginacion meta = new MetaPaginacion(1, 10, 2, 1, true, true);
        
        // When
        JsonApiResponse<String> response = new JsonApiResponse<>(data, message, meta);
        
        // Then
        assertEquals(data, response.getData());
        assertEquals(message, response.getMessage());
        assertEquals(meta, response.getMeta());
    }
    
    @Test
    @DisplayName("✅ Constructor simple sin paginación")
    void constructorSimpleSinPaginacion() {
        // Given
        List<String> data = Arrays.asList("item1", "item2", "item3");
        String message = "Datos obtenidos exitosamente";
        
        // When
        JsonApiResponse<String> response = new JsonApiResponse<>(data, message);
        
        // Then
        assertEquals(data, response.getData());
        assertEquals(message, response.getMessage());
        assertNull(response.getMeta(), "Meta debe ser null en constructor simple");
    }
    
    @Test
    @DisplayName("✅ Constructor con Page para paginación")
    void constructorConPageParaPaginacion() {
        // Given
        List<String> content = Arrays.asList("item1", "item2", "item3");
        Pageable pageable = PageRequest.of(0, 5); // Página 0, tamaño 5
        Page<String> page = new PageImpl<>(content, pageable, 8); // 8 elementos totales
        String message = "Página obtenida exitosamente";
        
        // When
        JsonApiResponse<String> response = new JsonApiResponse<>(page, message);
        
        // Then
        assertEquals(content, response.getData());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getMeta());
        
        MetaPaginacion meta = response.getMeta();
        assertEquals(1, meta.getPage(), "Página debe ser 1-based (0 + 1)");
        assertEquals(5, meta.getSize());
        assertEquals(8, meta.getTotalElements());
        assertEquals(2, meta.getTotalPages());
        assertTrue(meta.isFirst());
        assertFalse(meta.isLast());
    }
    
    @Test
    @DisplayName("✅ Constructor para un solo elemento")
    void constructorParaUnSoloElemento() {
        // Given
        String singleItem = "item único";
        String message = "Elemento encontrado";
        
        // When
        JsonApiResponse<String> response = new JsonApiResponse<>(singleItem, message);
        
        // Then
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertEquals(singleItem, response.getData().get(0));
        assertEquals(message, response.getMessage());
        assertNull(response.getMeta());
    }
    
    @Test
    @DisplayName("✅ Constructor con Page vacía")
    void constructorConPageVacia() {
        // Given
        List<String> emptyContent = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<String> emptyPage = new PageImpl<>(emptyContent, pageable, 0);
        String message = "No hay datos";
        
        // When
        JsonApiResponse<String> response = new JsonApiResponse<>(emptyPage, message);
        
        // Then
        assertTrue(response.getData().isEmpty());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getMeta());
        
        MetaPaginacion meta = response.getMeta();
        assertEquals(1, meta.getPage());
        assertEquals(10, meta.getSize());
        assertEquals(0, meta.getTotalElements());
        assertEquals(0, meta.getTotalPages());
        assertTrue(meta.isFirst());
        assertTrue(meta.isLast());
    }
    
    @Test
    @DisplayName("✅ Constructor con Page - última página")
    void constructorConPageUltimaPagina() {
        // Given
        List<String> content = Arrays.asList("item1", "item2");
        Pageable pageable = PageRequest.of(2, 3); // Página 2, tamaño 3
        Page<String> page = new PageImpl<>(content, pageable, 8); // 8 elementos totales
        String message = "Última página";
        
        // When
        JsonApiResponse<String> response = new JsonApiResponse<>(page, message);
        
        // Then
        assertEquals(content, response.getData());
        assertEquals(message, response.getMessage());
        
        MetaPaginacion meta = response.getMeta();
        assertEquals(3, meta.getPage(), "Página debe ser 3 (2 + 1)");
        assertEquals(3, meta.getSize());
        assertEquals(8, meta.getTotalElements());
        assertEquals(3, meta.getTotalPages());
        assertFalse(meta.isFirst());
        assertTrue(meta.isLast());
    }
    
    @Test
    @DisplayName("✅ Setters funcionan correctamente")
    void settersFuncionanCorrectamente() {
        // Given
        JsonApiResponse<String> response = new JsonApiResponse<>();
        List<String> newData = Arrays.asList("nuevo1", "nuevo2");
        String newMessage = "Mensaje actualizado";
        MetaPaginacion newMeta = new MetaPaginacion(2, 5, 10, 2, false, true);
        
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
        List<String> data = Arrays.asList("item1", "item2");
        String message = "Mensaje test";
        MetaPaginacion meta = new MetaPaginacion(1, 10, 2, 1, true, true);
        
        JsonApiResponse<String> response1 = new JsonApiResponse<>(data, message, meta);
        JsonApiResponse<String> response2 = new JsonApiResponse<>(data, message, meta);
        JsonApiResponse<String> response3 = new JsonApiResponse<>(
            Arrays.asList("otro"), "otro mensaje", null
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
        List<String> data = Arrays.asList("item1", "item2");
        String message = "Mensaje test";
        JsonApiResponse<String> response = new JsonApiResponse<>(data, message);
        
        // When
        String toString = response.toString();
        
        // Then
        assertNotNull(toString, "ToString no debe ser null");
        assertTrue(toString.contains("item1"), "ToString debe contener datos");
        assertTrue(toString.contains("Mensaje test"), "ToString debe contener mensaje");
    }
    
    @Test
    @DisplayName("✅ Manejo de valores null")
    void manejoValoresNull() {
        // When - Usando constructor AllArgs para evitar ambigüedad
        JsonApiResponse<String> response1 = new JsonApiResponse<String>(null, null, null);
        JsonApiResponse<String> response2 = new JsonApiResponse<String>(null, "Mensaje con data null", null);
        JsonApiResponse<String> response3 = new JsonApiResponse<String>("item", null);
        
        // Then
        assertNull(response1.getData());
        assertNull(response1.getMessage());
        assertNull(response1.getMeta());
        
        assertNull(response2.getData());
        assertEquals("Mensaje con data null", response2.getMessage());
        assertNull(response2.getMeta());
        
        assertEquals(1, response3.getData().size());
        assertEquals("item", response3.getData().get(0));
        assertNull(response3.getMessage());
        assertNull(response3.getMeta());
    }
    
    @Test
    @DisplayName("✅ Test con diferentes tipos genéricos")
    void testConDiferentesTiposGenericos() {
        // Given
        List<Integer> intData = Arrays.asList(1, 2, 3);
        List<Boolean> boolData = Arrays.asList(true, false);
        
        // When
        JsonApiResponse<Integer> intResponse = new JsonApiResponse<>(intData, "Enteros");
        JsonApiResponse<Boolean> boolResponse = new JsonApiResponse<>(boolData, "Booleanos");
        
        // Then
        assertEquals(intData, intResponse.getData());
        assertEquals("Enteros", intResponse.getMessage());
        
        assertEquals(boolData, boolResponse.getData());
        assertEquals("Booleanos", boolResponse.getMessage());
    }
    
    @Test
    @DisplayName("✅ Test inmutabilidad de lista en constructor single item")
    void testInmutabilidadListaConstructorSingleItem() {
        // Given
        String item = "item test";
        
        // When
        JsonApiResponse<String> response = new JsonApiResponse<>(item, "mensaje");
        
        // Then
        // Verificamos que la lista no es null y tiene el elemento
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertEquals("item test", response.getData().get(0));
        
        // Verificamos que es una lista inmutable intentando modificarla
        assertThrows(UnsupportedOperationException.class, () -> {
            response.getData().add("nuevo item");
        }, "La lista devuelta por List.of() debe ser inmutable");
    }
    
    @Test
    @DisplayName("✅ Test constructor con Page de elementos complejos")
    void testConstructorConPageElementosComplejos() {
        // Given
        List<InventarioConProductoDTO> content = Arrays.asList(
            new InventarioConProductoDTO(1L, 10L, 100, "Producto 1", java.math.BigDecimal.valueOf(10.0)),
            new InventarioConProductoDTO(2L, 20L, 200, "Producto 2", java.math.BigDecimal.valueOf(20.0))
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<InventarioConProductoDTO> page = new PageImpl<>(content, pageable, 2);
        String message = "Inventarios obtenidos";
        
        // When
        JsonApiResponse<InventarioConProductoDTO> response = new JsonApiResponse<>(page, message);
        
        // Then
        assertEquals(content, response.getData());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getMeta());
        assertEquals(2, response.getData().size());
        assertEquals("Producto 1", response.getData().get(0).getNombreProducto());
        assertEquals("Producto 2", response.getData().get(1).getNombreProducto());
    }
}