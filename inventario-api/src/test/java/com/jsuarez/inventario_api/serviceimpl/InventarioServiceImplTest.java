package com.jsuarez.inventario_api.serviceimpl;


import com.jsuarez.inventario_api.client.ProductoClient;
import com.jsuarez.inventario_api.dto.*;
import com.jsuarez.inventario_api.entity.*;
import com.jsuarez.inventario_api.repository.*;
import com.jsuarez.inventario_api.service.impl.InventarioServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para InventarioServiceImpl")
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Inventario inventarioMock;
    private ProductoSimple productoMock;
    private InventarioConProductoDTO inventarioCompletoMock;

    @BeforeEach
    void setUp() {
        // Mock de Inventario
        inventarioMock = new Inventario();
        inventarioMock.setId(1L);
        inventarioMock.setProductoId(10L);
        inventarioMock.setCantidad(100);

        // Mock de ProductoSimple
        productoMock = new ProductoSimple();
        productoMock.setId(10L);
        productoMock.setNombre("Producto Test");
        productoMock.setPrecio(new BigDecimal("50.00"));

        // Mock de InventarioConProductoDTO
        inventarioCompletoMock = new InventarioConProductoDTO(
            1L, 10L, 100, "Producto Test", new BigDecimal("50.00")
        );
    }

    // ====================== TESTS DE CONSULTA ======================

    @Test
    @DisplayName("✅ obtenerInventarioPorProductoId - Inventario encontrado")
    void obtenerInventarioPorProductoId_Encontrado() {
        // Given
        Long productoId = 10L;
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioMock));

        // When
        Optional<Inventario> resultado = inventarioService.obtenerInventarioPorProductoId(productoId);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(inventarioMock, resultado.get());
        verify(inventarioRepository, times(1)).findByProductoId(productoId);
    }

    @Test
    @DisplayName("❌ obtenerInventarioPorProductoId - Inventario no encontrado")
    void obtenerInventarioPorProductoId_NoEncontrado() {
        // Given
        Long productoId = 999L;
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.empty());

        // When
        Optional<Inventario> resultado = inventarioService.obtenerInventarioPorProductoId(productoId);

        // Then
        assertFalse(resultado.isPresent());
        verify(inventarioRepository, times(1)).findByProductoId(productoId);
    }

    @Test
    @DisplayName("✅ obtenerInventarioCompletoPorProductoId - Con producto encontrado")
    void obtenerInventarioCompletoPorProductoId_ConProductoEncontrado() {
        // Given
        Long productoId = 10L;
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioMock));
        when(productoClient.obtenerProductoPorId(productoId)).thenReturn(Optional.of(productoMock));

        // When
        Optional<InventarioConProductoDTO> resultado = inventarioService.obtenerInventarioCompletoPorProductoId(productoId);

        // Then
        assertTrue(resultado.isPresent());
        InventarioConProductoDTO dto = resultado.get();
        assertEquals(1L, dto.getInventarioId());
        assertEquals(10L, dto.getProductoId());
        assertEquals(100, dto.getCantidadEnStock());
        assertEquals("Producto Test", dto.getNombreProducto());
        assertEquals(new BigDecimal("50.00"), dto.getPrecioProducto());

        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(productoClient, times(1)).obtenerProductoPorId(productoId);
    }

    @Test
    @DisplayName("❌ obtenerInventarioCompletoPorProductoId - Inventario no encontrado")
    void obtenerInventarioCompletoPorProductoId_InventarioNoEncontrado() {
        // Given
        Long productoId = 999L;
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.empty());

        // When
        Optional<InventarioConProductoDTO> resultado = inventarioService.obtenerInventarioCompletoPorProductoId(productoId);

        // Then
        assertFalse(resultado.isPresent());
        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(productoClient, never()).obtenerProductoPorId(any());
    }

    @Test
    @DisplayName("⚠️ obtenerInventarioCompletoPorProductoId - Producto no encontrado en microservicio")
    void obtenerInventarioCompletoPorProductoId_ProductoNoEncontrado() {
        // Given
        Long productoId = 10L;
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioMock));
        when(productoClient.obtenerProductoPorId(productoId)).thenReturn(Optional.empty());

        // When
        Optional<InventarioConProductoDTO> resultado = inventarioService.obtenerInventarioCompletoPorProductoId(productoId);

        // Then
        assertTrue(resultado.isPresent());
        InventarioConProductoDTO dto = resultado.get();
        assertEquals("PRODUCTO NO ENCONTRADO", dto.getNombreProducto());
        assertNull(dto.getPrecioProducto());

        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(productoClient, times(1)).obtenerProductoPorId(productoId);
    }

    @Test
    @DisplayName("❌ obtenerInventarioCompletoPorProductoId - Error de comunicación")
    void obtenerInventarioCompletoPorProductoId_ErrorComunicacion() {
        // Given
        Long productoId = 10L;
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioMock));
        when(productoClient.obtenerProductoPorId(productoId))
                .thenThrow(new RuntimeException("Error de conectividad"));

        // When
        Optional<InventarioConProductoDTO> resultado = inventarioService.obtenerInventarioCompletoPorProductoId(productoId);

        // Then
        assertTrue(resultado.isPresent());
        InventarioConProductoDTO dto = resultado.get();
        assertEquals("SERVICIO NO DISPONIBLE", dto.getNombreProducto());
        assertNull(dto.getPrecioProducto());

        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(productoClient, times(1)).obtenerProductoPorId(productoId);
    }

    @Test
    @DisplayName("✅ obtenerTodosLosInventarios - Lista exitosa")
    void obtenerTodosLosInventarios_Exitoso() {
        // Given
        List<Inventario> inventarios = Arrays.asList(inventarioMock);
        when(inventarioRepository.findAll()).thenReturn(inventarios);

        // When
        List<Inventario> resultado = inventarioService.obtenerTodosLosInventarios();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(inventarioMock, resultado.get(0));
        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("✅ obtenerTodosLosInventariosCompletos - Paginación exitosa")
    void obtenerTodosLosInventariosCompletos_PaginacionExitosa() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Inventario> inventarios = Arrays.asList(inventarioMock);
        Page<Inventario> pageInventarios = new PageImpl<>(inventarios, pageable, 1);

        when(inventarioRepository.findAll(pageable)).thenReturn(pageInventarios);
        when(productoClient.obtenerProductoPorId(10L)).thenReturn(Optional.of(productoMock));

        // When
        Page<InventarioConProductoDTO> resultado = inventarioService.obtenerTodosLosInventariosCompletos(pageable);

        // Then
        assertEquals(1, resultado.getContent().size());
        assertEquals("Producto Test", resultado.getContent().get(0).getNombreProducto());
        assertEquals(1, resultado.getTotalElements());
        assertTrue(resultado.isFirst());
        assertTrue(resultado.isLast());

        verify(inventarioRepository, times(1)).findAll(pageable);
        verify(productoClient, times(1)).obtenerProductoPorId(10L);
    }

    // ====================== TESTS DE CREACIÓN/ACTUALIZACIÓN ======================

    @Test
    @DisplayName("✅ guardarInventario - Crear nuevo inventario")
    void guardarInventario_CrearNuevo() {
        // Given
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setProductoId(20L);
        nuevoInventario.setCantidad(50);

        when(inventarioRepository.findByProductoId(20L)).thenReturn(Optional.empty());
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioMock);

        // When
        Inventario resultado = inventarioService.guardarInventario(nuevoInventario);

        // Then
        assertEquals(inventarioMock, resultado);
        verify(inventarioRepository, times(1)).findByProductoId(20L);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    @DisplayName("✅ guardarInventario - Actualizar inventario existente")
    void guardarInventario_ActualizarExistente() {
        // Given
        Inventario inventarioExistente = new Inventario(1L, 10L, 75);
        Inventario inventarioActualizado = new Inventario();
        inventarioActualizado.setProductoId(10L);
        inventarioActualizado.setCantidad(125);

        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventarioExistente));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioExistente);

        // When
        Inventario resultado = inventarioService.guardarInventario(inventarioActualizado);

        // Then
        assertEquals(125, inventarioExistente.getCantidad()); // Verificar que se actualizó
        verify(inventarioRepository, times(1)).findByProductoId(10L);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    @DisplayName("✅ crearInventarioConValidacion - Producto existe")
    void crearInventarioConValidacion_ProductoExiste() {
        // Given
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setProductoId(10L);
        nuevoInventario.setCantidad(75);

        // Crear inventario guardado con ID asignado
        Inventario inventarioGuardado = new Inventario(1L, 10L, 75);

        // Configurar mocks
        when(productoClient.existeProducto(10L)).thenReturn(true);
        when(inventarioRepository.findByProductoId(10L))
            .thenReturn(Optional.empty())  // Primera llamada: no existe
            .thenReturn(Optional.of(inventarioGuardado)); // Segunda llamada: ya existe después de guardar
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioGuardado);
        when(productoClient.obtenerProductoPorId(10L)).thenReturn(Optional.of(productoMock));

        // When
        InventarioConProductoDTO resultado = inventarioService.crearInventarioConValidacion(nuevoInventario);

        // Then
        assertNotNull(resultado);
        assertEquals(10L, resultado.getProductoId());
        assertEquals("Producto Test", resultado.getNombreProducto());

        verify(productoClient, times(1)).existeProducto(10L);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
        verify(inventarioRepository, times(2)).findByProductoId(10L); // Se llama dos veces
    }

    @Test
    @DisplayName("❌ crearInventarioConValidacion - Producto no existe")
    void crearInventarioConValidacion_ProductoNoExiste() {
        // Given
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setProductoId(999L);
        nuevoInventario.setCantidad(75);

        when(productoClient.existeProducto(999L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.crearInventarioConValidacion(nuevoInventario);
        });

        assertTrue(exception.getMessage().contains("el producto ID 999 no existe"));
        verify(productoClient, times(1)).existeProducto(999L);
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("❌ crearInventarioConValidacion - Error de conectividad")
    void crearInventarioConValidacion_ErrorConectividad() {
        // Given
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setProductoId(10L);
        nuevoInventario.setCantidad(75);

        when(productoClient.existeProducto(10L)).thenThrow(new RuntimeException("No se pudo conectar"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.crearInventarioConValidacion(nuevoInventario);
        });

        assertTrue(exception.getMessage().contains("No se pudo conectar"));
        verify(productoClient, times(1)).existeProducto(10L);
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("✅ actualizarCantidad - Actualización exitosa")
    void actualizarCantidad_Exitoso() {
        // Given
        Long productoId = 10L;
        Integer nuevaCantidad = 150;

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioMock));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioMock);

        // When
        Inventario resultado = inventarioService.actualizarCantidad(productoId, nuevaCantidad);

        // Then
        assertEquals(150, inventarioMock.getCantidad());
        assertEquals(inventarioMock, resultado);
        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    @DisplayName("❌ actualizarCantidad - Inventario no encontrado")
    void actualizarCantidad_InventarioNoEncontrado() {
        // Given
        Long productoId = 999L;
        Integer nuevaCantidad = 150;

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.actualizarCantidad(productoId, nuevaCantidad);
        });

        assertTrue(exception.getMessage().contains("No se encontró inventario para el producto ID: 999"));
        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(inventarioRepository, never()).save(any());
    }

    // ====================== TESTS DE COMPRA ======================

    @Test
    @DisplayName("✅ reducirCantidadPorCompra - Compra exitosa")
    void reducirCantidadPorCompra_Exitoso() {
        // Given
        Long productoId = 10L;
        Integer cantidadComprada = 25;
        Inventario inventarioActualizado = new Inventario(1L, 10L, 75); // 100 - 25 = 75

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioMock));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioActualizado);
        when(productoClient.obtenerProductoPorId(productoId)).thenReturn(Optional.of(productoMock));

        // When
        CompraResponse resultado = inventarioService.reducirCantidadPorCompra(productoId, cantidadComprada);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getInventarioId());
        assertEquals(10L, resultado.getProductoId());
        assertEquals("Producto Test", resultado.getNombreProducto());
        assertEquals(25, resultado.getCantidadComprada());
        assertEquals(75, resultado.getCantidadRestante());
        assertEquals("EXITOSA", resultado.getEstadoCompra());
        assertTrue(resultado.getMensaje().contains("Compra procesada correctamente"));

        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
        verify(productoClient, times(1)).obtenerProductoPorId(productoId);
    }

    @Test
    @DisplayName("❌ reducirCantidadPorCompra - Stock insuficiente")
    void reducirCantidadPorCompra_StockInsuficiente() {
        // Given
        Long productoId = 10L;
        Integer cantidadComprada = 150; // Mayor que el stock (100)

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioMock));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.reducirCantidadPorCompra(productoId, cantidadComprada);
        });

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        assertTrue(exception.getMessage().contains("Disponible: 100"));
        assertTrue(exception.getMessage().contains("Solicitado: 150"));

        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(inventarioRepository, never()).save(any());
        verify(productoClient, never()).obtenerProductoPorId(any());
    }

    @Test
    @DisplayName("❌ reducirCantidadPorCompra - Inventario no encontrado")
    void reducirCantidadPorCompra_InventarioNoEncontrado() {
        // Given
        Long productoId = 999L;
        Integer cantidadComprada = 25;

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.reducirCantidadPorCompra(productoId, cantidadComprada);
        });

        assertTrue(exception.getMessage().contains("No se encontró inventario para el producto ID: 999"));
        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("⚠️ reducirCantidadPorCompra - Error al obtener info producto")
    void reducirCantidadPorCompra_ErrorInfoProducto() {
        // Given
        Long productoId = 10L;
        Integer cantidadComprada = 25;
        Inventario inventarioActualizado = new Inventario(1L, 10L, 75);

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioMock));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioActualizado);
        when(productoClient.obtenerProductoPorId(productoId)).thenThrow(new RuntimeException("Error de comunicación"));

        // When
        CompraResponse resultado = inventarioService.reducirCantidadPorCompra(productoId, cantidadComprada);

        // Then
        assertNotNull(resultado);
        assertEquals("Producto ID 10", resultado.getNombreProducto()); // Nombre por defecto
        assertEquals(25, resultado.getCantidadComprada());
        assertEquals(75, resultado.getCantidadRestante());

        verify(inventarioRepository, times(1)).save(any(Inventario.class));
        verify(productoClient, times(1)).obtenerProductoPorId(productoId);
    }

    // ====================== TESTS DE ELIMINACIÓN ======================

    @Test
    @DisplayName("✅ eliminarInventarioPorProductoId - Eliminación exitosa")
    void eliminarInventarioPorProductoId_Exitoso() {
        // Given
        Long productoId = 10L;
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioMock));

        // When
        boolean resultado = inventarioService.eliminarInventarioPorProductoId(productoId);

        // Then
        assertTrue(resultado);
        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(inventarioRepository, times(1)).delete(inventarioMock);
    }

    @Test
    @DisplayName("❌ eliminarInventarioPorProductoId - Inventario no encontrado")
    void eliminarInventarioPorProductoId_NoEncontrado() {
        // Given
        Long productoId = 999L;
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.empty());

        // When
        boolean resultado = inventarioService.eliminarInventarioPorProductoId(productoId);

        // Then
        assertFalse(resultado);
        verify(inventarioRepository, times(1)).findByProductoId(productoId);
        verify(inventarioRepository, never()).delete(any());
    }

    // ====================== TESTS DE MAPEO ======================

    @Test
    @DisplayName("✅ mapearInventarioCompleto - Mapping exitoso")
    void mapearInventarioCompleto_Exitoso() {
        // Given
        when(productoClient.obtenerProductoPorId(10L)).thenReturn(Optional.of(productoMock));

        // When - Usamos método de paginación que internamente usa mapearInventarioCompleto
        Pageable pageable = PageRequest.of(0, 10);
        List<Inventario> inventarios = Arrays.asList(inventarioMock);
        Page<Inventario> pageInventarios = new PageImpl<>(inventarios, pageable, 1);

        when(inventarioRepository.findAll(pageable)).thenReturn(pageInventarios);
        Page<InventarioConProductoDTO> resultado = inventarioService.obtenerTodosLosInventariosCompletos(pageable);

        // Then
        assertEquals(1, resultado.getContent().size());
        InventarioConProductoDTO dto = resultado.getContent().get(0);
        assertEquals("Producto Test", dto.getNombreProducto());
        assertEquals(new BigDecimal("50.00"), dto.getPrecioProducto());

        verify(productoClient, times(1)).obtenerProductoPorId(10L);
    }

    @Test
    @DisplayName("⚠️ mapearInventarioCompleto - Producto no encontrado")
    void mapearInventarioCompleto_ProductoNoEncontrado() {
        // Given
        when(productoClient.obtenerProductoPorId(10L)).thenReturn(Optional.empty());

        // When
        Pageable pageable = PageRequest.of(0, 10);
        List<Inventario> inventarios = Arrays.asList(inventarioMock);
        Page<Inventario> pageInventarios = new PageImpl<>(inventarios, pageable, 1);

        when(inventarioRepository.findAll(pageable)).thenReturn(pageInventarios);
        Page<InventarioConProductoDTO> resultado = inventarioService.obtenerTodosLosInventariosCompletos(pageable);

        // Then
        assertEquals(1, resultado.getContent().size());
        InventarioConProductoDTO dto = resultado.getContent().get(0);
        assertEquals("PRODUCTO NO ENCONTRADO", dto.getNombreProducto());
        assertNull(dto.getPrecioProducto());

        verify(productoClient, times(1)).obtenerProductoPorId(10L);
    }

    @Test
    @DisplayName("❌ mapearInventarioCompleto - Error de comunicación")
    void mapearInventarioCompleto_ErrorComunicacion() {
        // Given
        when(productoClient.obtenerProductoPorId(10L)).thenThrow(new RuntimeException("Error de red"));

        // When
        Pageable pageable = PageRequest.of(0, 10);
        List<Inventario> inventarios = Arrays.asList(inventarioMock);
        Page<Inventario> pageInventarios = new PageImpl<>(inventarios, pageable, 1);

        when(inventarioRepository.findAll(pageable)).thenReturn(pageInventarios);
        Page<InventarioConProductoDTO> resultado = inventarioService.obtenerTodosLosInventariosCompletos(pageable);

        // Then
        assertEquals(1, resultado.getContent().size());
        InventarioConProductoDTO dto = resultado.getContent().get(0);
        assertEquals("SERVICIO NO DISPONIBLE", dto.getNombreProducto());
        assertNull(dto.getPrecioProducto());

        verify(productoClient, times(1)).obtenerProductoPorId(10L);
    }
}
