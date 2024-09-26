package com.devalb.wellbing2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
        vService.verTopProductos(model);
        model.addAttribute("listaProductosTop", productoService.getProductos());
        vService.cargarVistas(model, auth);
        return "index";
    }

    @GetMapping("/productos")
    public String goToProductos(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        model.addAttribute("maxPrecio", productoService.getMaxPrecioProducto());
        model.addAttribute("listaCategorias", categoriaService.getCategorias());
        model.addAttribute("listaProductos", productoService.getProductosVisibles());

        return "productos";
    }

    @GetMapping("/nosotros")
    public String goToNosotros(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        return "nosotros";
    }

    @GetMapping("/producto_detalle/{id}")
    public String goToProductoDetalle(@PathVariable Long id, Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        model.addAttribute("producto", productoService.getProductoById(id));
        return "producto_detalle";
    }

    @GetMapping("/politicas_privacidad")
    public String goToPoliticasPrivacidad(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        return "politicas_privacidad";
    }

    @GetMapping("/aviso_legal")
    public String goToAvisoLegal(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        return "aviso_legal";
    }

    @GetMapping("/terminos_condiciones")
    public String goToTerminosCondiciones(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        return "terminos_condiciones";
    }

}
