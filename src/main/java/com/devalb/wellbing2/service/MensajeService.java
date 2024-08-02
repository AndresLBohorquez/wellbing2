package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.Mensaje;

public interface MensajeService {

    public List<Mensaje> getMensajes();

    public Mensaje getMensajeById(Long id);

    public Mensaje addMensaje(Mensaje mensaje);

    public Mensaje editMensaje(Mensaje mensaje);

    public void deleteMensaje(Long id);
}
