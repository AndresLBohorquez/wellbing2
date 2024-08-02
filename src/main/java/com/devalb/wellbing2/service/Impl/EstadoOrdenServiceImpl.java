package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.EstadoOrden;
import com.devalb.wellbing2.repository.EstadoOrdenRepository;
import com.devalb.wellbing2.service.EstadoOrdenService;

@Service
public class EstadoOrdenServiceImpl implements EstadoOrdenService {
    @Autowired
    private EstadoOrdenRepository estadoOrdenRepository;

    @Override
    public List<EstadoOrden> getEstadosOrden() {
        return estadoOrdenRepository.findAll();
    }

    @Override
    public EstadoOrden getEstadoOrdenById(Long id) {
        return estadoOrdenRepository.findById(id).orElse(null);
    }

    @Override
    public EstadoOrden addEstadoOrden(EstadoOrden estadoOrden) {
        return estadoOrdenRepository.save(estadoOrden);
    }

    @Override
    public EstadoOrden editEstadoOrden(EstadoOrden estadoOrden) {
        return estadoOrdenRepository.save(estadoOrden);
    }

    @Override
    public void deleteEstadoOrden(Long id) {
        estadoOrdenRepository.deleteById(id);
    }

    @Override
    public EstadoOrden getEstadoOrdenByNombre(String nombre) {
        return estadoOrdenRepository.findByNombre(nombre);
    }
}
