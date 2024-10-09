package com.devalb.wellbing2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.Activacion;
import com.devalb.wellbing2.entity.Equipo;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.ActivacionService;
import com.devalb.wellbing2.service.EquipoService;
import com.devalb.wellbing2.service.EstadoEquipoService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class EquipoController {

    @Autowired
    private VistaService vService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private ActivacionService activacionService;

    @Autowired
    private EstadoEquipoService estadoEquipoService;

    @GetMapping("/usuario/equipo")
    public String goToEquipo(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        List<Equipo> equipos = equipoService
                .getEquipoByIdUsuario(usuarioService.getUsuarioByUsername(auth.getName()).getId());

        calcularNietos(equipos);

        model.addAttribute("ultimaActivacion",
                activacionService.getUltimActivacion(usuarioService.getUsuarioByUsername(auth.getName()).getId()));
        model.addAttribute("listaEquipo", equipos);
        model.addAttribute("equipo", new Equipo());
        return "usuario/equipo";
    }

    @PostMapping("/usuario/equipo/crear")
    public String crearEquipo(@ModelAttribute("equipo") Equipo equipo,
            Model model,
            Authentication auth) {

        log.info("Iniciando la creación de un nuevo equipo. Usuario autenticado: {}", auth.getName());

        // Obtener el usuario autenticado (quien crea el equipo)
        Usuario usuarioActual = usuarioService.getUsuarioByUsername(auth.getName());
        log.debug("Usuario actual obtenido: {}", usuarioActual.getUsername());

        // Buscar el usuario por código desde el equipo
        Usuario usuarioHijo = usuarioService.getUsuarioByCodigoUsuario(equipo.getIdHijo().getCodigoUsuario());
        if (usuarioHijo == null) {
            log.warn("Código de usuario inválido: {}", equipo.getIdHijo().getCodigoUsuario());
            model.addAttribute("messageKO", "El código de usuario no es válido.");
            cargarDatosDeEquipo(model, auth);
            return "usuario/equipo";
        }
        log.debug("Usuario hijo encontrado: {}", usuarioHijo.getUsername());

        // Validar que el usuario no pueda agregarse a sí mismo como hijo
        if (usuarioActual.getId().equals(usuarioHijo.getId())) {
            log.warn("El usuario {} está intentando agregarse a sí mismo como hijo.", usuarioActual.getUsername());
            model.addAttribute("messageKO", "No puedes agregarte a ti mismo como miembro del equipo.");
            cargarDatosDeEquipo(model, auth);
            return "usuario/equipo";
        }

        // Validaciones adicionales
        if (equipoService.existeRelacion(usuarioActual.getId(), usuarioHijo.getId())) {
            log.warn("El usuario {} ya ha sido registrado por el usuario {}.", usuarioActual.getUsername(),
                    usuarioHijo.getUsername());
            model.addAttribute("messageKO", "No puedes registrar a un usuario que ya te ha registrado a ti.");
            cargarDatosDeEquipo(model, auth);
            return "usuario/equipo";
        }

        if (equipoService.existeRelacion(usuarioHijo.getId(), usuarioActual.getId())) {
            log.warn("Ya existe una relación de equipo entre el usuario {} y el usuario {}.", usuarioHijo.getUsername(),
                    usuarioActual.getUsername());
            model.addAttribute("messageKO", "Ya existe una relación de equipo con este usuario.");
            cargarDatosDeEquipo(model, auth);
            return "usuario/equipo";
        }

        // Verificar si ya existe un equipo con el mismo idHijo para el usuario
        if (equipoService.getEquipoPorIdHijo(usuarioHijo.getId()) != null) {
            log.warn("El usuario {} ya tiene registrado un equipo para el usuario hijo {}.",
                    usuarioActual.getUsername(), usuarioHijo.getUsername());
            model.addAttribute("messageKO", "Este usuario ya está registrado en un equipo.");
            cargarDatosDeEquipo(model, auth);
            return "usuario/equipo";
        }

        // Crear un nuevo equipo
        try {
            equipo.setIdHijo(usuarioHijo);
            equipo.setUsuario(usuarioActual);
            equipo.setEstadoEquipo(estadoEquipoService.getByNombreEstado("Pendiente"));
            equipo.setVisible(true);

            // Guardar el equipo
            equipoService.addEquipo(equipo);
            log.info("Equipo guardado exitosamente para el usuario: {}", usuarioActual.getUsername());

        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                log.error("Error al crear equipo: Usuario ya registrado en un equipo de trabajo");
                model.addAttribute("messageKO", "Usuario ya registrado en un equipo de trabajo.");
            } else {
                log.error("Ha ocurrido un error al crear el equipo: {}", e.getMessage());
                model.addAttribute("messageKO", "Ha ocurrido un error al crear el equipo de trabajo.");
            }
            return "usuario/equipo";
        }

        model.addAttribute("messageOK", "Equipo creado exitosamente.");
        cargarDatosDeEquipo(model, auth);
        return "usuario/equipo";
    }

    public void cargarDatosDeEquipo(Model model, Authentication auth) {
        // Cargar las vistas comunes
        vService.cargarVistas(model, auth);

        // Cargar información adicional relacionada con el equipo
        Usuario usuarioActual = usuarioService.getUsuarioByUsername(auth.getName());
        List<Equipo> equipos = equipoService.getEquipoByIdUsuario(usuarioActual.getId());

        // Cargar la última activación y el número de nietos (hijos del hijo)
        for (Equipo equipo : equipos) {
            Activacion ultimaActivacion = activacionService.getUltimActivacion(equipo.getIdHijo().getId());
            equipo.getIdHijo().setUltimaActivacion(ultimaActivacion);
            equipo.setCantidadNietos(equipoService.getEquiposVisiblesByUsuario(equipo.getIdHijo().getId()).size());
        }

        // Agregar los datos al modelo
        model.addAttribute("listaEquipo", equipos);
        model.addAttribute("ultimaActivacion", activacionService.getUltimActivacion(usuarioActual.getId()));
        model.addAttribute("equipo", new Equipo());
    }

    @GetMapping("/admin/equipo")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String goToEquipoAdmin(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("listaEquipos", equipoService.getEquipos());
        return "admin/equipo";
    }

    @GetMapping("/admin/equipo/aprobar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String aprobarEquipo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Equipo equipo = equipoService.getEquipoById(id);
            equipo.setEstadoEquipo(estadoEquipoService.getByNombreEstado("Aprobado"));
            equipoService.editEquipo(equipo);
            log.info("Equipo aprobado con éxito. ID: {}", id);
            redirectAttributes.addFlashAttribute("messageOK", "Equipo aprobado con éxito");
            return "redirect:/admin/equipo";
        } catch (Exception e) {
            log.error("Error al aprobar equipo. ID: {}", id, e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al aprobar equipo");
            return "redirect:/admin/equipo";
        }
    }

    @GetMapping("/admin/equipo/pendiente/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String pendienteEquipo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Equipo equipo = equipoService.getEquipoById(id);
            equipo.setEstadoEquipo(estadoEquipoService.getByNombreEstado("Pendiente"));
            equipoService.editEquipo(equipo);
            log.info("Equipo puesto en pendiente con éxito. ID: {}", id);
            redirectAttributes.addFlashAttribute("messageOK", "Equipo puesto en pendiente con éxito");
            return "redirect:/admin/equipo";
        } catch (Exception e) {
            log.error("Error al poner equipo en pendiente. ID: {}", id, e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al poner equipo en pendiente");
            return "redirect:/admin/equipo";
        }
    }

    @GetMapping("/admin/equipo/rechazar/{id}")
    public String rechazarEquipo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Equipo equipo = equipoService.getEquipoById(id);
            equipo.setEstadoEquipo(estadoEquipoService.getByNombreEstado("Rechazado"));
            equipoService.editEquipo(equipo);
            log.info("Equipo rechazado con éxito. ID: {}", id);
            redirectAttributes.addFlashAttribute("messageOK", "Equipo rechazado con éxito");
            return "redirect:/admin/equipo";
        } catch (Exception e) {
            log.error("Error al rechazar equipo. ID: {}", id, e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al rechazar equipo");
            return "redirect:/admin/equipo";
        }
    }

    public void calcularNietos(List<Equipo> equipos) {

        for (Equipo equipo : equipos) {
            equipo.getIdHijo().setUltimaActivacion(activacionService.getUltimActivacion(equipo.getIdHijo().getId()));

            List<Equipo> equiposHijo = equipoService
                    .getEquipoByIdUsuario(equipo.getIdHijo().getId());

            if (equiposHijo != null) {
                var nietos = 0;
                for (Equipo equiH : equiposHijo) {
                    equiH.getIdHijo()
                            .setUltimaActivacion(activacionService.getUltimActivacion(equiH.getIdHijo().getId()));
                    if (equiH.getIdHijo().getUltimaActivacion() != null) {
                        if (equiH.getIdHijo().getUltimaActivacion().getEstadoActivacion().getNombre()
                                .equals("Activado")
                                || equiH.getIdHijo().getUltimaActivacion().getEstadoActivacion().getNombre()
                                        .equals("Validado")) {
                            nietos++;
                        }
                    }

                }
                equipo.setCantidadNietos(nietos);
            }
        }
    }

}
