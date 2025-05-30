package com.jsuarez.inventario_api.repository;


import com.jsuarez.inventario_api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    
    /**
     * Busca el inventario por el ID del producto
     * @param productoId ID del producto
     * @return Optional con el inventario si existe
     */
    Optional<Inventario> findByProductoId(Long productoId);
    
    /**
     * Verifica si existe inventario para un producto específico
     * @param productoId ID del producto
     * @return true si existe inventario para el producto
     */
    boolean existsByProductoId(Long productoId);
}