package com.jsuarez.inventario_api.entity;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para Inventario Entity")
class InventarioTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    @DisplayName("✅ Crear inventario válido")
    void crearInventarioValido() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(10L);
        inventario.setCantidad(100);
        
        // When
        Set<ConstraintViolation<Inventario>> violations = validator.validate(inventario);
        
        // Then
        assertTrue(violations.isEmpty(), "No debe haber violaciones de validación");
        assertEquals(1L, inventario.getId());
        assertEquals(10L, inventario.getProductoId());
        assertEquals(100, inventario.getCantidad());
    }
    
    @Test
    @DisplayName("❌ Validar que productoId no puede ser null")
    void productoIdNoPuedeSerNull() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setProductoId(null);
        inventario.setCantidad(50);
        
        // When
        Set<ConstraintViolation<Inventario>> violations = validator.validate(inventario);
        
        // Then
        assertFalse(violations.isEmpty(), "Debe haber violaciones de validación");
        assertEquals(1, violations.size());
        
        ConstraintViolation<Inventario> violation = violations.iterator().next();
        assertEquals("El ID del producto es obligatorio", violation.getMessage());
        assertEquals("productoId", violation.getPropertyPath().toString());
    }
    
    @Test
    @DisplayName("❌ Validar que productoId debe ser mayor a 0")
    void productoIdDebeSerMayorACero() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setProductoId(0L);
        inventario.setCantidad(50);
        
        // When
        Set<ConstraintViolation<Inventario>> violations = validator.validate(inventario);
        
        // Then
        assertFalse(violations.isEmpty(), "Debe haber violaciones de validación");
        
        boolean foundCorrectViolation = violations.stream()
            .anyMatch(v -> v.getMessage().equals("El ID del producto debe ser mayor a 0"));
        
        assertTrue(foundCorrectViolation, "Debe encontrar la violación de ID del producto mayor a 0");
    }
    
    @Test
    @DisplayName("❌ Validar que cantidad no puede ser null")
    void cantidadNoPuedeSerNull() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setProductoId(10L);
        inventario.setCantidad(null);
        
        // When
        Set<ConstraintViolation<Inventario>> violations = validator.validate(inventario);
        
        // Then
        assertFalse(violations.isEmpty(), "Debe haber violaciones de validación");
        
        boolean foundCorrectViolation = violations.stream()
            .anyMatch(v -> v.getMessage().equals("La cantidad es obligatoria"));
        
        assertTrue(foundCorrectViolation, "Debe encontrar la violación de cantidad obligatoria");
    }
    
    @Test
    @DisplayName("❌ Validar que cantidad no puede ser negativa")
    void cantidadNoPuedeSerNegativa() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setProductoId(10L);
        inventario.setCantidad(-5);
        
        // When
        Set<ConstraintViolation<Inventario>> violations = validator.validate(inventario);
        
        // Then
        assertFalse(violations.isEmpty(), "Debe haber violaciones de validación");
        
        boolean foundCorrectViolation = violations.stream()
            .anyMatch(v -> v.getMessage().equals("La cantidad no puede ser negativa"));
        
        assertTrue(foundCorrectViolation, "Debe encontrar la violación de cantidad no negativa");
    }
    
    @Test
    @DisplayName("✅ Validar que cantidad puede ser cero")
    void cantidadPuedeSerCero() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setProductoId(10L);
        inventario.setCantidad(0);
        
        // When
        Set<ConstraintViolation<Inventario>> violations = validator.validate(inventario);
        
        // Then
        assertTrue(violations.isEmpty(), "No debe haber violaciones cuando cantidad es 0");
        assertEquals(0, inventario.getCantidad());
    }
    
    @Test
    @DisplayName("✅ Test equals y hashCode")
    void testEqualsYHashCode() {
        // Given
        Inventario inventario1 = new Inventario(1L, 10L, 100);
        Inventario inventario2 = new Inventario(1L, 10L, 100);
        Inventario inventario3 = new Inventario(2L, 20L, 200);
        
        // Then
        assertEquals(inventario1, inventario2, "Inventarios con mismos datos deben ser iguales");
        assertNotEquals(inventario1, inventario3, "Inventarios con datos diferentes no deben ser iguales");
        assertEquals(inventario1.hashCode(), inventario2.hashCode(), "HashCode debe ser igual para objetos iguales");
    }
    
    @Test
    @DisplayName("✅ Test toString")
    void testToString() {
        // Given
        Inventario inventario = new Inventario(1L, 10L, 100);
        
        // When
        String toString = inventario.toString();
        
        // Then
        assertNotNull(toString, "ToString no debe ser null");
        assertTrue(toString.contains("1"), "ToString debe contener el ID");
        assertTrue(toString.contains("10"), "ToString debe contener el productoId");
        assertTrue(toString.contains("100"), "ToString debe contener la cantidad");
    }
    
    @Test
    @DisplayName("✅ Test constructor con parámetros")
    void testConstructorConParametros() {
        // When
        Inventario inventario = new Inventario(5L, 25L, 150);
        
        // Then
        assertEquals(5L, inventario.getId());
        assertEquals(25L, inventario.getProductoId());
        assertEquals(150, inventario.getCantidad());
    }
    
    @Test
    @DisplayName("✅ Test constructor sin parámetros")
    void testConstructorSinParametros() {
        // When
        Inventario inventario = new Inventario();
        
        // Then
        assertNull(inventario.getId());
        assertNull(inventario.getProductoId());
        assertNull(inventario.getCantidad());
    }
}