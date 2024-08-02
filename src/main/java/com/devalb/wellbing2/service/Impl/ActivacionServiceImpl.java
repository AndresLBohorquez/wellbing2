package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Activacion;
import com.devalb.wellbing2.repository.ActivacionRepository;
import com.devalb.wellbing2.service.ActivacionService;

@Service
public class ActivacionServiceImpl implements ActivacionService {

    @Autowired
    private ActivacionRepository activacionRepository;

    @Override
    public List<Activacion> getActivaciones() {
        return activacionRepository.findAll();
    }

    @Override
    public Activacion getActivacionById(Long id) {
        return activacionRepository.findById(id).orElse(null);
    }

    @Override
    public Activacion addActivacion(Activacion activacion) {
        return activacionRepository.save(activacion);
    }

    @Override
    public Activacion editActivacion(Activacion activacion) {
        return activacionRepository.save(activacion);
    }

    @Override
    public void deleteActivacion(Long id) {
        activacionRepository.deleteById(id);
    }

    @Override
    public List<Activacion> getActivacionesByIdUsuario(Long id) {
        return activacionRepository.findAllByIdUsuario(id);
    }

    @Override
    public Activacion getUltimActivacion(Long id) {
        return activacionRepository.findLastActivationByIdUsuario(id);
    }

    @Override
    public List<Activacion> getActivacionesByEstadoActivacion(int id) {
        return activacionRepository.findAllByEstadoActivacion(id);
    }

}
