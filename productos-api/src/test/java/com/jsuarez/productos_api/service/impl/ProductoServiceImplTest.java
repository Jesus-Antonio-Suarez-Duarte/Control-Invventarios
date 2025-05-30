package com.jsuarez.productos_api.service.impl;


import com.jsuarez.productos_api.dto.*;
import com.jsuarez.productos_api.entity.*;
import com.jsuarez.productos_api.exception.*;
import com.jsuarez.productos_api.mapper.*;
import com.jsuarez.productos_api.repository.*;
import com.jsuarez.productos_api.service.*;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private ProductoRequestDto requestDto;
    private ProductoResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // Setup test data
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Cloro granulado 25kg");
        producto.setPrecio(new BigDecimal("45.50"));

        requestDto = new ProductoRequestDto();
        requestDto.setNombre("Cloro granulado 25kg");
        requestDto.setPrecio(new BigDecimal("45.50"));

        responseDto = new ProductoResponseDto();
        responseDto.setId("1");
        responseDto.setType("productos");
        ProductoResponseDto.ProductoAttributes attributes = new ProductoResponseDto.ProductoAttributes();
        attributes.setNombre("Cloro granulado 25kg");
        attributes.setPrecio(new BigDecimal("45.50"));
        responseDto.setAttributes(attributes);
    }

    @Test
    void testGetAllProductos() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Producto> productos = Arrays.asList(producto);
        Page<Producto> productosPage = new PageImpl<>(productos, pageable, 1);
        List<ProductoResponseDto> responseDtos = Arrays.asList(responseDto);
        Page<ProductoResponseDto> expectedPage = new PageImpl<>(responseDtos, pageable, 1);

        when(productoRepository.findAll(pageable)).thenReturn(productosPage);
        when(productoMapper.toResponseDto(producto)).thenReturn(responseDto);

        // When
        Page<ProductoResponseDto> result = productoService.getAllProductos(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(responseDto, result.getContent().get(0));
        verify(productoRepository).findAll(pageable);
        verify(productoMapper).toResponseDto(producto);
    }

    @Test
    void testGetProductoById_Success() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoMapper.toResponseDto(producto)).thenReturn(responseDto);

        // When
        ProductoResponseDto result = productoService.getProductoById(1L);

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(productoRepository).findById(1L);
        verify(productoMapper).toResponseDto(producto);
    }

    @Test
    void testGetProductoById_NotFound() {
        // Given
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ProductoNotFoundException exception = assertThrows(
            ProductoNotFoundException.class,
            () -> productoService.getProductoById(999L)
        );
        
        assertEquals("No se encontró el producto con ID: 999", exception.getMessage());
        verify(productoRepository).findById(999L);
        verify(productoMapper, never()).toResponseDto(any());
    }

    @Test
    void testCreateProducto_Success() {
        // Given
        when(productoMapper.toEntity(requestDto)).thenReturn(producto);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(productoMapper.toResponseDto(producto)).thenReturn(responseDto);

        // When
        ProductoResponseDto result = productoService.createProducto(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(productoMapper).toEntity(requestDto);
        verify(productoRepository).save(any(Producto.class));
        verify(productoMapper).toResponseDto(producto);
    }

    @Test
    void testUpdateProducto_Success() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);
        when(productoMapper.toResponseDto(producto)).thenReturn(responseDto);

        // When
        ProductoResponseDto result = productoService.updateProducto(1L, requestDto);

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(productoRepository).findById(1L);
        verify(productoMapper).updateEntity(producto, requestDto);
        verify(productoRepository).save(producto);
        verify(productoMapper).toResponseDto(producto);
    }

    @Test
    void testUpdateProducto_NotFound() {
        // Given
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ProductoNotFoundException exception = assertThrows(
            ProductoNotFoundException.class,
            () -> productoService.updateProducto(999L, requestDto)
        );
        
        assertEquals("No se puede actualizar. El producto con ID 999 no existe", exception.getMessage());
        verify(productoRepository).findById(999L);
        verify(productoMapper, never()).updateEntity(any(), any());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void testDeleteProducto_Success() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When
        assertDoesNotThrow(() -> productoService.deleteProducto(1L));

        // Then
        verify(productoRepository).findById(1L);
        verify(productoRepository).delete(producto);
    }

    @Test
    void testDeleteProducto_NotFound() {
        // Given
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ProductoNotFoundException exception = assertThrows(
            ProductoNotFoundException.class,
            () -> productoService.deleteProducto(999L)
        );
        
        assertEquals("No se puede eliminar. El producto con ID 999 no existe", exception.getMessage());
        verify(productoRepository).findById(999L);
        verify(productoRepository, never()).delete(any());
    }

    @Test
    void testGetAllProductos_EmptyPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Producto> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);

        when(productoRepository.findAll(pageable)).thenReturn(emptyPage);

        // When
        Page<ProductoResponseDto> result = productoService.getAllProductos(pageable);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(productoRepository).findAll(pageable);
        verify(productoMapper, never()).toResponseDto(any());
    }

    @Test
    void testServiceMockSetup() {
        // When & Then
        assertNotNull(productoService);
        assertNotNull(productoRepository);
        assertNotNull(productoMapper);
    }
}