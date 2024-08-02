package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.AccionWellPoints;

public interface AccionWellPointsService {

    public List<AccionWellPoints> getAccionesWellPoints();

    public AccionWellPoints getAccionWellPointsById(Long id);

    public AccionWellPoints addAccionWellPoints(AccionWellPoints accionWellPoints);

    public AccionWellPoints editAccionWellPoints(AccionWellPoints accionWellPoints);

    public void deleteAccionWellPoints(Long id);

    public AccionWellPoints getAccionWellPointsByNombre(String nombre);
}
