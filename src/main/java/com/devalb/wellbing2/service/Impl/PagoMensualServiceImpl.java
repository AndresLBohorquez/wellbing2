package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.PagoMensual;
import com.devalb.wellbing2.repository.PagoMensualRepository;
import com.devalb.wellbing2.service.PagoMensualService;

@Service
public class PagoMensualServiceImpl implements PagoMensualService {

    @Autowired
    private PagoMensualRepository pagoMensualRepository;

    @Override
    public List<PagoMensual> getPagosMensual() {
        return pagoMensualRepository.findAll();
    }

    @Override
    public PagoMensual getPagoMensualById(Long id) {
        return pagoMensualRepository.findById(id).orElse(null);
    }

    @Override
    public PagoMensual addPagoMensual(PagoMensual pagoMensual) {
        return pagoMensualRepository.save(pagoMensual);
    }

    @Override
    public PagoMensual editPagoMensual(PagoMensual pagoMensual) {
        return pagoMensualRepository.save(pagoMensual);
    }

}
