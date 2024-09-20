package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Mensaje;
import com.devalb.wellbing2.repository.MensajeRepository;
import com.devalb.wellbing2.service.MensajeService;

@Service
public class MensajeServiceImpl implements MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Override
    public List<Mensaje> getMensajes() {
        return mensajeRepository.findAll();
    }

    @Override
    public Mensaje getMensajeById(Long id) {
        return mensajeRepository.findById(id).orElse(null);
    }

    @Override
    public Mensaje addMensaje(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

    @Override
    public Mensaje editMensaje(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

    @Override
    public void deleteMensaje(Long id) {
        mensajeRepository.deleteById(id);
    }

    @Override
    public List<Mensaje> getByIdPqrs(Long id) {
        return mensajeRepository.findByIdPqrs(id);
    }

}
