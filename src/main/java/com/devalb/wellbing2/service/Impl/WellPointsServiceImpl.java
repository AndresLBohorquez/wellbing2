package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.WellPoints;
import com.devalb.wellbing2.repository.WellPointsRepository;
import com.devalb.wellbing2.service.WellPointsService;

@Service
public class WellPointsServiceImpl implements WellPointsService{

    @Autowired
    private WellPointsRepository wellPointsRepository;

    @Override
    public List<WellPoints> getWellPoints() {
        return wellPointsRepository.findAll();
    }

    @Override
    public WellPoints getWellPointsById(Long id) {
        return wellPointsRepository.findById(id).orElse(null);
    }

    @Override
    public WellPoints addWellPoints(WellPoints wellPoints) {
        return wellPointsRepository.save(wellPoints);
    }

    @Override
    public WellPoints editWellPoints(WellPoints wellPoints) {
        return wellPointsRepository.save(wellPoints);
    }

    @Override
    public void deleteWellPoints(Long id) {
        wellPointsRepository.deleteById(id);
    }

    @Override
    public List<WellPoints> getWellPointsByUsuario(Long id) {
        return wellPointsRepository.findByUsuario(id);
    }

}
