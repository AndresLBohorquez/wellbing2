package com.devalb.wellbing2.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Pqrs {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaRegistro;

    private LocalDateTime fechaActualizacion;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String contenido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private EstadoPqrs estadoPqrs;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private TipoPqrs tipoPqrs;

}
