package com.devalb.wellbing2.service;

import java.util.List;
import java.util.Map;

import com.devalb.wellbing2.entity.Pqrs;

public interface PqrsService {

    public List<Pqrs> getPqrs();

    public Pqrs getPqrsById(Long id);

    public Pqrs addPqrs(Pqrs pqrs);

    public Pqrs editPqrs(Pqrs pqrs);

    public void deletePqrs(Long id);

    public List<Pqrs> getPqrsByEstado(Long id);

    public List<Pqrs> getPqrsByTipo(Long id);

    public List<Pqrs> getPqrsByUsuario(Long id);

    public Map<String, Integer> getPqrsLast6Months();
}
