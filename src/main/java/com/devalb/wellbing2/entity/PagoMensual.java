package com.devalb.wellbing2.entity;

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
public class PagoMensual {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Activacion activacion;

    private int cantidadHijos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private EstadoPagoMensual estadoPagoMensual;

    private String comprobante;

    private String mes;

    private double descuento;

    private double ganancia;

    private double total;
}
