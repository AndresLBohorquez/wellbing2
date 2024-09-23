package com.devalb.wellbing2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.devalb.wellbing2.service.ItemsOrdenService;

@Controller
public class ProductoController {

    @Autowired
    private ItemsOrdenService itemsOrdenService;

    @GetMapping("/reportes/top-productos")
    public String verTopProductos(Model model) {
        List<Object[]> productosMasVendidos = itemsOrdenService.obtenerTop10ProductosMasVendidos();
        model.addAttribute("productosMasVendidos", productosMasVendidos);
        return "reportes/top-productos";
    }

}
