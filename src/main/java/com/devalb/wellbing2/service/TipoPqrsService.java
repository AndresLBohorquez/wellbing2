package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.TipoPqrs;

public interface TipoPqrsService {

    public List<TipoPqrs> getTipoPqrs();

    public TipoPqrs getTipoPqrsById(Long id);

    public TipoPqrs addTipoPqrs(TipoPqrs tipoPqrs);

    public TipoPqrs editTipoPqrs(TipoPqrs tipoPqrs);

    public void deleteTipoPqrs(Long id);

    public TipoPqrs getTipoPqrsByNombre(String nombre);
}
