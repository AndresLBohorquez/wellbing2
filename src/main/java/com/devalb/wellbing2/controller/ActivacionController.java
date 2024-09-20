package com.devalb.wellbing2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.devalb.wellbing2.service.ActivacionService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;

@Controller
public class ActivacionController {

    @Autowired
    private VistaService vService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ActivacionService activacionService;

    @GetMapping("/usuario/activaciones")
    public String goToUsuarioActivaciones(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        var usuLog = usuarioService.getUsuarioByUsername(auth.getName());
        model.addAttribute("listaActivacionesUsuario", activacionService
                .getActivacionesByIdUsuario(usuLog.getId()));

        return "usuario/activaciones";
    }
}
