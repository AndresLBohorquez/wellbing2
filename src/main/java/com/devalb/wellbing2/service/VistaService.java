package com.devalb.wellbing2.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import com.devalb.wellbing2.entity.Rol;

public interface VistaService {

    public void cargarVistas(Model model, Authentication auth);

    public void cargarVistasAdmin(Model model, Authentication auth);

    public void verTopProductos(Model model);

    public String obtenerRolPrincipal(List<Rol> roles);
}
