package com.devalb.wellbing2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.devalb.wellbing2.service.CategoriaService;
import com.devalb.wellbing2.service.ProductoService;
import com.devalb.wellbing2.service.VistaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private VistaService vService;

    @GetMapping("/")
    public String goToIndex(Model model, Authentication auth) {
        model.addAttribute("listaProductosTop", productoService.getProductos());
        vService.cargarVistas(model, auth);
        return "index";
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

    @GetMapping("/admin")
    public String goToAdmin() {
        return "admin/index";
    }

    @GetMapping("/carrito")
    public String goToCarrito() {
        return "carrito";
    }

}
