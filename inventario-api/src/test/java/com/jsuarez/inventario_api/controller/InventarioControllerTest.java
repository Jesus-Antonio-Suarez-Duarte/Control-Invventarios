package com.jsuarez.inventario_api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsuarez.inventario_api.dto.*;
import com.jsuarez.inventario_api.entity.*;
import com.jsuarez.inventario_api.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
@DisplayName("Tests para InventarioController")
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventarioService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Inventario inventarioMock;
    private InventarioConProductoDTO inventarioCompletoMock;
    private CompraResponse compraResponseMock;

    @BeforeEach
    void setUp() {
        // Mock de Inventario básico
        inventarioMock = new Inventario();
        inventarioMock.setId(1L);
        inventarioMock.setProductoId(10L);
        inventarioMock.setCantidad(100);

        // Mock de InventarioConProductoDTO
        inventarioCompletoMock = new InventarioConProductoDTO(
            1L, 10L, 100, "Producto Test", new BigDecimal("50.00")
        );

        // Mock de CompraResponse
        compraResponseMock = new CompraResponse(
            1L, 10L, "Producto Test", 25, 75
        );
    }

    @Test
    @DisplayName("✅ GET /inventario - Obtener todos los inventarios exitosamente")
    void obtenerTodosLosInventarios_Exitoso() throws Exception {
        // Given
        List<Inventario> inventarios = Arrays.asList(inventarioMock);
        when(inventarioService.obtenerTodosLosInventarios()).thenReturn(inventarios);

        // When & Then
        mockMvc.perform(get("/api/v1/inventario"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].productoId", is(10)))
                .andExpect(jsonPath("$.data[0].cantidad", is(100)))
                .andExpect(jsonPath("$.message", is("Inventarios obtenidos exitosamente")))
                .andExpect(jsonPath("$.meta").doesNotExist());

        verify(inventarioService, times(1)).obtenerTodosLosInventarios();
    }

    @Test
    @DisplayName("❌ GET /inventario - Error interno del servidor")
    void obtenerTodosLosInventarios_ErrorInterno() throws Exception {
        // Given
        when(inventarioService.obtenerTodosLosInventarios()).thenThrow(new RuntimeException("Error de base de datos"));

        // When & Then
        mockMvc.perform(get("/api/v1/inventario"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.message", is("Error al obtener inventarios")));

        verify(inventarioService, times(1)).obtenerTodosLosInventarios();
    }

    @Test
    @DisplayName("✅ GET /inventario/completo - Obtener inventarios completos paginados")
    void obtenerTodosLosInventariosCompletos_Exitoso() throws Exception {
        // Given
        List<InventarioConProductoDTO> inventarios = Arrays.asList(inventarioCompletoMock);
        Page<InventarioConProductoDTO> page = new PageImpl<>(inventarios, PageRequest.of(0, 10), 1);
        when(inventarioService.obtenerTodosLosInventariosCompletos(any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/v1/inventario/completo")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].inventarioId", is(1)))
                .andExpect(jsonPath("$.data[0].productoId", is(10)))
                .andExpect(jsonPath("$.data[0].cantidadEnStock", is(100)))
                .andExpect(jsonPath("$.data[0].nombreProducto", is("Producto Test")))
                .andExpect(jsonPath("$.data[0].precioProducto", is(50.00)))
                .andExpect(jsonPath("$.message", is("Inventarios completos obtenidos exitosamente")))
                .andExpect(jsonPath("$.meta.page", is(1)))
                .andExpect(jsonPath("$.meta.size", is(10)))
                .andExpect(jsonPath("$.meta.totalElements", is(1)))
                .andExpect(jsonPath("$.meta.first", is(true)))
                .andExpect(jsonPath("$.meta.last", is(true)));

        verify(inventarioService, times(1)).obtenerTodosLosInventariosCompletos(any(Pageable.class));
    }

    @Test
    @DisplayName("❌ GET /inventario/completo - Error de microservicio")
    void obtenerTodosLosInventariosCompletos_ErrorMicroservicio() throws Exception {
        // Given
        when(inventarioService.obtenerTodosLosInventariosCompletos(any(Pageable.class)))
                .thenThrow(new RuntimeException("Error de comunicación"));

        // When & Then
        mockMvc.perform(get("/api/v1/inventario/completo"))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.message", containsString("Error al obtener inventarios completos")));
    }

    @Test
    @DisplayName("✅ GET /inventario/producto/{id} - Encontrar inventario por producto ID")
    void obtenerInventarioPorProductoId_Encontrado() throws Exception {
        // Given
        Long productoId = 10L;
        when(inventarioService.obtenerInventarioPorProductoId(productoId)).thenReturn(Optional.of(inventarioMock));

        // When & Then
        mockMvc.perform(get("/api/v1/inventario/producto/{productoId}", productoId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].productoId", is(10)))
                .andExpect(jsonPath("$.message", is("Inventario encontrado exitosamente")));

        verify(inventarioService, times(1)).obtenerInventarioPorProductoId(productoId);
    }

    @Test
    @DisplayName("❌ GET /inventario/producto/{id} - Inventario no encontrado")
    void obtenerInventarioPorProductoId_NoEncontrado() throws Exception {
        // Given
        Long productoId = 999L;
        when(inventarioService.obtenerInventarioPorProductoId(productoId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/inventario/producto/{productoId}", productoId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.message", containsString("No se encontró inventario para el producto ID: 999")));

        verify(inventarioService, times(1)).obtenerInventarioPorProductoId(productoId);
    }

    @Test
    @DisplayName("✅ GET /inventario/producto/{id}/completo - Inventario completo encontrado")
    void obtenerInventarioCompletoPorProductoId_Encontrado() throws Exception {
        // Given
        Long productoId = 10L;
        when(inventarioService.obtenerInventarioCompletoPorProductoId(productoId))
                .thenReturn(Optional.of(inventarioCompletoMock));

        // When & Then
        mockMvc.perform(get("/api/v1/inventario/producto/{productoId}/completo", productoId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].inventarioId", is(1)))
                .andExpect(jsonPath("$.data[0].nombreProducto", is("Producto Test")))
                .andExpect(jsonPath("$.message", containsString("Inventario completo encontrado exitosamente")));

        verify(inventarioService, times(1)).obtenerInventarioCompletoPorProductoId(productoId);
    }

    @Test
    @DisplayName("❌ GET /inventario/producto/{id}/completo - Error de comunicación")
    void obtenerInventarioCompletoPorProductoId_ErrorComunicacion() throws Exception {
        // Given
        Long productoId = 10L;
        when(inventarioService.obtenerInventarioCompletoPorProductoId(productoId))
                .thenThrow(new RuntimeException("Error de comunicación"));

        // When & Then
        mockMvc.perform(get("/api/v1/inventario/producto/{productoId}/completo", productoId))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message", containsString("Error al obtener inventario completo")));
    }

    @Test
    @DisplayName("✅ POST /inventario - Crear inventario exitosamente")
    void crearInventario_Exitoso() throws Exception {
        // Given
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setProductoId(10L);
        nuevoInventario.setCantidad(50);

        when(inventarioService.guardarInventario(any(Inventario.class))).thenReturn(inventarioMock);

        // When & Then
        mockMvc.perform(post("/api/v1/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoInventario)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.message", containsString("Inventario creado/actualizado exitosamente")));

        verify(inventarioService, times(1)).guardarInventario(any(Inventario.class));
    }

    @Test
    @DisplayName("❌ POST /inventario - Error de validación")
    void crearInventario_ErrorValidacion() throws Exception {
        // Given
        Inventario inventarioInvalido = new Inventario();
        inventarioInvalido.setProductoId(null); // Inválido
        inventarioInvalido.setCantidad(50);

        // When & Then
        mockMvc.perform(post("/api/v1/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventarioInvalido)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("✅ POST /inventario/con-validacion - Crear con validación exitosa")
    void crearInventarioConValidacion_Exitoso() throws Exception {
        // Given
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setProductoId(10L);
        nuevoInventario.setCantidad(50);

        when(inventarioService.crearInventarioConValidacion(any(Inventario.class)))
                .thenReturn(inventarioCompletoMock);

        // When & Then
        mockMvc.perform(post("/api/v1/inventario/con-validacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoInventario)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].nombreProducto", is("Producto Test")))
                .andExpect(jsonPath("$.message", containsString("Inventario creado exitosamente con validación")));

        verify(inventarioService, times(1)).crearInventarioConValidacion(any(Inventario.class));
    }

    @Test
    @DisplayName("❌ POST /inventario/con-validacion - Producto no existe")
    void crearInventarioConValidacion_ProductoNoExiste() throws Exception {
        // Given
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setProductoId(999L);
        nuevoInventario.setCantidad(50);

        when(inventarioService.crearInventarioConValidacion(any(Inventario.class)))
                .thenThrow(new RuntimeException("No se puede crear inventario: el producto ID 999 no existe"));

        // When & Then
        mockMvc.perform(post("/api/v1/inventario/con-validacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoInventario)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Error de validación")));
    }

    @Test
    @DisplayName("❌ POST /inventario/con-validacion - Error de conectividad")
    void crearInventarioConValidacion_ErrorConectividad() throws Exception {
        // Given
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setProductoId(10L);
        nuevoInventario.setCantidad(50);

        when(inventarioService.crearInventarioConValidacion(any(Inventario.class)))
                .thenThrow(new RuntimeException("No se pudo conectar con el microservicio"));

        // When & Then
        mockMvc.perform(post("/api/v1/inventario/con-validacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoInventario)))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message", containsString("Error de conectividad")));
    }

    @Test
    @DisplayName("✅ PUT /inventario/producto/{id}/cantidad - Actualizar cantidad exitosamente")
    void actualizarCantidad_Exitoso() throws Exception {
        // Given
        Long productoId = 10L;
        Integer nuevaCantidad = 150;
        Inventario inventarioActualizado = new Inventario(1L, productoId, nuevaCantidad);

        when(inventarioService.actualizarCantidad(productoId, nuevaCantidad)).thenReturn(inventarioActualizado);

        // When & Then
        mockMvc.perform(put("/api/v1/inventario/producto/{productoId}/cantidad", productoId)
                        .param("cantidad", nuevaCantidad.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].cantidad", is(150)))
                .andExpect(jsonPath("$.message", is("✅ Cantidad actualizada exitosamente")));

        verify(inventarioService, times(1)).actualizarCantidad(productoId, nuevaCantidad);
    }

    @Test
    @DisplayName("❌ PUT /inventario/producto/{id}/cantidad - Producto no encontrado")
    void actualizarCantidad_ProductoNoEncontrado() throws Exception {
        // Given
        Long productoId = 999L;
        Integer nuevaCantidad = 150;

        when(inventarioService.actualizarCantidad(productoId, nuevaCantidad))
                .thenThrow(new RuntimeException("No se encontró inventario para el producto ID: 999"));

        // When & Then
        mockMvc.perform(put("/api/v1/inventario/producto/{productoId}/cantidad", productoId)
                        .param("cantidad", nuevaCantidad.toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("No se encontró inventario")));
    }

    @Test
    @DisplayName("✅ PUT /inventario/producto/{id}/compra - Procesar compra exitosamente")
    void procesarCompra_Exitoso() throws Exception {
        // Given
        Long productoId = 10L;
        Integer cantidadComprada = 25;

        when(inventarioService.reducirCantidadPorCompra(productoId, cantidadComprada))
                .thenReturn(compraResponseMock);

        // When & Then
        mockMvc.perform(put("/api/v1/inventario/producto/{productoId}/compra", productoId)
                        .param("cantidadComprada", cantidadComprada.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].cantidadComprada", is(25)))
                .andExpect(jsonPath("$.data[0].cantidadRestante", is(75)))
                .andExpect(jsonPath("$.data[0].estadoCompra", is("EXITOSA")))
                .andExpect(jsonPath("$.message", containsString("Compra procesada correctamente")));

        verify(inventarioService, times(1)).reducirCantidadPorCompra(productoId, cantidadComprada);
    }

    @Test
    @DisplayName("❌ PUT /inventario/producto/{id}/compra - Stock insuficiente")
    void procesarCompra_StockInsuficiente() throws Exception {
        // Given
        Long productoId = 10L;
        Integer cantidadComprada = 200;

        when(inventarioService.reducirCantidadPorCompra(productoId, cantidadComprada))
                .thenThrow(new RuntimeException("Stock insuficiente. Disponible: 100, Solicitado: 200"));

        // When & Then
        mockMvc.perform(put("/api/v1/inventario/producto/{productoId}/compra", productoId)
                        .param("cantidadComprada", cantidadComprada.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Stock insuficiente")));
    }

    @Test
    @DisplayName("✅ DELETE /inventario/producto/{id} - Eliminar inventario exitosamente")
    void eliminarInventario_Exitoso() throws Exception {
        // Given
        Long productoId = 10L;
        when(inventarioService.eliminarInventarioPorProductoId(productoId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/v1/inventario/producto/{productoId}", productoId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("✅ Inventario eliminado exitosamente")));

        verify(inventarioService, times(1)).eliminarInventarioPorProductoId(productoId);
    }

    @Test
    @DisplayName("❌ DELETE /inventario/producto/{id} - Inventario no encontrado para eliminar")
    void eliminarInventario_NoEncontrado() throws Exception {
        // Given
        Long productoId = 999L;
        when(inventarioService.eliminarInventarioPorProductoId(productoId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/inventario/producto/{productoId}", productoId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("❌ No se encontró inventario para eliminar")));

        verify(inventarioService, times(1)).eliminarInventarioPorProductoId(productoId);
    }
}