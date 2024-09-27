package com.devalb.wellbing2.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.Orden;
import com.devalb.wellbing2.entity.Pago;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.EstadoPagoService;
import com.devalb.wellbing2.service.OrdenService;
import com.devalb.wellbing2.service.PagoService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PagoController {

    @Autowired
    private VistaService vService;

    @Autowired
    private PagoService pagoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private EstadoPagoService estadoPagoService;

    @GetMapping("/usuario/pagos")
    public String goToPagos(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
        model.addAttribute("listaPagos", pagoService.getPagosByUsuario(usuario.getId()));
        model.addAttribute("pago", new Pago());
        return "usuario/pagos";
    }

    @RequestMapping("/usuario/pago/crear/{id}")
    public String crearPago(@PathVariable Long id, Authentication auth,
            RedirectAttributes redirectAttributes, @RequestParam("cantidad") Double cantidad,
            @RequestParam("comprobante") MultipartFile comprobante) {
        log.info("Crear pago para la orden con id {}", id);

        Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());

        Orden orden = ordenService.getOrdenById(id);

        if (!cantidad.equals(orden.getTotal())) {
            log.error("No coincide el valor registrado con el valor total de la orden {}", id);
            redirectAttributes.addFlashAttribute("messageKO",
                    "No coincide el valor registrado con el valor total de la orden.");
            return "redirect:/usuario/ordenes";
        }

        // Validar que no haya otro pago para la misma orden
        if (pagoService.existsByOrdenId(id)) {
            log.error("Ya existe un pago para la orden con id {}", id);
            redirectAttributes.addFlashAttribute("messageKO", "Ya existe un pago para esta orden");
            return "redirect:/usuario/ordenes";
        }

        // Crear el pago
        Pago pago = new Pago();
        pago.setCantidad(cantidad);
        pago.setFecha(LocalDate.now());
        pago.setUsuario(usuario);
        pago.setOrden(orden);
        pago.setEstadoPago(estadoPagoService.getEstadoPagoByNombre("Pendiente"));

        // Procesar el comprobante
        if (comprobante.isEmpty()) {
            log.error("El comprobante no puede estar vacío");
            redirectAttributes.addFlashAttribute("messageKO", "El comprobante no puede estar vacío");
            return "redirect:/usuario/ordenes";
        }

        // Guardar el comprobante en la ruta \images\pagos\
        String fileName = "pago_" + orden.getId() + ".png";
        try {
            byte[] bytes = comprobante.getBytes();
            Path ruta = Paths.get("src/main/resources/static/images/pagos/" + fileName);
            if (Files.exists(ruta)) {
                Files.delete(ruta);
            }
            Files.write(ruta, bytes);
            log.info("Imagen subida correctamente");
        } catch (IOException e) {
            log.error("Error al guardar el comprobante", e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al guardar el comprobante");
            return "redirect:/usuario/ordenes";
        }

        // Guardar el pago
        pago.setComprobante(fileName);
        pagoService.addPago(pago);

        log.info("Pago creado con éxito");
        redirectAttributes.addFlashAttribute("messageOK", "Pago creado con éxito");
        return "redirect:/usuario/ordenes";
    }

    @GetMapping("/admin/pagos")
    @PreAuthorize("hasAnyAuthority('Admin', 'Tesorero')")
    public String goToPagosAdmin(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("listaPagos", pagoService.getPagos());
        model.addAttribute("estadosPago", estadoPagoService.getEstadosPago());
        return "admin/pagos";
    }

    @PostMapping("/admin/pagos/editar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Tesorero')")
    public String editarEstadoPago(@PathVariable Long id, @RequestParam("estadoPago") String estadoPago,
            RedirectAttributes redirectAttributes) {
        Pago pago = pagoService.getPagoById(id);

        pago.setEstadoPago(estadoPagoService.getEstadoPagoByNombre(estadoPago));
        pagoService.editPago(pago);

        redirectAttributes.addFlashAttribute("messageOK", "Estado de pago actualizado con éxito.");
        return "redirect:/admin/pagos";
    }

    @PostMapping("/admin/pagos/eliminar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Tesorero')")
    public String eliminarPago(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pagoService.deletePago(id);
        redirectAttributes.addFlashAttribute("messageOK", "Pago eliminado correctamente");
        return "redirect:/admin/pagos";
    }

}
