package com.devalb.wellbing2.controller;

import java.time.LocalDate;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.Rol;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.entity.WellPoints;
import com.devalb.wellbing2.service.AccionWellPointsService;
import com.devalb.wellbing2.service.ActivacionService;
import com.devalb.wellbing2.service.EstadoUsuarioService;
import com.devalb.wellbing2.service.OrdenService;
import com.devalb.wellbing2.service.RolService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;
import com.devalb.wellbing2.service.WellPointsService;

import io.micrometer.common.util.StringUtils;
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
            usuario.setPassword(ENCODER.encode(usuarioForm.getPassword()));
            usuario.setRoles(usuarioForm.getRoles());
            usuario.setVisible(true);
            if (usuarioForm.getEstadoUsuario().getNombre().equals("Eliminado")) {
                usuario.setVisible(false);
            }
            usuario.setEstadoUsuario(usuarioForm.getEstadoUsuario());
            usuario.setFechaRegistro(usuarioActual.getFechaRegistro());
            usuario.setInactivo(usuarioActual.getInactivo());

            usuario.setWellPoints(usuarioActual.getWellPoints());
            if (isAdmin) {
                if (!usuarioActual.getWellPoints().equals(usuarioForm.getWellPoints())) {
                    System.out.println(usuarioActual.getWellPoints());
                    System.out.println(usuarioForm.getWellPoints());
                    editarWellPoints(usuario, usuarioForm.getWellPoints(), usuLog.getCodigoUsuario());
                }
                usuario.setWellPoints(usuarioForm.getWellPoints());
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

    

    @GetMapping("/admin/activaciones")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String goToActivaciones(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        return "admin/activaciones";
    }

    @GetMapping("/admin/equipo")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Tesorero')")
    public String goToEquipo(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        return "admin/equipo";
    }

    @GetMapping("/admin/ordenes")
    @PreAuthorize("hasAnyAuthority('Admin', 'Domiciliario')")
    public String goToOrdenes(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        return "admin/ordenes";
    }

    @GetMapping("/admin/pagos")
    @PreAuthorize("hasAnyAuthority('Admin', 'Tesorero')")
    public String goToPagos(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        return "admin/pagos";
    }

    @GetMapping("/admin/pqrs")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario')")
    public String goToPqrs(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        return "admin/pqrs";
    }

    @GetMapping("/admin/wellpoints")
    @PreAuthorize("hasAuthority('Admin')")
    public String goTowellpoints(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
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
}
