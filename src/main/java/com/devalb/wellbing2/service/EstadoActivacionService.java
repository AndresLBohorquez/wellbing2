package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.EstadoActivacion;

public interface EstadoActivacionService {

    public List<EstadoActivacion> getEstadosActivacion();

    public EstadoActivacion getEstadoActivacionById(Long id);

    public EstadoActivacion addEstadoActivacion(EstadoActivacion estadoActivacion);

    public EstadoActivacion editEstadoActivacion(EstadoActivacion estadoActivacion);

    public void deleteEstadoActivacion(Long id);
}
