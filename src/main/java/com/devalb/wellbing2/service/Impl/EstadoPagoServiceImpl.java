package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.EstadoPago;
import com.devalb.wellbing2.repository.EstadoPagoRepository;
import com.devalb.wellbing2.service.EstadoPagoService;

@Service
public class EstadoPagoServiceImpl implements EstadoPagoService {

    @Autowired
    private EstadoPagoRepository estadoPagoRepository;

    @Override
    public List<EstadoPago> getEstadosPago() {
        return estadoPagoRepository.findAll();
    }

    @Override
    public EstadoPago getEstadoPagoById(Long id) {
        return estadoPagoRepository.findById(id).orElse(null);
    }

    @Override
    public EstadoPago addEstadoPago(EstadoPago estadoPago) {
        return estadoPagoRepository.save(estadoPago);
    }

    @Override
    public EstadoPago editEstadoPago(EstadoPago estadoPago) {
        return estadoPagoRepository.save(estadoPago);
    }

    @Override
    public void deleteEstadoPago(Long id) {
        estadoPagoRepository.deleteById(id);
    }

    @Override
    public EstadoPago getEstadoPagoByNombre(String nombre) {
        return estadoPagoRepository.findByNombre(nombre);
    }

}
