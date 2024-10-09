package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.PagoMensual;

public interface PagoMensualService {

    public List<PagoMensual> getPagosMensual();

    public PagoMensual getPagoMensualById(Long id);

    public PagoMensual addPagoMensual(PagoMensual pagoMensual);

    public PagoMensual editPagoMensual(PagoMensual pagoMensual);
}
