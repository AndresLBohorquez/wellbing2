package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Rol;
import com.devalb.wellbing2.repository.RolRepository;
import com.devalb.wellbing2.service.RolService;

@Service
public class RolServiceImpl implements RolService{

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<Rol> getRoles() {
        return rolRepository.findAll();
    }

    @Override
    public Rol getRolById(Long id) {
        return rolRepository.findById(id).orElse(null);
    }

    @Override
    public Rol addRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public Rol editRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void deleteRol(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    public Rol getRolByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

}
