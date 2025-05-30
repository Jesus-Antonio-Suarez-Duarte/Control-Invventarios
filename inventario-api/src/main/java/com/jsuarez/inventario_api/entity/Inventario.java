package com.jsuarez.inventario_api.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventario_seq")
    @SequenceGenerator(name = "inventario_seq", sequenceName = "inventario_seq", allocationSize = 1)
    private Long id;
    
    @NotNull(message = "El ID del producto es obligatorio")
    @Min(value = 1, message = "El ID del producto debe ser mayor a 0")
    @Column(name = "producto_id", nullable = false, unique = true)
    private Long productoId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}