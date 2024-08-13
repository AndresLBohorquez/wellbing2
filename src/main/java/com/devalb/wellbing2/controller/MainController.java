package com.devalb.wellbing2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.devalb.wellbing2.service.CategoriaService;
import com.devalb.wellbing2.service.ProductoService;

@Controller
public class MainController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/")
    public String goToIndex(Model model) {
        model.addAttribute("listaProductosTop", productoService.getProductos());
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
    public String goToProductos(Model model) {
        model.addAttribute("maxPrecio", productoService.getMaxPrecioProducto());
        model.addAttribute("listaCategorias", categoriaService.getCategorias());
        model.addAttribute("listaProductos", productoService.getProductos());

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
