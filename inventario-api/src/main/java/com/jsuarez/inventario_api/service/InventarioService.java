package com.jsuarez.inventario_api.service;

import com.jsuarez.inventario_api.dto.*;
import com.jsuarez.inventario_api.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InventarioService {
    
    /**
     * Obtiene el inventario por ID del producto
     * @param productoId ID del producto
     * @return Optional con el inventario si existe
     */
    Optional<Inventario> obtenerInventarioPorProductoId(Long productoId);
    
    /**
     * Obtiene el inventario completo (con información del producto) por ID del producto
     * @param productoId ID del producto
     * @return Optional con el inventario completo si existe
     */
    Optional<InventarioConProductoDTO> obtenerInventarioCompletoPorProductoId(Long productoId);
    
    /**
     * Crea o actualiza el inventario de un producto
     * @param inventario Datos del inventario
     * @return Inventario guardado
     */
    Inventario guardarInventario(Inventario inventario);
    
    /**
     * Crea inventario con validación de que el producto existe
     * @param inventario Datos del inventario
     * @return Inventario completo creado
     * @throws RuntimeException si el producto no existe
     */
    InventarioConProductoDTO crearInventarioConValidacion(Inventario inventario);
    
    /**
     * Actualiza la cantidad de un producto en inventario
     * @param productoId ID del producto
     * @param nuevaCantidad Nueva cantidad
     * @return Inventario actualizado
     * @throws RuntimeException si el producto no existe en inventario
     */
    Inventario actualizarCantidad(Long productoId, Integer nuevaCantidad);
    
    /**
     * Reduce la cantidad de un producto tras una compra
     * @param productoId ID del producto
     * @param cantidadComprada Cantidad a reducir
     * @return Respuesta de compra procesada
     * @throws RuntimeException si no hay suficiente stock o el producto no existe
     */
    CompraResponse reducirCantidadPorCompra(Long productoId, Integer cantidadComprada);
    
    /**
     * Obtiene todos los inventarios
     * @return Lista de todos los inventarios
     */
    List<Inventario> obtenerTodosLosInventarios();
    
    /**
     * Obtiene todos los inventarios con información de productos (con paginación)
     * @param pageable Información de paginación
     * @return Página de inventarios completos
     */
    Page<InventarioConProductoDTO> obtenerTodosLosInventariosCompletos(Pageable pageable);
    
    /**
     * Elimina un inventario por ID del producto
     * @param productoId ID del producto
     * @return true si se eliminó, false si no existía
     */
    boolean eliminarInventarioPorProductoId(Long productoId);
}