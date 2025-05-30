package com.jsuarez.productos_api.controller;


import com.jsuarez.productos_api.dto.*;
import com.jsuarez.productos_api.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoResponseDto responseDto;
    private ProductoRequestDto requestDto;

    @BeforeEach
    void setUp() {
        // Setup response DTO
        responseDto = new ProductoResponseDto();
        responseDto.setId("1");
        responseDto.setType("productos");
        ProductoResponseDto.ProductoAttributes attributes = new ProductoResponseDto.ProductoAttributes();
        attributes.setNombre("Cloro granulado 25kg");
        attributes.setPrecio(new BigDecimal("45.50"));
        responseDto.setAttributes(attributes);

        // Setup request DTO
        requestDto = new ProductoRequestDto();
        requestDto.setNombre("Cloro granulado 25kg");
        requestDto.setPrecio(new BigDecimal("45.50"));
    }

    @Test
    void testGetAllProductos() throws Exception {
        // Given
        Page<ProductoResponseDto> page = new PageImpl<>(Arrays.asList(responseDto), PageRequest.of(0, 10), 1);
        when(productoService.getAllProductos(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].type").value("productos"))
                .andExpect(jsonPath("$.data[0].attributes.nombre").value("Cloro granulado 25kg"));
    }

    @Test
    void testGetProductoById() throws Exception {
        // Given
        when(productoService.getProductoById(1L)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.type").value("productos"))
                .andExpect(jsonPath("$.data.attributes.nombre").value("Cloro granulado 25kg"));
    }

    @Test
    void testCreateProducto() throws Exception {
        // Given
        when(productoService.createProducto(any(ProductoRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.message").value("Producto creado exitosamente"));
    }

    @Test
    void testUpdateProducto() throws Exception {
        // Given
        when(productoService.updateProducto(eq(1L), any(ProductoRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(put("/api/v1/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.message").value("Producto actualizado exitosamente"));
    }

    @Test
    void testDeleteProducto() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Producto eliminado exitosamente"));
    }
}