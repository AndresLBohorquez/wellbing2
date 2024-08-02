package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.EstadoEquipo;
import com.devalb.wellbing2.repository.EstadoEquipoRepositoy;
import com.devalb.wellbing2.service.EstadoEquipoService;

@Service
public class EstadoEquipoServiceImpl implements EstadoEquipoService {

    @Autowired
    private EstadoEquipoRepositoy estadoEquipoRepositoy;

    @Override
    public List<EstadoEquipo> getEstadosEquipo() {
        return estadoEquipoRepositoy.findAll();
    }

    @Override
    public EstadoEquipo getEstadoEquipoById(Long id) {
        return estadoEquipoRepositoy.findById(id).orElse(null);
    }

    @Override
    public EstadoEquipo addEstadoEquipo(EstadoEquipo estadoEquipo) {
        return estadoEquipoRepositoy.save(estadoEquipo);
    }

    @Override
    public EstadoEquipo editEstadoEquipo(EstadoEquipo estadoEquipo) {
        return estadoEquipoRepositoy.save(estadoEquipo);
    }

    @Override
    public void deleteEstadoEquipo(Long id) {
        estadoEquipoRepositoy.deleteById(id);
    }

    @Override
    public EstadoEquipo getByNombreEstado(String nombre) {
        return estadoEquipoRepositoy.findByNombre(nombre);
    }

}
