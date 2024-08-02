package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.TipoPqrs;
import com.devalb.wellbing2.repository.TipoPqrsRepository;
import com.devalb.wellbing2.service.TipoPqrsService;

@Service
public class TipoPqrsServiceImpl implements TipoPqrsService {

    @Autowired
    private TipoPqrsRepository tipoPqrsRepository;

    @Override
    public List<TipoPqrs> getTipoPqrs() {
        return tipoPqrsRepository.findAll();
    }

    @Override
    public TipoPqrs getTipoPqrsById(Long id) {
        return tipoPqrsRepository.findById(id).orElse(null);
    }

    @Override
    public TipoPqrs addTipoPqrs(TipoPqrs tipoPqrs) {
        return tipoPqrsRepository.save(tipoPqrs);
    }

    @Override
    public TipoPqrs editTipoPqrs(TipoPqrs tipoPqrs) {
        return tipoPqrsRepository.save(tipoPqrs);
    }

    @Override
    public void deleteTipoPqrs(Long id) {
        tipoPqrsRepository.deleteById(id);
    }

    @Override
    public TipoPqrs getTipoPqrsByNombre(String nombre) {
        return tipoPqrsRepository.findByNombre(nombre);
    }

}
