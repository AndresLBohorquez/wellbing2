package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.Redes;

public interface RedesService {
    
    public List<Redes> getRedes();

    public Redes getRedesById(Long id);

    public Redes addRedes(Redes redes);

    public Redes editRedes(Redes redes);

    public void deleteRedes(Long id);

    public Redes getRedesByNombre(String nombre);
}
