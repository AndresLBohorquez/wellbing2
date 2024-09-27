package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Pago;
import com.devalb.wellbing2.repository.PagoRepository;
import com.devalb.wellbing2.service.PagoService;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public List<Pago> getPagos() {
        return pagoRepository.findAll();
    }

    @Override
    public Pago getPagoById(Long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    @Override
    public Pago addPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    @Override
    public Pago editPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    @Override
    public void deletePago(Long id) {
        pagoRepository.deleteById(id);
    }

    @Override
    public List<Pago> getPagosByEstado(Long id) {
        return pagoRepository.findAllByEstado(id);
    }

    @Override
    public List<Pago> getPagosByUsuario(Long id) {
        return pagoRepository.findAllByUsuarioId(id);
    }

    @Override
    public Pago getPagoByOrdenId(Long id) {
        return pagoRepository.findByOrdenId(id);
    }

    @Override
    public boolean existsByOrdenId(Long id) {
        return pagoRepository.existsByOrdenId(id);
    }

}
