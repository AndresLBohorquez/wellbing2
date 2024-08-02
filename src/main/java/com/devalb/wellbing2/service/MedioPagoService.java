package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.MedioPago;

public interface MedioPagoService {

    public List<MedioPago> getMediosPago();

    public MedioPago getMedioPagoById(Long id);

    public MedioPago addMedioPago(MedioPago medioPago);

    public MedioPago editMedioPago(MedioPago medioPago);

    public void deleteMedioPago(Long id);

    public List<MedioPago> getMedioPagoByIdUsuario(Long id);
}
