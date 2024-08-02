package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.RedesUsuario;
import com.devalb.wellbing2.repository.RedesUsuarioRepository;
import com.devalb.wellbing2.service.RedesUsuarioService;

@Service
public class RedesUsuarioServiceImpl implements RedesUsuarioService {

    @Autowired
    private RedesUsuarioRepository redesUsuarioRepository;

    @Override
    public List<RedesUsuario> getRedesUsuario() {
        return redesUsuarioRepository.findAll();
    }

    @Override
    public RedesUsuario getRedesUsuarioById(Long id) {
        return redesUsuarioRepository.findById(id).orElse(null);
    }

    @Override
    public RedesUsuario addRedesUsuario(RedesUsuario redesUsuario) {
        return redesUsuarioRepository.save(redesUsuario);
    }

    @Override
    public RedesUsuario editRedesUsuario(RedesUsuario redesUsuario) {
        return redesUsuarioRepository.save(redesUsuario);
    }

    @Override
    public void deleteRedesUsuario(Long id) {
        redesUsuarioRepository.deleteById(id);
    }

    @Override
    public List<RedesUsuario> getRedesUsuarioByUsuario(Long id) {
        return redesUsuarioRepository.findAllByUsuario(id);
    }

}
