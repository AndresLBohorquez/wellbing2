package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.WellPoints;

public interface WellPointsService {

    public List<WellPoints> getWellPoints();

    public WellPoints getWellPointsById(Long id);

    public WellPoints addWellPoints(WellPoints wellPoints);

    public WellPoints editWellPoints(WellPoints wellPoints);

    public void deleteWellPoints(Long id);

    public WellPoints getWellPointsByUsuario(Long id);
}
