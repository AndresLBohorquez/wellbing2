package com.devalb.wellbing2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.ItemsOrden;
import com.devalb.wellbing2.entity.Orden;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.ItemsOrdenService;
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

    @Autowired
    private ItemsOrdenService itemsOrdenService;

    @GetMapping("/usuario/ordenes")
    public String goToOrdenes(Model model, Authentication auth) {
        log.info("Cargando la página de órdenes para el usuario: {}", auth.getName());

        vService.cargarVistas(model, auth);

        // Obtener el usuario autenticado
        Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
        log.debug("Usuario autenticado: {}", usuario.getUsername());

        // Obtener las órdenes visibles del usuario autenticado
        List<Orden> listaOrdenes = ordenService.getOrdenesVisiblesByUsuario(usuario.getId());
        log.debug("Cantidad de órdenes encontradas: {}", listaOrdenes.size());

        model.addAttribute("listaOrdenes", listaOrdenes);

        return "usuario/ordenes";
    }

    @GetMapping("/usuario/ordenes/detalles/{id}")
    public String verDetallesOrden(@PathVariable Long id, Model model, Authentication auth,
            RedirectAttributes redirectAttributes) {
        log.info("Cargando detalles de la orden con ID: {}", id);

        vService.cargarVistas(model, auth);

        // Obtener la orden por ID
        Orden orden = ordenService.getOrdenById(id);
        if (orden == null || !orden.getUsuario().getUsername().equals(auth.getName())) {
            log.warn("Orden no encontrada o no pertenece al usuario.");
            redirectAttributes.addFlashAttribute("messageKO", "Orden no encontrada o no pertenece al usuario.");
            return "redirect:/usuario/ordenes";
        }

        List<ItemsOrden> itemsOrden = itemsOrdenService.getItemsOrdenByIdOrden(id);

        model.addAttribute("orden", orden);
        model.addAttribute("itemsOrden", itemsOrden);

        return "usuario/detalles-orden";
    }

}
