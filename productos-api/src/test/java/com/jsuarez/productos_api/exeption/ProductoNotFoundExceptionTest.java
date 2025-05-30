package com.jsuarez.productos_api.exeption;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import com.jsuarez.productos_api.exception.ProductoNotFoundException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProductoNotFoundExceptionTest {

	   @Test
	    void testProductoNotFoundExceptionWithMessage() {
	        // Given
	        String errorMessage = "Producto no encontrado con ID: 999";
	        
	        // When
	        ProductoNotFoundException exception = new ProductoNotFoundException(errorMessage);
	        
	        // Then
	        assertEquals(errorMessage, exception.getMessage());
	        assertNotNull(exception);
	    }

	    @Test
	    void testProductoNotFoundExceptionIsRuntimeException() {
	        // Given
	        ProductoNotFoundException exception = new ProductoNotFoundException("Test message");
	        
	        // When & Then
	        assertTrue(exception instanceof RuntimeException);
	    }
	}