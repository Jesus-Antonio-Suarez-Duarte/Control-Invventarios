package com.jsuarez.productos_api.exeption;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jsuarez.productos_api.exception.GlobalExceptionHandler;
import com.jsuarez.productos_api.exception.ProductoNotFoundException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {
	
    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleProductoNotFoundException() {
        // Given
        String errorMessage = "Producto no encontrado con ID: 999";
        ProductoNotFoundException exception = new ProductoNotFoundException(errorMessage);
        
        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleProductoNotFoundException(exception);
        
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Producto no encontrado", response.getBody().get("error"));
        assertEquals(errorMessage, response.getBody().get("message"));
    }

    @Test
    void testHandleProductoNotFoundExceptionResponseStructure() {
        // Given
        ProductoNotFoundException exception = new ProductoNotFoundException("Test error");
        
        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleProductoNotFoundException(exception);
        
        // Then
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("error"));
        assertTrue(body.containsKey("message"));
        assertEquals(2, body.size()); // Solo debe tener error y message
    }

    @Test
    void testGlobalExceptionHandlerNotNull() {
        // When & Then
        assertNotNull(exceptionHandler);
    }
}