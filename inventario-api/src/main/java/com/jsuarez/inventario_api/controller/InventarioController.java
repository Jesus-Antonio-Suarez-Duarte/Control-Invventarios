package com.jsuarez.inventario_api.controller;


import com.jsuarez.inventario_api.dto.*;
import com.jsuarez.inventario_api.entity.*;
import com.jsuarez.inventario_api.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/inventario")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventario", description = "API para gestión de inventarios")
public class InventarioController {
    
    private final InventarioService inventarioService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los inventarios", description = "Retorna una lista de todos los inventarios")
    public ResponseEntity<JsonApiResponse<Inventario>> obtenerTodosLosInventarios() {
        log.debug("GET /api/v1/inventario - Obteniendo todos los inventarios");
        
        try {
            List<Inventario> inventarios = inventarioService.obtenerTodosLosInventarios();
            JsonApiResponse<Inventario> response = new JsonApiResponse<>(inventarios, "Inventarios obtenidos exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ Error al obtener inventarios: {}", e.getMessage());
            JsonApiResponse<Inventario> errorResponse = new JsonApiResponse<>(List.of(), "Error al obtener inventarios");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/completo")
    @Operation(summary = "Obtener todos los inventarios con información de productos (paginado)", 
               description = "Retorna una lista paginada de inventarios enriquecidos con datos de productos desde el microservicio")
    public ResponseEntity<JsonApiResponse<InventarioConProductoDTO>> obtenerTodosLosInventariosCompletos(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.debug("GET /api/v1/inventario/completo - Obteniendo inventarios completos paginados");
        
        try {
            Page<InventarioConProductoDTO> inventariosPage = inventarioService.obtenerTodosLosInventariosCompletos(pageable);
            JsonApiResponse<InventarioConProductoDTO> response = new JsonApiResponse<>(
                inventariosPage, 
                "Inventarios completos obtenidos exitosamente"
            );
            
            log.info("✅ Se obtuvieron {} inventarios completos de {} totales", 
                    inventariosPage.getContent().size(), inventariosPage.getTotalElements());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("❌ Error al obtener inventarios completos: {}", e.getMessage());
            JsonApiResponse<InventarioConProductoDTO> errorResponse = new JsonApiResponse<>(
                List.of(), 
                "❌ Error al obtener inventarios completos - Verifique conectividad con microservicio de productos"
            );
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }
    }
    
    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener inventario por ID de producto", 
               description = "Retorna el inventario disponible para un producto específico")
    public ResponseEntity<JsonApiResponse<Inventario>> obtenerInventarioPorProductoId(
            @Parameter(description = "ID del producto") @PathVariable Long productoId) {
        log.debug("GET /api/v1/inventario/producto/{} - Obteniendo inventario por producto ID", productoId);
        
        try {
            Optional<Inventario> inventario = inventarioService.obtenerInventarioPorProductoId(productoId);
            
            if (inventario.isPresent()) {
                JsonApiResponse<Inventario> response = new JsonApiResponse<>(
                    inventario.get(), 
                    "Inventario encontrado exitosamente"
                );
                return ResponseEntity.ok(response);
            } else {
                JsonApiResponse<Inventario> response = new JsonApiResponse<>(
                    List.of(), 
                    "No se encontró inventario para el producto ID: " + productoId
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("❌ Error al obtener inventario: {}", e.getMessage());
            JsonApiResponse<Inventario> errorResponse = new JsonApiResponse<>(
                List.of(), 
                "Error al obtener inventario"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/producto/{productoId}/completo")
    @Operation(summary = "Obtener inventario completo por ID de producto", 
               description = "Retorna el inventario de un producto específico enriquecido con información del producto")
    public ResponseEntity<JsonApiResponse<InventarioConProductoDTO>> obtenerInventarioCompletoPorProductoId(
            @Parameter(description = "ID del producto") @PathVariable Long productoId) {
        log.debug("GET /api/v1/inventario/producto/{}/completo - Obteniendo inventario completo", productoId);
        
        try {
            Optional<InventarioConProductoDTO> inventario = 
                inventarioService.obtenerInventarioCompletoPorProductoId(productoId);
            
            if (inventario.isPresent()) {
                JsonApiResponse<InventarioConProductoDTO> response = new JsonApiResponse<>(
                    inventario.get(),
                    "✅ Inventario completo encontrado exitosamente"
                );
                log.info("✅ Inventario completo encontrado para producto ID: {}", productoId);
                return ResponseEntity.ok(response);
            } else {
                JsonApiResponse<InventarioConProductoDTO> response = new JsonApiResponse<>(
                    List.of(),
                    "❌ No se encontró inventario para el producto ID: " + productoId
                );
                log.warn("⚠️ No se encontró inventario para producto ID: {}", productoId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("❌ Error al obtener inventario completo para producto {}: {}", productoId, e.getMessage());
            JsonApiResponse<InventarioConProductoDTO> errorResponse = new JsonApiResponse<>(
                List.of(),
                "❌ Error al obtener inventario completo - Problema de comunicación con microservicios"
            );
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }
    }
    
    @PostMapping
    @Operation(summary = "Crear o actualizar inventario", 
               description = "Crea un nuevo inventario o actualiza uno existente")
    public ResponseEntity<JsonApiResponse<Inventario>> crearInventario(@Valid @RequestBody Inventario inventario) {
        log.debug("POST /api/v1/inventario - Creando inventario para producto ID: {}", inventario.getProductoId());
        
        try {
            Inventario inventarioGuardado = inventarioService.guardarInventario(inventario);
            JsonApiResponse<Inventario> response = new JsonApiResponse<>(
                inventarioGuardado,
                "✅ Inventario creado/actualizado exitosamente"
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("❌ Error al crear inventario: {}", e.getMessage());
            JsonApiResponse<Inventario> errorResponse = new JsonApiResponse<>(
                List.of(),
                "❌ Error al crear inventario: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    @PostMapping("/con-validacion")
    @Operation(summary = "Crear inventario con validación de producto", 
               description = "Crea un nuevo inventario después de validar que el producto existe en el microservicio")
    public ResponseEntity<JsonApiResponse<InventarioConProductoDTO>> crearInventarioConValidacion(
            @Valid @RequestBody Inventario inventario) {
        log.debug("POST /api/v1/inventario/con-validacion - Creando inventario con validación para producto ID: {}", 
                 inventario.getProductoId());
        
        try {
            InventarioConProductoDTO inventarioCreado = 
                inventarioService.crearInventarioConValidacion(inventario);
            
            JsonApiResponse<InventarioConProductoDTO> response = new JsonApiResponse<>(
                inventarioCreado,
                "✅ Inventario creado exitosamente con validación de producto"
            );
            
            log.info("✅ Inventario creado exitosamente para producto ID: {} - Stock: {}", 
                    inventarioCreado.getProductoId(), inventarioCreado.getCantidadEnStock());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            log.error("❌ Error de validación al crear inventario: {}", e.getMessage());
            String mensaje = e.getMessage().contains("conectar") ? 
                "❌ Error de conectividad con microservicio de productos" : 
                "❌ Error de validación: " + e.getMessage();
                
            JsonApiResponse<InventarioConProductoDTO> errorResponse = new JsonApiResponse<>(
                List.of(), mensaje
            );
            
            HttpStatus status = e.getMessage().contains("conectar") ? 
                HttpStatus.SERVICE_UNAVAILABLE : HttpStatus.BAD_REQUEST;
                
            return ResponseEntity.status(status).body(errorResponse);
            
        } catch (Exception e) {
            log.error("❌ Error interno al crear inventario: {}", e.getMessage());
            JsonApiResponse<InventarioConProductoDTO> errorResponse = new JsonApiResponse<>(
                List.of(),
                "❌ Error interno del servidor"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PutMapping("/producto/{productoId}/cantidad")
    @Operation(summary = "Actualizar cantidad de inventario", 
               description = "Actualiza la cantidad disponible de un producto específico")
    public ResponseEntity<JsonApiResponse<Inventario>> actualizarCantidad(
            @Parameter(description = "ID del producto") @PathVariable Long productoId,
            @Parameter(description = "Nueva cantidad") @RequestParam Integer cantidad) {
        log.debug("PUT /api/v1/inventario/producto/{}/cantidad - Actualizando cantidad a: {}", productoId, cantidad);
        
        try {
            Inventario inventarioActualizado = inventarioService.actualizarCantidad(productoId, cantidad);
            JsonApiResponse<Inventario> response = new JsonApiResponse<>(
                inventarioActualizado,
                "✅ Cantidad actualizada exitosamente"
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("❌ Error al actualizar cantidad: {}", e.getMessage());
            JsonApiResponse<Inventario> errorResponse = new JsonApiResponse<>(
                List.of(),
                "❌ " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            log.error("❌ Error interno al actualizar cantidad: {}", e.getMessage());
            JsonApiResponse<Inventario> errorResponse = new JsonApiResponse<>(
                List.of(),
                "❌ Error interno al actualizar cantidad"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PutMapping("/producto/{productoId}/compra")
    @Operation(summary = "Procesar compra", 
               description = "Reduce la cantidad disponible tras una compra")
    public ResponseEntity<JsonApiResponse<CompraResponse>> procesarCompra(
            @Parameter(description = "ID del producto") @PathVariable Long productoId,
            @Parameter(description = "Cantidad comprada") @RequestParam Integer cantidadComprada) {
        log.debug("PUT /api/v1/inventario/producto/{}/compra - Procesando compra de {} unidades", 
                 productoId, cantidadComprada);
        
        try {
            CompraResponse compraResponse = inventarioService.reducirCantidadPorCompra(productoId, cantidadComprada);
            JsonApiResponse<CompraResponse> response = new JsonApiResponse<>(
                compraResponse,
                compraResponse.getMensaje()
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("❌ Error al procesar compra: {}", e.getMessage());
            JsonApiResponse<CompraResponse> errorResponse = new JsonApiResponse<>(
                List.of(),
                "❌ " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            log.error("❌ Error interno al procesar compra: {}", e.getMessage());
            JsonApiResponse<CompraResponse> errorResponse = new JsonApiResponse<>(
                List.of(),
                "❌ Error interno al procesar compra"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @DeleteMapping("/producto/{productoId}")
    @Operation(summary = "Eliminar inventario", 
               description = "Elimina el inventario de un producto específico")
    public ResponseEntity<JsonApiResponse<Void>> eliminarInventario(
            @Parameter(description = "ID del producto") @PathVariable Long productoId) {
        log.debug("DELETE /api/v1/inventario/producto/{} - Eliminando inventario", productoId);
        
        try {
            boolean eliminado = inventarioService.eliminarInventarioPorProductoId(productoId);
            
            if (eliminado) {
                JsonApiResponse<Void> response = new JsonApiResponse<>(
                    List.of(),
                    "✅ Inventario eliminado exitosamente"
                );
                return ResponseEntity.ok(response);
            } else {
                JsonApiResponse<Void> response = new JsonApiResponse<>(
                    List.of(),
                    "❌ No se encontró inventario para eliminar"
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("❌ Error al eliminar inventario: {}", e.getMessage());
            JsonApiResponse<Void> response = new JsonApiResponse<>(
                List.of(),
                "❌ Error al eliminar inventario"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}