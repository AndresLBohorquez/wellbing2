package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.EstadoActivacion;
import com.devalb.wellbing2.repository.EstadoActivacionRepository;
import com.devalb.wellbing2.service.EstadoActivacionService;

@Service
public class EstadoActivacionImpl implements EstadoActivacionService {

    @Autowired
    private EstadoActivacionRepository estadoActivacionRepository;

    @Override
    public List<EstadoActivacion> getEstadosActivacion() {
        return estadoActivacionRepository.findAll();

    }

    @Override
    public EstadoActivacion getEstadoActivacionById(Long id) {
        return estadoActivacionRepository.findById(id).orElse(null);
    }

    @Override
    public EstadoActivacion addEstadoActivacion(EstadoActivacion estadoActivacion) {
        return estadoActivacionRepository.save(estadoActivacion);
    }

    @Override
    public EstadoActivacion editEstadoActivacion(EstadoActivacion estadoActivacion) {
        return estadoActivacionRepository.save(estadoActivacion);
    }

    @Override
    public void deleteEstadoActivacion(Long id) {
        estadoActivacionRepository.deleteById(id);
    }

}
