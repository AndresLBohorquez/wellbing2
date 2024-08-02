package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.EstadoPago;

public interface EstadoPagoService {
 
    public List<EstadoPago> getEstadosPago();

    public EstadoPago getEstadoPagoById(Long id);

    public EstadoPago addEstadoPago(EstadoPago estadoPago);

    public EstadoPago editEstadoPago(EstadoPago estadoPago);

    public void deleteEstadoPago(Long id);

    public EstadoPago getEstadoPagoByNombre(String nombre);
}
