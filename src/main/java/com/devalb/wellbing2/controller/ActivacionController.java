package com.devalb.wellbing2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.devalb.wellbing2.entity.Activacion;
import com.devalb.wellbing2.service.ActivacionService;
import com.devalb.wellbing2.service.EstadoActivacionService;
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

    @Autowired
    private EstadoActivacionService eActivacionService;

    @GetMapping("/usuario/activaciones")
    public String goToUsuarioActivaciones(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        var usuLog = usuarioService.getUsuarioByUsername(auth.getName());
        model.addAttribute("listaActivacionesUsuario", activacionService
                .getActivacionesByIdUsuario(usuLog.getId()));

        return "usuario/activaciones";
    }

    @GetMapping("/admin/activaciones")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String goToActivaciones(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("activaciones", activacionService.getActivaciones());

        return "admin/activaciones";
    }

    // Método para activar una activación
    @GetMapping("/admin/activaciones/activar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String activarActivacion(@PathVariable Long id) {
        // Lógica para activar la activación
        Activacion activacion = activacionService.getActivacionById(id);
        activacion.setEstadoActivacion(eActivacionService.getEstadoActivacionByNombre("Activado"));
        activacionService.editActivacion(activacion);
        return "redirect:/admin/activaciones";
    }

    // Método para inactivar una activación
    @GetMapping("/admin/activaciones/inactivar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String inactivarActivacion(@PathVariable Long id) {
        // Lógica para inactivar la activación
        Activacion activacion = activacionService.getActivacionById(id);
        activacion.setEstadoActivacion(eActivacionService.getEstadoActivacionByNombre("Inactivo"));
        activacionService.editActivacion(activacion);
        return "redirect:/admin/activaciones";
    }

    // Método para preactivar una activación
    @GetMapping("/admin/activaciones/preactivar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String preactivarActivacion(@PathVariable Long id) {
        // Lógica para reactivar la activación
        Activacion activacion = activacionService.getActivacionById(id);
        activacion.setEstadoActivacion(eActivacionService.getEstadoActivacionByNombre("Pre Activado"));
        activacionService.editActivacion(activacion);
        return "redirect:/admin/activaciones";
    }

    // Método para rechazar una activación
    @GetMapping("/admin/activaciones/rechazar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String rechazarActivacion(@PathVariable Long id) {
        // Lógica para rechazar la activación
        Activacion activacion = activacionService.getActivacionById(id);
        activacion.setEstadoActivacion(eActivacionService.getEstadoActivacionByNombre("Rechazada"));
        activacionService.editActivacion(activacion);
        return "redirect:/admin/activaciones";
    }

    // Método para validar una activación
    @GetMapping("/admin/activaciones/validar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String validarActivacion(@PathVariable Long id) {
        // Lógica para validar la activación
        Activacion activacion = activacionService.getActivacionById(id);
        activacion.setEstadoActivacion(eActivacionService.getEstadoActivacionByNombre("Validado"));
        activacionService.editActivacion(activacion);
        return "redirect:/admin/activaciones";
    }
}
