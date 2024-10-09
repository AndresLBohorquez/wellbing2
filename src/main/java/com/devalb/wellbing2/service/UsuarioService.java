package com.devalb.wellbing2.service;

import java.util.List;
import java.util.Map;

import com.devalb.wellbing2.entity.Usuario;

public interface UsuarioService {

    public List<Usuario> getUsuarios();

    public Usuario getUsuarioById(Long id);

    public Usuario addUsuario(Usuario usuario);

    public Usuario editUsuario(Usuario usuario);

    public void deleteUsuario(Long id);

    public Usuario getUsuarioByUsername(String username);

    public Usuario getUsuarioByEmail(String email);

    public Usuario getUsuarioByCodigoUsuario(String codigoUsuario);

    public List<Usuario> getUsuariosVisibles();

    public Map<String, Integer> getUsuariosLast6Months();

    public List<Usuario> getUsuariosUltimaActivacionOK();

    public List<Usuario> getUsuariosUltimaActivacionValidado();

}
