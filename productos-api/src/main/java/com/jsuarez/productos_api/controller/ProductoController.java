package com.jsuarez.productos_api.controller;

import com.jsuarez.productos_api.dto.*;
import com.jsuarez.productos_api.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para gestión de productos de tratamiento de agua")
public class ProductoController {
    
    private final ProductoService productoService;
    
    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    
    @Operation(summary = "Listar todos los productos con paginación")
    @GetMapping
    public ResponseEntity<JsonApiResponse<List<ProductoResponseDto>>> getAllProductos(
            @Parameter(description = "Número de página", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Productos por página", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoResponseDto> productosPage = productoService.getAllProductos(pageable);
        
        Meta meta = Meta.of(
            productosPage.getNumber(),
            productosPage.getSize(),
            productosPage.getTotalElements(),
            productosPage.getTotalPages(),
            productosPage.isFirst(),
            productosPage.isLast()
        );
        
        return ResponseEntity.ok(JsonApiResponse.collection(productosPage.getContent(), meta));
    }
    
    @Operation(summary = "Obtener producto por ID")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<JsonApiResponse<ProductoResponseDto>> getProductoById(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) {
        ProductoResponseDto producto = productoService.getProductoById(id);
        return ResponseEntity.ok(JsonApiResponse.single(producto));
    }
    
    @Operation(summary = "Crear nuevo producto")
    @PostMapping
    public ResponseEntity<JsonApiResponse<ProductoResponseDto>> createProducto(@RequestBody ProductoRequestDto requestDto) {
        ProductoResponseDto createdProducto = productoService.createProducto(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JsonApiResponse.single(createdProducto, "Producto creado exitosamente"));
    }
    
    @Operation(summary = "Actualizar producto existente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<JsonApiResponse<ProductoResponseDto>> updateProducto(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id, 
            @RequestBody ProductoRequestDto requestDto) {
        ProductoResponseDto updatedProducto = productoService.updateProducto(id, requestDto);
        return ResponseEntity.ok(JsonApiResponse.single(updatedProducto, "Producto actualizado exitosamente"));
    }
    
    @Operation(summary = "Eliminar producto")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonApiResponse<Object>> deleteProducto(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.ok(JsonApiResponse.message("Producto eliminado exitosamente"));
    }
}