package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.Rol;

public interface RolService {

    public List<Rol> getRoles();

    public Rol getRolById(Long id);

    public Rol addRol(Rol rol);

    public Rol editRol(Rol rol);

    public void deleteRol(Long id);

    public Rol getRolByNombre(String nombre);

    public List<Rol> getRolesByIds(List<Long> ids);
}
