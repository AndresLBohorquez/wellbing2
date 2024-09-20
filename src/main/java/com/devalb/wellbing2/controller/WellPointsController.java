package com.devalb.wellbing2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;
import com.devalb.wellbing2.service.WellPointsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WellPointsController {

    @Autowired
    private VistaService vService;

    @Autowired
    private WellPointsService wellPointsService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuario/wellpoints")
    public String goToWellPoints(Model model, Authentication auth) {
        log.info("Accediendo a la vista de WellPoints. Usuario autenticado: {}", auth.getName());

        try {
            vService.cargarVistas(model, auth);
            log.debug("Vista cargada correctamente para el usuario: {}", auth.getName());

            Long usuarioId = usuarioService.getUsuarioByUsername(auth.getName()).getId();
            log.debug("ID del usuario obtenido: {}", usuarioId);

            model.addAttribute("listaWellPoints", wellPointsService.getWellPointsByUsuario(usuarioId));
            log.info("WellPoints cargados correctamente para el usuario con ID: {}", usuarioId);

        } catch (Exception e) {
            log.error("Error al cargar la vista de WellPoints para el usuario: {}", auth.getName(), e);
            model.addAttribute("messageKO", "Error al cargar la vista de WellPoints.");
            return "usuario";
        }

        return "usuario/wellpoints";
    }
}
