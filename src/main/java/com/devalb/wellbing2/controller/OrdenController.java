package com.devalb.wellbing2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.devalb.wellbing2.entity.Orden;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.OrdenService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class OrdenController {

    @Autowired
    private VistaService vService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuario/ordenes")
    public String goToOrdenes(Model model, Authentication auth) {
        log.info("Cargando la página de órdenes para el usuario: {}", auth.getName());

        // Cargar vistas generales (ejemplo: información del usuario logueado)
        vService.cargarVistas(model, auth);

        // Obtener el usuario autenticado
        Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
        log.debug("Usuario autenticado: {}", usuario.getUsername());

        // Obtener las órdenes visibles del usuario autenticado
        List<Orden> listaOrdenes = ordenService.getOrdenesVisiblesByUsuario(usuario.getId());
        log.debug("Cantidad de órdenes encontradas: {}", listaOrdenes.size());

        // Añadir la lista de órdenes al modelo
        model.addAttribute("listaOrdenes", listaOrdenes);

        return "usuario/ordenes"; // Vista Thymeleaf correspondiente
    }

}
