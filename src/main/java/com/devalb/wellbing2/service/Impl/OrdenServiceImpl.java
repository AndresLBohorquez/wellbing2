package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Orden;
import com.devalb.wellbing2.repository.OrdenRepository;
import com.devalb.wellbing2.service.OrdenService;

@Service
public class OrdenServiceImpl implements OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Override
    public List<Orden> getOrdenes() {
        return ordenRepository.findAll();
    }

    @Override
    public Orden getOrdenById(Long id) {
        return ordenRepository.findById(id).orElse(null);
    }

    @Override
    public Orden addOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    @Override
    public Orden editOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    @Override
    public void deleteOrden(Long id) {
        Orden orden = ordenRepository.findById(id).orElse(null);
        if (orden != null) {
            orden.setVisible(false);
            ordenRepository.save(orden);
        }
    }

    @Override
    public List<Orden> getOrdenesByEstado(Long id) {
        return ordenRepository.findAllByEstado(id);
    }

    @Override
    public List<Orden> getOrdenesByUsuario(Long id) {
        return ordenRepository.findAllByUsuarioId(id);
    }

    @Override
    public List<Orden> getOrdenesVisibles() {
        return ordenRepository.findAllVisible();
    }

}
