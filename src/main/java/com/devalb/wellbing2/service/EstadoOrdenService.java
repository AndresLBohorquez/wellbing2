package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.EstadoOrden;

public interface EstadoOrdenService {

    public List<EstadoOrden> getEstadosOrden();

    public EstadoOrden getEstadoOrdenById(Long id);

    public EstadoOrden addEstadoOrden(EstadoOrden estadoOrden);

    public EstadoOrden editEstadoOrden(EstadoOrden estadoOrden);

    public void deleteEstadoOrden(Long id);

    public EstadoOrden getEstadoOrdenByNombre(String nombre);
}
