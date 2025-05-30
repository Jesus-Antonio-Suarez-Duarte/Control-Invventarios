package com.jsuarez.productos_api.repository;

import com.jsuarez.productos_api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Heredamos findAll() automáticamente
}