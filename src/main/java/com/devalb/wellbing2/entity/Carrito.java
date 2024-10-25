package com.devalb.wellbing2.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;

    private int cantidad;

    private String comentario;
}
