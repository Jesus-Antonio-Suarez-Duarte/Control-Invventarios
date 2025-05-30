package com.jsuarez.inventario_api.service.impl;


import com.jsuarez.inventario_api.client.*;
import com.jsuarez.inventario_api.dto.*;
import com.jsuarez.inventario_api.entity.*;
import com.jsuarez.inventario_api.repository.*;
import com.jsuarez.inventario_api.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventarioServiceImpl implements InventarioService {
    
    private final InventarioRepository inventarioRepository;
    private final ProductoClient productoClient;
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Inventario> obtenerInventarioPorProductoId(Long productoId) {
        log.debug("Buscando inventario para producto ID: {}", productoId);
        return inventarioRepository.findByProductoId(productoId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<InventarioConProductoDTO> obtenerInventarioCompletoPorProductoId(Long productoId) {
        log.debug("Obteniendo inventario completo para producto ID: {}", productoId);
        
        // 1. Obtener inventario local
        Optional<Inventario> inventarioOpt = obtenerInventarioPorProductoId(productoId);
        
        if (inventarioOpt.isEmpty()) {
            log.warn("No se encontró inventario para producto ID: {}", productoId);
            return Optional.empty();
        }
        
        Inventario inventario = inventarioOpt.get();
        
        // 2. Obtener información del producto desde microservicio
        try {
            Optional<ProductoSimple> productoOpt = productoClient.obtenerProductoPorId(productoId);
            
            if (productoOpt.isEmpty()) {
                log.warn("No se encontró producto ID: {} en microservicio de productos", productoId);
                System.out.println("⚠️ Producto ID " + productoId + " no encontrado en microservicio de productos");
                // Retornar inventario sin información del producto
                return Optional.of(new InventarioConProductoDTO(
                    inventario.getId(),
                    inventario.getProductoId(),
                    inventario.getCantidad(),
                    "PRODUCTO NO ENCONTRADO",
                    null
                ));
            }
            
            ProductoSimple producto = productoOpt.get();
            
            // 3. Combinar información
            InventarioConProductoDTO resultado = new InventarioConProductoDTO(
                inventario.getId(),
                inventario.getProductoId(),
                inventario.getCantidad(),
                producto.getNombre(),
                producto.getPrecio()
            );
            
            log.info("✅ Inventario completo obtenido - Producto: {}, Stock: {}, Valor total: {}", 
                    producto.getNombre(), inventario.getCantidad(), resultado.getValorTotalInventario());
            
            return Optional.of(resultado);
            
        } catch (RuntimeException e) {
            log.error("❌ Error al comunicarse con microservicio de productos: {}", e.getMessage());
            System.out.println("❌ Error de comunicación con microservicio de productos");
            
            // Retornar inventario básico sin información del producto
            return Optional.of(new InventarioConProductoDTO(
                inventario.getId(),
                inventario.getProductoId(),
                inventario.getCantidad(),
                "SERVICIO NO DISPONIBLE",
                null
            ));
        }
    }
    
    @Override
    public Inventario guardarInventario(Inventario inventario) {
        log.debug("Guardando inventario para producto ID: {}, cantidad: {}", 
                 inventario.getProductoId(), inventario.getCantidad());
        
        // Verificar si ya existe inventario para este producto
        Optional<Inventario> inventarioExistente = inventarioRepository.findByProductoId(inventario.getProductoId());
        
        if (inventarioExistente.isPresent()) {
            // Si existe, actualizar la cantidad
            Inventario existing = inventarioExistente.get();
            existing.setCantidad(inventario.getCantidad());
            Inventario saved = inventarioRepository.save(existing);
            log.info("✅ Inventario actualizado para producto ID: {} con cantidad: {}", 
                    saved.getProductoId(), saved.getCantidad());
            return saved;
        } else {
            // Si no existe, crear nuevo
            Inventario saved = inventarioRepository.save(inventario);
            log.info("✅ Nuevo inventario creado para producto ID: {} con cantidad: {}", 
                    saved.getProductoId(), saved.getCantidad());
            return saved;
        }
    }
    
    @Override
    public InventarioConProductoDTO crearInventarioConValidacion(Inventario inventario) {
        log.debug("Creando inventario con validación para producto ID: {}", inventario.getProductoId());
        
        try {
            // 1. Validar que el producto existe
            if (!productoClient.existeProducto(inventario.getProductoId())) {
                throw new RuntimeException("No se puede crear inventario: el producto ID " + 
                                         inventario.getProductoId() + " no existe");
            }
            
            // 2. Crear inventario
            Inventario inventarioCreado = guardarInventario(inventario);
            
            // 3. Retornar inventario completo
            Optional<InventarioConProductoDTO> resultado = obtenerInventarioCompletoPorProductoId(inventarioCreado.getProductoId());
            
            return resultado.orElseThrow(() -> 
                new RuntimeException("Error al obtener inventario completo después de crear"));
                
        } catch (RuntimeException e) {
            if (e.getMessage().contains("conectar")) {
                System.out.println("❌ No se puede validar producto - Microservicio de productos no disponible");
            }
            throw e;
        }
    }
    
    @Override
    public Inventario actualizarCantidad(Long productoId, Integer nuevaCantidad) {
        log.debug("Actualizando cantidad para producto ID: {} a cantidad: {}", productoId, nuevaCantidad);
        
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("No se encontró inventario para el producto ID: " + productoId));
        
        inventario.setCantidad(nuevaCantidad);
        Inventario saved = inventarioRepository.save(inventario);
        
        log.info("✅ Cantidad actualizada para producto ID: {} - Nueva cantidad: {}", productoId, nuevaCantidad);
        // Evento simple de cambio de inventario
        System.out.println("📦 EVENTO: Inventario actualizado - Producto ID: " + productoId + ", Nueva cantidad: " + nuevaCantidad);
        
        return saved;
    }
    
    @Override
    public CompraResponse reducirCantidadPorCompra(Long productoId, Integer cantidadComprada) {
        log.debug("Reduciendo cantidad por compra - Producto ID: {}, Cantidad: {}", productoId, cantidadComprada);
        
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("No se encontró inventario para el producto ID: " + productoId));
        
        if (inventario.getCantidad() < cantidadComprada) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + inventario.getCantidad() + 
                                     ", Solicitado: " + cantidadComprada);
        }
        
        int cantidadRestante = inventario.getCantidad() - cantidadComprada;
        inventario.setCantidad(cantidadRestante);
        Inventario saved = inventarioRepository.save(inventario);
        
        // Obtener información del producto para la respuesta
        String nombreProducto = "Producto ID " + productoId;
        try {
            Optional<ProductoSimple> producto = productoClient.obtenerProductoPorId(productoId);
            if (producto.isPresent()) {
                nombreProducto = producto.get().getNombre();
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener nombre del producto para la respuesta de compra");
            System.out.println("⚠️ No se pudo obtener info del producto para respuesta de compra");
        }
        
        log.info("🛒 Compra procesada - Producto ID: {}, Cantidad comprada: {}, Stock restante: {}", 
                productoId, cantidadComprada, cantidadRestante);
        
        // Evento simple de cambio de inventario
        System.out.println("🛒 EVENTO: Compra procesada - Producto: " + nombreProducto + 
                          ", Cantidad comprada: " + cantidadComprada + 
                          ", Stock restante: " + cantidadRestante);
        
        return new CompraResponse(
            saved.getId(),
            productoId,
            nombreProducto,
            cantidadComprada,
            cantidadRestante
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventario> obtenerTodosLosInventarios() {
        log.debug("Obteniendo todos los inventarios");
        return inventarioRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<InventarioConProductoDTO> obtenerTodosLosInventariosCompletos(Pageable pageable) {
        log.debug("Obteniendo inventarios completos con paginación - Página: {}, Tamaño: {}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        
        // Obtener inventarios paginados
        Page<Inventario> inventariosPage = inventarioRepository.findAll(pageable);
        
        // Mapear a DTOs completos
        Page<InventarioConProductoDTO> resultado = inventariosPage.map(this::mapearInventarioCompleto);
        
        log.info("✅ Se obtuvieron {} inventarios completos de {} totales", 
                resultado.getContent().size(), resultado.getTotalElements());
        
        return resultado;
    }
    
    @Override
    public boolean eliminarInventarioPorProductoId(Long productoId) {
        log.debug("Eliminando inventario para producto ID: {}", productoId);
        
        Optional<Inventario> inventario = inventarioRepository.findByProductoId(productoId);
        if (inventario.isPresent()) {
            inventarioRepository.delete(inventario.get());
            log.info("✅ Inventario eliminado para producto ID: {}", productoId);
            System.out.println("🗑️ EVENTO: Inventario eliminado - Producto ID: " + productoId);
            return true;
        }
        
        log.warn("⚠️ No se encontró inventario para eliminar - Producto ID: {}", productoId);
        return false;
    }
    
    /**
     * Mapea un inventario a InventarioConProductoDTO obteniendo información del producto
     */
    private InventarioConProductoDTO mapearInventarioCompleto(Inventario inventario) {
        try {
            Optional<ProductoSimple> producto = productoClient.obtenerProductoPorId(inventario.getProductoId());
            
            if (producto.isPresent()) {
                return new InventarioConProductoDTO(
                    inventario.getId(),
                    inventario.getProductoId(),
                    inventario.getCantidad(),
                    producto.get().getNombre(),
                    producto.get().getPrecio()
                );
            } else {
                log.warn("Producto ID {} no encontrado al mapear inventario", inventario.getProductoId());
                return new InventarioConProductoDTO(
                    inventario.getId(),
                    inventario.getProductoId(),
                    inventario.getCantidad(),
                    "PRODUCTO NO ENCONTRADO",
                    null
                );
            }
        } catch (Exception e) {
            log.warn("❌ Error al obtener producto ID {} al mapear inventario: {}", 
                    inventario.getProductoId(), e.getMessage());
            return new InventarioConProductoDTO(
                inventario.getId(),
                inventario.getProductoId(),
                inventario.getCantidad(),
                "SERVICIO NO DISPONIBLE",
                null
            );
        }
    }
}