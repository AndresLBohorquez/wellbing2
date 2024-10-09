package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.EstadoPagoMensual;

public interface EstadoPagoMensualService {

    public List<EstadoPagoMensual> getEstadosPagoMensual();

    public EstadoPagoMensual getEstadoPagoById(Long id);

    public EstadoPagoMensual addEstadoPago(EstadoPagoMensual estadoPagoMensual);

    public EstadoPagoMensual editEstadoPago(EstadoPagoMensual estadoPagoMensual);

    public void deleteEstadoPago(Long id);

    public EstadoPagoMensual getEstadoPagoMensualByNombre(String nombre);
}
