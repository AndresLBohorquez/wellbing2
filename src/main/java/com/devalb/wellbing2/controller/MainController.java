package com.devalb.wellbing2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String goToIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String goToLogin() {
        return "login";
    }

    @GetMapping("/signup")
    public String goToRegister() {
        return "register";
    }

    @GetMapping("/productos")
    public String goToProductos() {
        return "productos";
    }

    @GetMapping("/nosotros")
    public String goToNosotros() {
        return "nosotros";
    }

    @GetMapping("/recuperar-pass")
    public String goToRecuperarPass() {
        return "recuperar-pass";
    }

    @GetMapping("/producto_detalle")
    public String goToProductoDetalle() {
        return "producto_detalle";
    }

    @GetMapping("/politicas_privacidad")
    public String goToPoliticasPrivacidad() {
        return "politicas_privacidad";
    }

    @GetMapping("/aviso_legal")
    public String goToAvisoLegal() {
        return "aviso_legal";
    }

    @GetMapping("/terminos_condiciones")
    public String goToTerminosCondiciones() {
        return "terminos_condiciones";
    }

}
