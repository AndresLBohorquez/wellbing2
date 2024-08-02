package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.MedioPago;
import com.devalb.wellbing2.repository.MedioPagoRepository;
import com.devalb.wellbing2.service.MedioPagoService;

@Service
public class MedioPagoServiceImpl implements MedioPagoService {

    @Autowired
    private MedioPagoRepository medioPagoRepository;

    @Override
    public List<MedioPago> getMediosPago() {
        return medioPagoRepository.findAll();
    }

    @Override
    public MedioPago getMedioPagoById(Long id) {
        return medioPagoRepository.findById(id).orElse(null);
    }

    @Override
    public MedioPago addMedioPago(MedioPago medioPago) {
        return medioPagoRepository.save(medioPago);
    }

    @Override
    public MedioPago editMedioPago(MedioPago medioPago) {
        return medioPagoRepository.save(medioPago);
    }

    @Override
    public void deleteMedioPago(Long id) {
        medioPagoRepository.deleteById(id);
    }

    @Override
    public List<MedioPago> getMedioPagoByIdUsuario(Long id) {
        return medioPagoRepository.findAllByIdUsuario(id);
    }

}
