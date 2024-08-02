package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.EstadoUsuario;

public interface EstadoUsuarioService {

    public List<EstadoUsuario> getEstadosUsuario();

    public EstadoUsuario getEstadoUsuarioById(Long id);

    public EstadoUsuario addEstadoUsuario(EstadoUsuario estadoUsuario);

    public EstadoUsuario editEstadoUsuario(EstadoUsuario estadoUsuario);

    public void deleteEstadoUsuario(Long id);

    public EstadoUsuario getEstadoUsuarioByNombre(String nombre);
}
