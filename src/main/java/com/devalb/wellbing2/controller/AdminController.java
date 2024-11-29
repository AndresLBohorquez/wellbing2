package com.devalb.wellbing2.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.Email;
import com.devalb.wellbing2.entity.EstadoPagoMensual;
import com.devalb.wellbing2.entity.PagoMensual;
import com.devalb.wellbing2.entity.Rol;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.entity.WellPoints;
import com.devalb.wellbing2.service.AccionWellPointsService;
import com.devalb.wellbing2.service.ActivacionService;
import com.devalb.wellbing2.service.EstadoPagoMensualService;
import com.devalb.wellbing2.service.EstadoUsuarioService;
import com.devalb.wellbing2.service.MedioPagoService;
import com.devalb.wellbing2.service.OrdenService;
import com.devalb.wellbing2.service.PagoMensualService;
import com.devalb.wellbing2.service.RedesUsuarioService;
import com.devalb.wellbing2.service.RolService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;
import com.devalb.wellbing2.service.WellPointsService;
import com.devalb.wellbing2.util.CambiarPadre;
import com.devalb.wellbing2.util.PlantillaNotificacion;
import com.devalb.wellbing2.util.WellPointsUtil;

import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AdminController {

    @Autowired
    private VistaService vService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private EstadoUsuarioService estadoUsuarioService;

    @Autowired
    private AccionWellPointsService accionWellPointsService;

    @Autowired
    private WellPointsService wellPointsService;

    @Autowired
    private ActivacionService activacionService;

    @Autowired
    private PagoMensualService pagoMensualService;

    @Autowired
    private EstadoPagoMensualService estadoPagoMensualService;

    @Autowired
    private RedesUsuarioService redesUsuarioService;

    @Autowired
    private MedioPagoService medioPagoService;

    @Autowired
    private PlantillaNotificacion pn;

    @Autowired
    private WellPointsUtil wpu;

    @Autowired
    private CambiarPadre cPadre;

    @Autowired
    private final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(4);

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('Admin')")
    public String goToAdmin(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        // Ingresos de los últimos 6 meses
        model.addAttribute("ingresosMensuales", ordenService.getIngresosLast6Months());

        return "admin/index";
    }

    @GetMapping("/admin/usuarios")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero', 'Domiciliario')")
    public String goToUsuarios(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("usuarios", usuarioService.getUsuarios());
        return "admin/usuarios";
    }

    @GetMapping("/admin/usuarios/detalle/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero', 'Domiciliario')")
    public String goToDetalleUsuario(@PathVariable Long id, Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        Usuario usuario = usuarioService.getUsuarioById(id);
        model.addAttribute("usuarioForm", usuario);
        model.addAttribute("roles", rolService.getRoles());
        model.addAttribute("listaEstadosUsuario", estadoUsuarioService.getEstadosUsuario());
        model.addAttribute("ultimaActivacion", activacionService.getUltimActivacion(usuario.getId()));
        model.addAttribute("redesSociales", redesUsuarioService.getRedesUsuarioByUsuario(id));
        model.addAttribute("mediosPago", medioPagoService.getMedioPagoByIdUsuario(id));
        return "admin/usuarios-detalle";
    }

    @GetMapping("/admin/usuarios/editar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero', 'Domiciliario')")
    public String goToEditarUsuario(@PathVariable Long id, Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        Usuario usuario = usuarioService.getUsuarioById(id);
        model.addAttribute("usuarioForm", usuario);
        model.addAttribute("roles", rolService.getRoles());
        model.addAttribute("listaEstadosUsuario", estadoUsuarioService.getEstadosUsuario());
        return "admin/usuarios-editar";
    }

    @PostMapping("/admin/usuarios/editar/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public String editarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuarioForm,
            BindingResult result, Model model, RedirectAttributes redirectAttributes, Authentication auth) {
        if (result.hasErrors()) {
            model.addAttribute("usuarioForm", usuarioForm);
            model.addAttribute("roles", rolService.getRoles());
            model.addAttribute("listaEstadosUsuario", estadoUsuarioService.getEstadosUsuario());
            return "admin/usuarios-editar";
        }

        Usuario usuarioActual = usuarioService.getUsuarioById(id);

        // Verificar si los campos del formulario están vacíos
        if (StringUtils.isEmpty(usuarioForm.getNombre())) {
            usuarioForm.setNombre(usuarioActual.getNombre());
        }
        if (StringUtils.isEmpty(usuarioForm.getApellido())) {
            usuarioForm.setApellido(usuarioActual.getApellido());
        }
        if (StringUtils.isEmpty(usuarioForm.getCelular())) {
            usuarioForm.setCelular(usuarioActual.getCelular());
        }
        if (StringUtils.isEmpty(usuarioForm.getCodigoUsuario())) {
            usuarioForm.setCodigoUsuario(usuarioActual.getCodigoUsuario());
        }
        if (StringUtils.isEmpty(usuarioForm.getUsername())) {
            usuarioForm.setUsername(usuarioActual.getUsername());
        }
        if (StringUtils.isEmpty(usuarioForm.getDireccion())) {
            usuarioForm.setDireccion(usuarioActual.getDireccion());
        }
        if (StringUtils.isEmpty(usuarioForm.getEmail())) {
            usuarioForm.setEmail(usuarioActual.getEmail());
        }
        if (StringUtils.isEmpty(usuarioForm.getPassword())) {
            usuarioForm.setPassword(usuarioActual.getPassword());
        } else {
            usuarioForm.setPassword(ENCODER.encode(usuarioForm.getPassword()));
        }

        // Verificar si los roles han cambiado
        List<Rol> rolesActuales = usuarioActual.getRoles();
        List<Long> rolesActualesIds = rolesActuales.stream().map(Rol::getId).collect(Collectors.toList());
        boolean rolesModified = !usuarioForm.getRoles().stream().map(Rol::getId).collect(Collectors.toList())
                .equals(rolesActualesIds);

        // Verificar si el usuario autenticado es Admin
        Usuario usuLog = usuarioService.getUsuarioByUsername(auth.getName());
        var rolPrincipal = vService.obtenerRolPrincipal(usuLog.getRoles());
        boolean isAdmin = rolPrincipal.equals("Admin");

        try {
            if (rolesModified && isAdmin) {
                // Modificar roles solo si el usuario autenticado es Admin
                List<Long> rolesIds = usuarioForm.getRoles().stream().map(Rol::getId).collect(Collectors.toList());
                usuarioForm.setRoles(rolService.getRolesByIds(rolesIds));
            } else if (rolesModified && !isAdmin) {
                // No modificar roles si el usuario autenticado no es Admin
                usuarioForm.setRoles(rolesActuales);
            }

            // Editar resto de los campos
            Usuario usuario = new Usuario();
            usuario.setId(usuarioForm.getId());
            usuario.setNombre(usuarioForm.getNombre());
            usuario.setApellido(usuarioForm.getApellido());
            usuario.setCelular(usuarioForm.getCelular());
            usuario.setCodigoUsuario(usuarioForm.getCodigoUsuario());
            usuario.setUsername(usuarioForm.getUsername());
            usuario.setDireccion(usuarioForm.getDireccion());
            usuario.setEmail(usuarioForm.getEmail());
            usuario.setPassword(usuarioForm.getPassword());
            usuario.setRoles(usuarioForm.getRoles());
            usuario.setVisible(true);
            usuario.setWellPoints(usuarioActual.getWellPoints());

            usuario.setEstadoUsuario(usuarioForm.getEstadoUsuario());
            usuario.setFechaRegistro(usuarioActual.getFechaRegistro());
            usuario.setInactivo(usuarioActual.getInactivo());

            if (isAdmin) {
                if (!usuarioActual.getWellPoints().equals(usuarioForm.getWellPoints())) {
                    System.out.println(usuarioActual.getWellPoints());
                    System.out.println(usuarioForm.getWellPoints());
                    editarWellPoints(usuario, usuarioForm.getWellPoints(), usuLog.getCodigoUsuario());
                }
                usuario.setWellPoints(usuarioForm.getWellPoints());
                if (usuarioForm.getEstadoUsuario().getNombre().equals("Eliminado")) {
                    usuario.setVisible(false);
                    wpu.quitarWellPoints(usuario, "WellPoints retirados por motivo de eliminación de usuario");
                    usuario.setWellPoints(0.0);
                    cPadre.cambiarPadre(usuario);
                }
                if (usuarioForm.getEstadoUsuario().getNombre().equals("Baneado")) {
                    usuario.setVisible(false);
                    wpu.quitarWellPoints(usuario, "WellPoints retirados por motivo de baneo de usuario");
                    pn.enviarEmailBaneo(usuario);
                    usuario.setWellPoints(0.0);
                    cPadre.cambiarPadre(usuario);
                }
            }

            usuarioService.editUsuario(usuario);
            redirectAttributes.addFlashAttribute("messageOK", "Usuario editado correctamente");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            log.error("Error al editar usuario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("messageKO", "Error al editar usuario");
            return "redirect:/admin/usuarios";
        }
    }

    @GetMapping("/admin/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var usu = usuarioService.getUsuarioById(id);
            usu.setEstadoUsuario(estadoUsuarioService.getEstadoUsuarioByNombre("Eliminado"));

            Usuario usuAdmin = usuarioService.getUsuarioByUsername("WellBing-Admin");
            usuAdmin.setWellPoints(usuAdmin.getWellPoints() + usu.getWellPoints());
            cPadre.cambiarPadre(usu);
            wpu.quitarWellPoints(usu, "WellPoints retirados por motivo de eliminación de usuario");

            usuarioService.editUsuario(usu);
            usuarioService.deleteUsuario(id);
            log.info("Usuario eliminado con éxito. ID: {}", id);
            redirectAttributes.addFlashAttribute("messageOK", "Usuario eliminado con éxito.");
        } catch (Exception e) {
            log.error("Error al eliminar el usuario. ID: {}", id, e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al eliminar el usuario.");
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/wellpoints")
    @PreAuthorize("hasAuthority('Admin')")
    public String goTowellpoints(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("listaWellPoints", wellPointsService.getWellPoints());
        return "admin/wellpoints";
    }

    private void editarWellPoints(Usuario usuario, double valor, String nombre) {
        WellPoints wellPoints = new WellPoints();
        wellPoints.setUsuario(usuario);
        if (usuario.getWellPoints() < valor) {
            wellPoints.setAccion(accionWellPointsService.getAccionWellPointsByNombre("Adicionar"));
            wellPoints.setCantidad(valor - usuario.getWellPoints());
        } else {
            wellPoints.setAccion(accionWellPointsService.getAccionWellPointsByNombre("Retirar"));
            wellPoints.setCantidad(usuario.getWellPoints() - valor);
        }

        wellPoints.setDescripcion("Modificación generada por " + nombre);

        wellPoints.setTotal(valor);
        wellPoints.setFecha(LocalDate.now());
        wellPointsService.addWellPoints(wellPoints);
    }

    @GetMapping("/admin/pago-mensual")
    @PreAuthorize("hasAuthority('Admin')")
    public String goToPagoMensual(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("listaPagosMensual", pagoMensualService.getPagosMensual());
        model.addAttribute("estados", estadoPagoMensualService.getEstadosPagoMensual());

        return "admin/pago-mensual";
    }

    @PostMapping("/admin/pago-mensual/actualizar")
    public String actualizarPagoMensual(@RequestParam("pagoMensualId") Long pagoMensualId,
            @RequestParam("comprobanteImagen") MultipartFile comprobanteImagen,
            @RequestParam("estadoPagoMensual") Long estadoId,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            PagoMensual pagoMensual = pagoMensualService.getPagoMensualById(pagoMensualId);
            EstadoPagoMensual nuevoEstado = estadoPagoMensualService.getEstadoPagoById(estadoId);

            // Guardar el archivo en la carpeta /app/images/pagoMensual
            String mes = pagoMensual.getMes().toLowerCase();
            String fileName = mes + "_" + pagoMensual.getUsuario().getId() + ".png";
            Path path = Paths.get("/app/images/pagoMensual/" + fileName);

            // Registrar la acción de subir el archivo en los logs
            log.info("Guardando archivo comprobante: {} para el PagoMensual ID: {}", fileName, pagoMensualId);

            Files.write(path, comprobanteImagen.getBytes());

            // Actualizar la información del pago mensual
            pagoMensual.setComprobante(fileName);
            pagoMensual.setEstadoPagoMensual(nuevoEstado);
            pagoMensualService.addPagoMensual(pagoMensual);

            // Quitar WellPoints del usuario debido al pago generado
            wpu.wellPointPagos(pagoMensual.getUsuario());

            // Registrar la actualización exitosa en los logs
            log.info("PagoMensual ID: {} actualizado exitosamente con nuevo estado: {}", pagoMensualId,
                    nuevoEstado.getNombre());

            // Mensaje de éxito para redirigir
            redirectAttributes.addFlashAttribute("messageOK", "Pago mensual actualizado exitosamente.");

            return "redirect:/admin/pago-mensual";
        } catch (IOException e) {
            // Registrar el error en los logs
            log.error("Error al subir el comprobante para PagoMensual ID: {}", pagoMensualId, e);

            // Mensaje de error para redirigir
            redirectAttributes.addFlashAttribute("messageKO", "Hubo un error al subir el comprobante.");

            return "redirect:/admin/pago-mensual";
        } catch (Exception e) {
            // Registrar errores generales en los logs
            log.error("Error inesperado al actualizar el PagoMensual ID: {}", pagoMensualId, e);

            // Mensaje de error genérico para redirigir
            redirectAttributes.addFlashAttribute("messageKO",
                    "Ocurrió un error inesperado al actualizar el pago mensual.");

            return "redirect:/admin/pago-mensual";
        }
    }

    @PostMapping("/admin/pago-mensual/estado-pendiente")
    public String cambiarEstadoPendiente(@RequestParam("pagoMensualId") Long pagoMensualId,
            RedirectAttributes redirectAttributes) {
        try {
            // Obtener el pago mensual por ID
            PagoMensual pagoMensual = pagoMensualService.getPagoMensualById(pagoMensualId);

            // Verifica si hay un comprobante y lo elimina
            if (pagoMensual.getComprobante() != null) {
                String filePath = "/app/images/pagoMensual/" + pagoMensual.getComprobante();
                Path path = Paths.get(filePath);
                Files.deleteIfExists(path); // Elimina el archivo si existe
            }

            // Obtener el estado "Pendiente"
            EstadoPagoMensual estadoPendiente = estadoPagoMensualService.getEstadoPagoMensualByNombre("Pendiente");

            // Actualizar el estado del pago mensual y eliminar el comprobante
            pagoMensual.setEstadoPagoMensual(estadoPendiente);
            pagoMensual.setComprobante(null); // Eliminar comprobante
            pagoMensualService.addPagoMensual(pagoMensual);

            // Agregar mensaje de éxito
            redirectAttributes.addFlashAttribute("messageOK",
                    "El estado se ha actualizado a Pendiente y se ha eliminado el comprobante.");
        } catch (Exception e) {
            // Agregar mensaje de error
            redirectAttributes.addFlashAttribute("messageKO", "Error al cambiar el estado del pago mensual.");
            e.printStackTrace();
        }

        return "redirect:/admin/pago-mensual"; // Redirigir a la lista de pagos
    }

    @GetMapping("/admin/email")
    public String goToEmail(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("email", new Email());
        return "admin/email";
    }

    @PostMapping("/admin/email")
    public String enviarEmail(@ModelAttribute Email email, @RequestParam String action,
            RedirectAttributes redirectAttributes) {
        try {
            if ("enviar".equals(action)) {
                // Obtener los correos del campo "para"
                String[] destinatarios = email.getDestinatario().split(",");
                for (String destinatario : destinatarios) {
                    // Trim para eliminar espacios en blanco
                    pn.enviarEmail(email, destinatario.trim());
                }
                log.info("Correos enviados a: {}", Arrays.toString(destinatarios));
                redirectAttributes.addFlashAttribute("messageOK", "Los correos han sido enviados correctamente.");
            } else if ("enviarTodos".equals(action)) {
                // Ignorar el campo "para" y enviar a todos los usuarios
                List<Usuario> usuarios = usuarioService.getUsuariosVisibles();
                for (Usuario usuario : usuarios) {
                    pn.enviarEmail(email, usuario.getEmail());
                }
                log.info("Correos enviados a todos los usuarios.");
                redirectAttributes.addFlashAttribute("messageOK",
                        "Los correos han sido enviados a todos los usuarios.");
            }
        } catch (MessagingException e) {
            log.error("Error al enviar el correo: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("messageKO",
                    "Hubo un error al enviar los correos. Inténtalo de nuevo más tarde.");
        } catch (Exception e) {
            log.error("Error inesperado: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("messageKO",
                    "Hubo un error inesperado. Inténtalo de nuevo más tarde.");
        }

        return "redirect:/admin/email";
    }

}
