package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.repository.UsuarioRepository;
import com.devalb.wellbing2.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario addUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario editUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setVisible(false);
            usuarioRepository.save(usuario);
        }
    }

    @Override
    public Usuario getUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Usuario getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario getUsuarioByCodigoUsuario(String codigoUsuario) {
        return usuarioRepository.findByCodigoUsuario(codigoUsuario);
    }

    @Override
    public List<Usuario> getUsuariosVisibles() {
        return usuarioRepository.findAllVisible();
    }

}
