package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.EstadoPagoMensual;
import com.devalb.wellbing2.repository.EstadoPagoMensualRepository;
import com.devalb.wellbing2.service.EstadoPagoMensualService;

@Service
public class EstadoPagoMensualServiceImpl implements EstadoPagoMensualService {

    @Autowired
    private EstadoPagoMensualRepository estadoPagoMensualRepository;

    @Override
    public List<EstadoPagoMensual> getEstadosPagoMensual() {
        return estadoPagoMensualRepository.findAll();
    }

    @Override
    public EstadoPagoMensual getEstadoPagoById(Long id) {
        return estadoPagoMensualRepository.findById(id).orElse(null);
    }

    @Override
    public EstadoPagoMensual addEstadoPago(EstadoPagoMensual estadoPagoMensual) {
        return estadoPagoMensualRepository.save(estadoPagoMensual);
    }

    @Override
    public EstadoPagoMensual editEstadoPago(EstadoPagoMensual estadoPagoMensual) {
        return estadoPagoMensualRepository.save(estadoPagoMensual);
    }

    @Override
    public void deleteEstadoPago(Long id) {
        estadoPagoMensualRepository.deleteById(id);
    }

    @Override
    public EstadoPagoMensual getEstadoPagoMensualByNombre(String nombre) {
        return estadoPagoMensualRepository.findByNombre(nombre);
    }

}
