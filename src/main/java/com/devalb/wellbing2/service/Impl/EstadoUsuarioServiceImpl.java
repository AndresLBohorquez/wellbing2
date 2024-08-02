package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.EstadoUsuario;
import com.devalb.wellbing2.repository.EstadoUsuarioRepository;
import com.devalb.wellbing2.service.EstadoUsuarioService;

@Service
public class EstadoUsuarioServiceImpl implements EstadoUsuarioService {

    @Autowired
    private EstadoUsuarioRepository estadoUsuarioRepository;

    @Override
    public List<EstadoUsuario> getEstadosUsuario() {
        return estadoUsuarioRepository.findAll();
    }

    @Override
    public EstadoUsuario getEstadoUsuarioById(Long id) {
        return estadoUsuarioRepository.findById(id).orElse(null);
    }

    @Override
    public EstadoUsuario addEstadoUsuario(EstadoUsuario estadoUsuario) {
        return estadoUsuarioRepository.save(estadoUsuario);
    }

    @Override
    public EstadoUsuario editEstadoUsuario(EstadoUsuario estadoUsuario) {
        return estadoUsuarioRepository.save(estadoUsuario);
    }

    @Override
    public void deleteEstadoUsuario(Long id) {
        estadoUsuarioRepository.deleteById(id);
    }

    @Override
    public EstadoUsuario getEstadoUsuarioByNombre(String nombre) {
        return estadoUsuarioRepository.findByNombre(nombre);
    }

}
