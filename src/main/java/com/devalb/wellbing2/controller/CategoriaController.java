package com.devalb.wellbing2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.devalb.wellbing2.entity.Categoria;
import com.devalb.wellbing2.service.CategoriaService;
import com.devalb.wellbing2.service.VistaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private VistaService vService;

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public String goToCategorias(Model model, Authentication auth) {
        log.info("Mostrando lista de categorías");
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("categoriaObj", new Categoria());
        model.addAttribute("listaCategorias", categoriaService.getCategorias());
        return "admin/categorias";
    }

    @GetMapping("/crear")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public String createCategoriaForm(Model model, Authentication auth) {
        log.info("Mostrando formulario de creación de categoría");
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("categoria", new Categoria());
        return "admin/categoria-form";
    }

    @PostMapping("/crear")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public RedirectView createCategoria(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttributes) {
        log.info("Creando nueva categoría: {}", categoria.getNombre());
        categoriaService.addCategoria(categoria);
        redirectAttributes.addFlashAttribute("messageOK", "Categoría creada correctamente");
        return new RedirectView("/admin/categorias", true);
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public String editCategoriaForm(@PathVariable Long id, Model model, Authentication auth) {
        log.info("Mostrando formulario de edición de categoría: {}", id);
        vService.cargarVistasAdmin(model, auth);
        Categoria categoria = categoriaService.getCategoriaById(id);
        model.addAttribute("categoria", categoria);
        return "admin/categoria-form";
    }

    @PostMapping("/editar")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public RedirectView editCategoria(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttributes) {
        log.info("Editando categoría: {}", categoria.getNombre());
        categoriaService.editCategoria(categoria);
        redirectAttributes.addFlashAttribute("messageOK", "Categoría editada correctamente");
        return new RedirectView("/admin/categorias", true);
    }
}
