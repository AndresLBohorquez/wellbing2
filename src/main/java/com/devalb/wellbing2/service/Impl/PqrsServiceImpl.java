package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Pqrs;
import com.devalb.wellbing2.repository.PqrsRepository;
import com.devalb.wellbing2.service.PqrsService;

@Service
public class PqrsServiceImpl implements PqrsService {

    @Autowired
    private PqrsRepository pqrsRepository;

    @Override
    public List<Pqrs> getPqrs() {
        return pqrsRepository.findAll();
    }

    @Override
    public Pqrs getPqrsById(Long id) {
        return pqrsRepository.findById(id).orElse(null);
    }

    @Override
    public Pqrs addPqrs(Pqrs pqrs) {
        return pqrsRepository.save(pqrs);
    }

    @Override
    public Pqrs editPqrs(Pqrs pqrs) {
        return pqrsRepository.save(pqrs);
    }

    @Override
    public void deletePqrs(Long id) {
        pqrsRepository.deleteById(id);
    }

    @Override
    public List<Pqrs> getPqrsByEstado(Long id) {
        return pqrsRepository.findAllByEstado(id);
    }

    @Override
    public List<Pqrs> getPqrsByTipo(Long id) {
        return pqrsRepository.findAllByTipo(id);
    }

    @Override
    public List<Pqrs> getPqrsByUsuario(Long id) {
        return pqrsRepository.findAllByUsuarioId(id);
    }
}
