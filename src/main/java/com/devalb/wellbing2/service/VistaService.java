package com.devalb.wellbing2.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public interface VistaService {

    public void cargarVistas(Model model, Authentication auth);

    public void verTopProductos(Model model);
}
