package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.AccionWellPoints;
import com.devalb.wellbing2.repository.AccionWellPointsRepository;
import com.devalb.wellbing2.service.AccionWellPointsService;

@Service
public class AccionWellPointsServiceImpl implements AccionWellPointsService {
    @Autowired
    private AccionWellPointsRepository accionWellPointsRepository;

    @Override
    public List<AccionWellPoints> getAccionesWellPoints() {
        return accionWellPointsRepository.findAll();
    }

    @Override
    public AccionWellPoints getAccionWellPointsById(Long id) {
        return accionWellPointsRepository.findById(id).orElse(null);
    }

    @Override
    public AccionWellPoints addAccionWellPoints(AccionWellPoints accionWellPoints) {
        return accionWellPointsRepository.save(accionWellPoints);
    }

    @Override
    public AccionWellPoints editAccionWellPoints(AccionWellPoints accionWellPoints) {
        return accionWellPointsRepository.save(accionWellPoints);
    }

    @Override
    public void deleteAccionWellPoints(Long id) {
        accionWellPointsRepository.deleteById(id);
    }

    @Override
    public AccionWellPoints getAccionWellPointsByNombre(String nombre) {
        return accionWellPointsRepository.findByNombre(nombre);
    }

}
