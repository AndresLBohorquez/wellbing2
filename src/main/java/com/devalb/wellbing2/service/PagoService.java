package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.Pago;

public interface PagoService {

    public List<Pago> getPagos();

    public Pago getPagoById(Long id);

    public Pago addPago(Pago pago);

    public Pago editPago(Pago pago);

    public void deletePago(Long id);

    public List<Pago> getPagosByEstado(Long id);

    public List<Pago> getPagosByUsuario(Long id);

    public Pago getPagoByOrdenId(Long id);

    public boolean existsByOrdenId(Long id);
}
