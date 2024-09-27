package com.devalb.wellbing2.controller;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.EstadoPqrs;
import com.devalb.wellbing2.entity.Mensaje;
import com.devalb.wellbing2.entity.Pqrs;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.EstadoPqrsService;
import com.devalb.wellbing2.service.MensajeService;
import com.devalb.wellbing2.service.PqrsService;
import com.devalb.wellbing2.service.TipoPqrsService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PqrsController {

    @Autowired
    private VistaService vService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PqrsService pqrsService;

    @Autowired
    private EstadoPqrsService estadoPqrsService;

    @Autowired
    private TipoPqrsService tipoPqrsService;

    @Autowired
    private MensajeService mensajeService;

    @GetMapping("/usuario/pqrs")
    public String goToPqrs(Model model, Authentication auth) {
        log.info("Accediendo a la vista de PQRS. Usuario autenticado: {}", auth.getName());

        try {
            cargarDatosDePqrs(model, auth);
        } catch (Exception e) {
            log.error("Error al cargar la vista de PQRS para el usuario: {}", auth.getName(), e);
            model.addAttribute("messageKO", "Error al cargar la vista de PQRS.");
            return "error"; // Redirigir a una página de error o mostrar un mensaje en la misma página.
        }

        return "usuario/pqrs";
    }

    @PostMapping("/usuario/pqrs")
    public String crearPqrs(@ModelAttribute("pqrs") Pqrs pqrs, Authentication auth,
            RedirectAttributes redirectAttributes) {

        log.info("Iniciando el proceso de creación de una nueva PQRS. Usuario autenticado: {}", auth.getName());

        try {
            setDatosPqrs(pqrs, auth);
            pqrsService.addPqrs(pqrs);
            log.info("PQRS creada correctamente para el usuario: {}", auth.getName());

            crearMensaje(pqrs, auth);
            redirectAttributes.addFlashAttribute("messageOK", "PQRS creada correctamente");
        } catch (Exception e) {
            log.error("Error al crear la PQRS para el usuario: {}", auth.getName(), e);
            redirectAttributes.addFlashAttribute("messageKO", "No se ha podido crear la PQRS");
        }

        return "redirect:/usuario/pqrs";
    }

    private void cargarDatosDePqrs(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);

        Long usuarioId = usuarioService.getUsuarioByUsername(auth.getName()).getId();
        log.debug("ID del usuario obtenido: {}", usuarioId);

        model.addAttribute("listaPqrs", pqrsService.getPqrsByUsuario(usuarioId));
        model.addAttribute("pqrs", new Pqrs());
        model.addAttribute("listaTipoPqrs", tipoPqrsService.getTipoPqrs());
        log.info("Datos de PQRS cargados correctamente para el usuario con ID: {}", usuarioId);
    }

    private void setDatosPqrs(Pqrs pqrs, Authentication auth) {
        pqrs.setFechaRegistro(LocalDateTime.now());
        pqrs.setFechaActualizacion(LocalDateTime.now());

        pqrs.setEstadoPqrs(estadoPqrsService.getEstadoPqrsByNombre("Abierta"));
        log.debug("Estado de PQRS establecido como 'Abierta'.");

        Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
        pqrs.setUsuario(usuario);
        log.debug("Usuario asignado a la PQRS: {}", usuario.getUsername());
    }

    private void crearMensaje(Pqrs pqrs, Authentication auth) {
        Mensaje mensaje = new Mensaje();
        mensaje.setContenido(pqrs.getContenido());
        mensaje.setFecha(LocalDateTime.now());
        mensaje.setPqrs(pqrs);

        Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
        mensaje.setUsuario(usuario);
        mensajeService.addMensaje(mensaje);

        log.info("Mensaje creado y asociado a la PQRS para el usuario: {}", usuario.getUsername());
    }

    @GetMapping("/usuario/pqrs-detalle/{id}")
    public String goToUsuarioPqrsDetalle(@PathVariable("id") Long id, Model model, Authentication auth) {
        log.info("Accediendo al detalle de PQRS con ID: {} para el usuario autenticado: {}", id, auth.getName());

        try {
            // Obtener la PQRS por ID
            var pqrs = pqrsService.getPqrsById(id);
            if (pqrs == null) {
                log.warn("La PQRS con ID: {} no fue encontrada.", id);
                model.addAttribute("messageKO", "PQRS no encontrada.");
                return "usuario/pqrs";
            }

            // Obtener los mensajes asociados a la PQRS
            List<Map.Entry<String, Mensaje>> listaMensajes = new ArrayList<>();
            var mensajes = mensajeService.getByIdPqrs(pqrs.getId());

            for (Mensaje mens : mensajes) {
                String tipoUsuario = mens.getUsuario().getRoles().size() > 1 ? "Admin" : "Usuario";
                listaMensajes.add(new AbstractMap.SimpleEntry<>(tipoUsuario, mens));
            }

            // Cargar datos de vistas generales (nombre, apellido, roles, etc.)
            vService.cargarVistas(model, auth);

            // Añadir datos específicos de la PQRS y mensajes
            model.addAttribute("mensajeObj", new Mensaje()); // Para añadir nuevos mensajes
            model.addAttribute("mensajes", listaMensajes);
            model.addAttribute("pqrs", pqrs);

            log.info("Datos del detalle de PQRS cargados correctamente.");
        } catch (Exception e) {
            log.error("Error al cargar el detalle de PQRS con ID: {} para el usuario: {}", id, auth.getName(), e);
            model.addAttribute("messageKO", "Error al cargar el detalle de PQRS.");
            return "usuario/pqrs"; // Redirigir de vuelta a la lista de PQRS en caso de error
        }

        return "usuario/pqrs-detalle";
    }

    @PostMapping("/usuario/pqrs-detalle")
    public String crearMensaje(@RequestParam("pqrsId") Long pqrsId,
            @ModelAttribute("mensajeObj") Mensaje mensaje,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        log.info("Iniciando la creación de un nuevo mensaje para la PQRS con ID: {} por el usuario: {}", pqrsId,
                auth.getName());

        try {
            // Obtener la PQRS asociada al mensaje
            var pqrs = pqrsService.getPqrsById(pqrsId);
            if (pqrs == null) {
                log.warn("No se encontró la PQRS con ID: {}", pqrsId);
                redirectAttributes.addFlashAttribute("messageKO", "La PQRS no existe.");
                return "redirect:/usuario/pqrs";
            }

            // Actualizar el estado y la fecha de la PQRS
            actualizarEstadoYFechaPqrs(pqrs);

            // Configurar el mensaje con la PQRS y el usuario autenticado
            prepararMensaje(mensaje, pqrs, auth);

            // Guardar el mensaje
            mensajeService.addMensaje(mensaje);
            log.info("Mensaje enviado correctamente por el usuario: {} para la PQRS con ID: {}", auth.getName(),
                    pqrsId);

            redirectAttributes.addFlashAttribute("messageOK", "Mensaje enviado correctamente");
        } catch (Exception e) {
            log.error("Error al enviar el mensaje para la PQRS con ID: {}", pqrsId, e);
            redirectAttributes.addFlashAttribute("messageKO", "No se ha podido enviar el mensaje");
        }

        return "redirect:/usuario/pqrs-detalle/" + pqrsId;
    }

    @GetMapping("/admin/pqrs")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario')")
    public String goToPqrsAdmin(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("listaPqrs", pqrsService.getPqrs());
        return "admin/pqrs";
    }

    @RequestMapping("/admin/pqrs/abrir/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario')")
    public String abrirPqrs(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var pqrs = pqrsService.getPqrsById(id);
            var estadoPqrs = estadoPqrsService.getEstadoPqrsByNombre("Abierta");
            pqrs.setEstadoPqrs(estadoPqrs);
            pqrs.setFechaActualizacion(LocalDateTime.now());
            pqrsService.editPqrs(pqrs);
            redirectAttributes.addFlashAttribute("messageOK", "PQRS abierta correctamente");
        } catch (Exception e) {
            System.out.println("PqrsController.abrirPqrs()" + e.getMessage());
            redirectAttributes.addFlashAttribute("messageKO", "No ha sido posible abrir la PQRS");
        }
        return "redirect:/admin/pqrs";
    }

    @RequestMapping("/admin/pqrs/eliminar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario')")
    public String eliminarPqrs(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var pqrs = pqrsService.getPqrsById(id);
            var estadoPqrs = estadoPqrsService.getEstadoPqrsByNombre("Eliminada");
            pqrs.setEstadoPqrs(estadoPqrs);
            pqrs.setFechaActualizacion(LocalDateTime.now());
            pqrsService.editPqrs(pqrs);
            redirectAttributes.addFlashAttribute("messageOK", "PQRS eliminada correctamente");
        } catch (Exception e) {
            System.out.println("PqrsController.eliminarPqrs()" + e.getMessage());
            redirectAttributes.addFlashAttribute("messageKO", "No ha sido posible eliminar la PQRS");
        }
        return "redirect:/admin/pqrs";
    }

    @GetMapping("/admin/pqrs/responder/{id}")
    public String goToResponderPqrs(@PathVariable Long id, Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        var pqrsUsuario = pqrsService.getPqrsById(id);

        List<Map.Entry<String, Mensaje>> listaMensajes = new ArrayList<>();
        var mensajes = mensajeService.getByIdPqrs(pqrsUsuario.getId());

        for (Mensaje mens : mensajes) {
            String tipoUsuario = mens.getUsuario().getRoles().size() > 1 ? "Admin" : "User";
            listaMensajes.add(new AbstractMap.SimpleEntry<>(tipoUsuario, mens));
        }

        model.addAttribute("pqrsUsuario", pqrsUsuario);
        model.addAttribute("mensajeObj", new Mensaje());
        model.addAttribute("listaMensajes", listaMensajes);

        return "admin/pqrs-responder";
    }

    @PostMapping("/admin/pqrs/responder")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario')")
    public String postMethodName(@RequestParam("pqrsId") Long pqrsId, @ModelAttribute("mensajeObj") Mensaje mensaje,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {
            if (mensaje.getContenido().isEmpty() || mensaje.getContenido() == null) {
                redirectAttributes.addFlashAttribute("message",
                        "No se ha podido responder la PQRS, el mensaje no puede estar vacío");
                return "redirect:/admin/pqrs";
            }
            var pqrs = pqrsService.getPqrsById(pqrsId);
            pqrs.setFechaActualizacion(LocalDateTime.now());
            var ep = estadoPqrsService.getEstadoPqrsByNombre("Cerrada");
            pqrs.setEstadoPqrs(ep);
            pqrsService.editPqrs(pqrs);

            mensaje.setFecha(LocalDateTime.now());
            mensaje.setPqrs(pqrs);
            mensaje.setUsuario(usuarioService.getUsuarioByUsername(auth.getName()));
            mensajeService.addMensaje(mensaje);
            redirectAttributes.addFlashAttribute("messageOK", "Respuesta enviada correctamente");
        } catch (Exception e) {
            System.out.println("PqrsController.postMethodName()" + e.getMessage());
            redirectAttributes.addFlashAttribute("message", "No se ha podido responder la PQRS");
        }

        return "redirect:/admin/pqrs";
    }

    private void actualizarEstadoYFechaPqrs(Pqrs pqrs) {
        log.debug("Actualizando estado y fecha de la PQRS con ID: {}", pqrs.getId());

        EstadoPqrs estadoPqrs = estadoPqrsService.getEstadoPqrsById(1L); // Estado "Abierta"
        pqrs.setEstadoPqrs(estadoPqrs);
        pqrs.setFechaActualizacion(LocalDateTime.now());

        pqrsService.editPqrs(pqrs);
        log.debug("Estado de PQRS actualizado a 'Abierta' y fecha de actualización registrada.");
    }

    private void prepararMensaje(Mensaje mensaje, Pqrs pqrs, Authentication auth) {
        log.debug("Preparando mensaje para la PQRS con ID: {} por el usuario: {}", pqrs.getId(), auth.getName());

        mensaje.setFecha(LocalDateTime.now());
        mensaje.setPqrs(pqrs);
        mensaje.setUsuario(usuarioService.getUsuarioByUsername(auth.getName()));

        log.debug("Mensaje preparado con la fecha actual y usuario autenticado.");
    }

}
