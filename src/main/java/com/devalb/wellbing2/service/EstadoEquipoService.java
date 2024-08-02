package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.EstadoEquipo;

public interface EstadoEquipoService {

    public List<EstadoEquipo> getEstadosEquipo();

    public EstadoEquipo getEstadoEquipoById(Long id);

    public EstadoEquipo addEstadoEquipo(EstadoEquipo estadoEquipo);

    public EstadoEquipo editEstadoEquipo(EstadoEquipo estadoEquipo);

    public void deleteEstadoEquipo(Long id);

    public EstadoEquipo getByNombreEstado(String nombre);
}
