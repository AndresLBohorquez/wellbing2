package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.Activacion;

public interface ActivacionService {

    public List<Activacion> getActivaciones();

    public Activacion getActivacionById(Long id);

    public Activacion addActivacion(Activacion activacion);

    public Activacion editActivacion(Activacion activacion);

    public void deleteActivacion(Long id);

    public List<Activacion> getActivacionesByIdUsuario(Long id);

    public Activacion getUltimActivacion(Long id);

    public List<Activacion> getActivacionesByEstadoActivacion(int id);
}
