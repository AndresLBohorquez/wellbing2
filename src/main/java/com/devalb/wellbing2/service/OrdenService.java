package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.Orden;

public interface OrdenService {

    public List<Orden> getOrdenes();

    public Orden getOrdenById(Long id);

    public Orden addOrden(Orden orden);

    public Orden editOrden(Orden orden);

    public void deleteOrden(Long id);

    public List<Orden> getOrdenesByEstado(Long id);

    public List<Orden> getOrdenesByUsuario(Long id);

    public List<Orden> getOrdenesVisibles();

    public List<Orden> getOrdenesVisiblesByUsuario(Long id);
}
