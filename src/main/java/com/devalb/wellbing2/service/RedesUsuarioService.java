package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.RedesUsuario;

public interface RedesUsuarioService {

    public List<RedesUsuario> getRedesUsuario();

    public RedesUsuario getRedesUsuarioById(Long id);

    public RedesUsuario addRedesUsuario(RedesUsuario redesUsuario);

    public RedesUsuario editRedesUsuario(RedesUsuario redesUsuario);

    public void deleteRedesUsuario(Long id);

    public List<RedesUsuario> getRedesUsuarioByUsuario(Long id);
}
