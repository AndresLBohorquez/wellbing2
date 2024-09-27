package com.devalb.wellbing2.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.EstadoOrden;
import com.devalb.wellbing2.entity.ItemsOrden;
import com.devalb.wellbing2.entity.Orden;
import com.devalb.wellbing2.entity.Pago;
import com.devalb.wellbing2.entity.Producto;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.EstadoOrdenService;
import com.devalb.wellbing2.service.ItemsOrdenService;
import com.devalb.wellbing2.service.OrdenService;
import com.devalb.wellbing2.service.PagoService;
import com.devalb.wellbing2.service.ProductoService;
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

    @Autowired
    private EstadoOrdenService estadoOrdenService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PagoService pagoService;

    @GetMapping("/usuario/ordenes")
    public String goToOrdenes(Model model, Authentication auth) {
        log.info("Cargando la página de órdenes para el usuario: {}", auth.getName());

        vService.cargarVistas(model, auth);

        Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
        log.debug("Usuario autenticado: {}", usuario.getUsername());

        List<Orden> listaOrdenes = ordenService.getOrdenesVisiblesByUsuario(usuario.getId());
        log.debug("Cantidad de órdenes encontradas: {}", listaOrdenes.size());

        Map<Long, Pago> pagosMap = new HashMap<>();
        for (Orden orden : listaOrdenes) {
            Pago pago = pagoService.getPagoByOrdenId(orden.getId());
            pagosMap.put(orden.getId(), pago);
        }

        model.addAttribute("listaOrdenes", listaOrdenes);
        model.addAttribute("pagosMap", pagosMap); 

        return "usuario/ordenes";
    }

    @GetMapping("/usuario/ordenes/detalles/{id}")
    public String verDetallesOrden(@PathVariable Long id, Model model, Authentication auth,
            RedirectAttributes redirectAttributes) {
        log.info("Cargando detalles de la orden con ID: {}", id);

        vService.cargarVistas(model, auth);

        Orden orden = ordenService.getOrdenById(id);
        if (orden == null || !orden.getUsuario().getUsername().equals(auth.getName())) {
            log.warn("Orden no encontrada o no pertenece al usuario.");
            redirectAttributes.addFlashAttribute("messageKO", "Orden no encontrada o no pertenece al usuario.");
            return "redirect:/usuario/ordenes";
        }

        List<ItemsOrden> itemsOrden = itemsOrdenService.getItemsOrdenByIdOrden(id);

        Pago pago = pagoService.getPagoByOrdenId(id);
        model.addAttribute("orden", orden);
        model.addAttribute("itemsOrden", itemsOrden);
        model.addAttribute("pago", pago);

        return "usuario/detalles-orden";
    }

    @GetMapping("/admin/ordenes")
    @PreAuthorize("hasAnyAuthority('Admin', 'Domiciliario')")
    public String goToOrdenesAdmin(Model model, Authentication auth) {

        vService.cargarVistasAdmin(model, auth);
        List<Orden> ordenes = ordenService.getOrdenes();
        Map<Long, Pago> pagosMap = new HashMap<>();

        for (Orden orden : ordenes) {
            Pago pago = pagoService.getPagoByOrdenId(orden.getId());
            pagosMap.put(orden.getId(), pago);
        }

        model.addAttribute("listaOrdenes", ordenes);
        model.addAttribute("pagosMap", pagosMap);

        return "admin/ordenes";
    }

    @GetMapping("/admin/ordenes/detalle/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Domiciliario')")
    public String detalleOrden(@PathVariable Long id, Model model, Authentication auth) {
        try {
            log.info("Accediendo a los detalles de la orden con ID: {}", id);
            vService.cargarVistasAdmin(model, auth);

            Orden orden = ordenService.getOrdenById(id);

            model.addAttribute("orden", orden);
            model.addAttribute("itemsOrden", itemsOrdenService.getItemsOrdenByIdOrden(id));

            Pago pago = pagoService.getPagoByOrdenId(id);
            model.addAttribute("pago", pago);

            log.info("Detalles de la orden y pago cargados correctamente.");
            return "admin/detalles-orden";
        } catch (Exception e) {
            log.error("Error al obtener la orden con ID: {}", id, e);
            return "redirect:/admin/ordenes";
        }
    }

    @GetMapping("/admin/ordenes/editar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Domiciliario')")
    public String editarOrden(@PathVariable Long id, Model model, Authentication auth) {
        log.info("Accediendo a la edición de la orden con ID: {}", id);
        Orden orden = ordenService.getOrdenById(id);
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("orden", orden);
        model.addAttribute("itemsOrden", itemsOrdenService.getItemsOrdenByIdOrden(id));
        model.addAttribute("productos", productoService.getProductosVisibles());
        model.addAttribute("estadosOrden", estadoOrdenService.getEstadosOrden());
        log.info("Vista de edición de la orden cargada correctamente.");
        return "admin/ordenes-editar";
    }

    @PostMapping("/admin/ordenes/quitar-producto-orden/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Domiciliario')")
    public String quitarProductoOrden(@PathVariable Long id, @RequestParam Long ordenId,
            RedirectAttributes redirectAttributes) {
        log.info("Intentando quitar producto con ID: {} de la orden con ID: {}", id, ordenId);

        Orden orden = ordenService.getOrdenById(ordenId);
        itemsOrdenService.deleteItemsOrden(id);

        List<ItemsOrden> itemOrden = itemsOrdenService.getItemsOrdenByIdOrden(ordenId);
        orden.setTotal(calcularTotalOrden(itemOrden));
        ordenService.editOrden(orden);

        log.info("Producto con ID: {} eliminado de la orden con ID: {} correctamente.", id, ordenId);
        redirectAttributes.addFlashAttribute("messageOK", "Producto quitado de la orden correctamente");
        return "redirect:/admin/ordenes/editar/" + ordenId;
    }

    @PostMapping("/admin/ordenes/agregar-producto-orden/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Domiciliario')")
    public String agregarProductoOrden(@PathVariable Long id, @RequestParam Long producto,
            @RequestParam Integer cantidad, RedirectAttributes redirectAttributes) {
        log.info("Intentando agregar producto con ID: {} y cantidad: {} a la orden con ID: {}", producto, cantidad, id);

        try {
            Orden orden = ordenService.getOrdenById(id);
            Producto productoSeleccionado = productoService.getProductoById(producto);

            ItemsOrden itemOrden = new ItemsOrden();
            itemOrden.setOrden(orden);
            itemOrden.setProducto(productoSeleccionado);
            itemOrden.setCantidad(cantidad);
            itemsOrdenService.addItemsOrden(itemOrden);

            List<ItemsOrden> itemsOrden = itemsOrdenService.getItemsOrdenByIdOrden(id);
            orden.setTotal(calcularTotalOrden(itemsOrden));
            orden.setFechaActualizacion(LocalDateTime.now());
            ordenService.editOrden(orden);

            log.info("Producto con ID: {} agregado correctamente a la orden con ID: {}", producto, id);
            redirectAttributes.addFlashAttribute("messageOK", "Producto agregado a la orden correctamente");
        } catch (Exception e) {
            log.error("Error al agregar el producto con ID: {} a la orden con ID: {}", producto, id, e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al agregar producto a la orden.");
        }

        return "redirect:/admin/ordenes/editar/" + id;
    }

    @PostMapping("/admin/ordenes/editar-orden/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Domiciliario')")
    public String actualizarEstadoOrden(@PathVariable Long id, @RequestParam("estadoOrden") Long estadoId,
            RedirectAttributes redirectAttributes) {
        log.info("Actualizando estado de la orden con ID: {} al estado con ID: {}", id, estadoId);

        try {
            Orden orden = ordenService.getOrdenById(id);
            EstadoOrden nuevoEstado = estadoOrdenService.getEstadoOrdenById(estadoId);
            orden.setEstadoOrden(nuevoEstado);
            orden.setFechaActualizacion(LocalDateTime.now());
            System.out.println(LocalDateTime.now());
            ordenService.editOrden(orden);

            log.info("Estado de la orden con ID: {} actualizado correctamente.", id);
            redirectAttributes.addFlashAttribute("messageOK",
                    "El estado de la orden ha sido actualizado correctamente.");
        } catch (Exception e) {
            log.error("Error al actualizar el estado de la orden con ID: {}", id, e);
            redirectAttributes.addFlashAttribute("messageKO", "Ocurrió un error al actualizar el estado de la orden.");
        }

        return "redirect:/admin/ordenes";
    }

    public double calcularTotalOrden(List<ItemsOrden> itemOrden) {
        log.info("Calculando total de la orden.");
        double total = itemOrden.stream()
                .mapToDouble(iOrden -> iOrden.getCantidad() * iOrden.getProducto().getPrecio())
                .sum();
        log.info("Total de la orden calculado: {}", total);
        return total;
    }
}
