package com.devalb.wellbing2.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.Activacion;
import com.devalb.wellbing2.entity.Equipo;
import com.devalb.wellbing2.entity.MedioPago;
import com.devalb.wellbing2.entity.RedesUsuario;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.entity.WellPoints;
import com.devalb.wellbing2.service.AccionWellPointsService;
import com.devalb.wellbing2.service.ActivacionService;
import com.devalb.wellbing2.service.EquipoService;
import com.devalb.wellbing2.service.EstadoActivacionService;
import com.devalb.wellbing2.service.MedioPagoService;
import com.devalb.wellbing2.service.RedesService;
import com.devalb.wellbing2.service.RedesUsuarioService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;
import com.devalb.wellbing2.service.WellPointsService;
import com.devalb.wellbing2.service.Impl.UserService;
import com.devalb.wellbing2.util.MultipartFileEditor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PerfilUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MedioPagoService mediosPagoService;

    @Autowired
    private ActivacionService activacionService;

    @Autowired
    private RedesUsuarioService redesUsuarioService;

    @Autowired
    private RedesService redesService;

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private EstadoActivacionService estadoActivacionService;

    @Autowired
    private WellPointsService wellPointsService;

    @Autowired
    private AccionWellPointsService accionWellPointsService;

    @Autowired
    private final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(4);

    @Autowired
    private UserService userService;

    @Autowired
    private VistaService vService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, "comprobante", new MultipartFileEditor());
    }

    @GetMapping("/usuario")
    public String mostrarPerfil(Model model, Authentication auth) {
        String username = auth.getName();
        log.info("Acceso al perfil del usuario: {}", username);

        Usuario usuario = usuarioService.getUsuarioByUsername(username);
        if (usuario != null) {
            log.info("Usuario encontrado: {}", usuario.getUsername());

            // Agregar información adicional
            vService.cargarVistas(model, auth);
            model.addAttribute("mediosPago", mediosPagoService.getMedioPagoByIdUsuario(usuario.getId()));
            model.addAttribute("activacion", activacionService.getUltimActivacion(usuario.getId()));
            model.addAttribute("redesSociales", redesUsuarioService.getRedesUsuarioByUsuario(usuario.getId()));
            model.addAttribute("redes", redesService.getRedes());
            model.addAttribute("redesUsuario", new RedesUsuario());
            model.addAttribute("nuevoMedioPago", new MedioPago());
            model.addAttribute("nuevaActivacion", new Activacion());

            model.addAttribute("valorActivacion",
                    calcularValorActivacion(validarCantidadHijosActivos(usuario)));

        } else {
            log.warn("No se encontró usuario con el nombre: {}", username);
        }

        return "usuario/perfil";
    }

    @PostMapping("/usuario/editar")
    public String editarUsuario(@ModelAttribute("usuLog") Usuario usuarioModificado,
            BindingResult result,
            Authentication auth,
            Model model,
            RedirectAttributes redirectAttributes) {
        log.info("Solicitud de edición del perfil de usuario iniciada por: {}", auth.getName());

        if (result.hasErrors()) {
            log.warn("Errores en la validación de los datos de entrada: {}", result.getAllErrors());
            model.addAttribute("messageKO", "Error en la validación de los datos.");
            return "usuario/perfil";
        }

        String usernameActual = auth.getName();
        Usuario usuarioActual = usuarioService.getUsuarioByUsername(usernameActual);

        if (usuarioActual != null) {
            log.info("Usuario actual encontrado: {}", usernameActual);

            // Actualizar los campos modificados
            usuarioActual.setNombre(usuarioModificado.getNombre());
            usuarioActual.setApellido(usuarioModificado.getApellido());
            usuarioActual.setCelular(usuarioModificado.getCelular());
            usuarioActual.setDireccion(usuarioModificado.getDireccion());

            log.info("Datos básicos del usuario actualizados para: {}", usernameActual);

            boolean nombreUsuarioModificado = false;

            // Validar cambios en el nombre de usuario
            if (!usuarioModificado.getUsername().equals(usuarioActual.getUsername())) {
                if (usuarioModificado.getUsername().length() >= 7) {
                    usuarioActual.setUsername(usuarioModificado.getUsername());
                    log.info("Nombre de usuario actualizado de '{}' a '{}'", usernameActual,
                            usuarioModificado.getUsername());
                    nombreUsuarioModificado = true;
                } else {
                    log.warn("Intento de actualizar el nombre de usuario fallido: longitud insuficiente.");
                    model.addAttribute("messageKO", "El nombre de usuario debe tener al menos 7 caracteres.");
                    return "usuario/perfil";
                }
            }

            // Validar cambios en la contraseña
            if (usuarioModificado.getPassword() != null && !usuarioModificado.getPassword().isEmpty()) {
                if (usuarioModificado.getPassword().length() >= 6) {
                    usuarioActual.setPassword(ENCODER.encode(usuarioModificado.getPassword()));
                    log.info("Contraseña actualizada para el usuario: {}", usernameActual);
                } else {
                    log.warn("Intento de actualización de contraseña fallido: longitud insuficiente.");
                    model.addAttribute("messageKO", "La contraseña debe tener al menos 6 caracteres.");
                    return "usuario/perfil";
                }
            }

            // Guardar los cambios en la base de datos
            usuarioService.editUsuario(usuarioActual);
            log.info("Usuario '{}' actualizado correctamente en la base de datos.", usernameActual);

            // Si el nombre de usuario fue modificado, actualizar la autenticación en la
            // sesión
            if (nombreUsuarioModificado) {
                actualizarAutenticacion(usuarioActual);
                log.info("Autenticación de usuario actualizada en la sesión.");
            }

            redirectAttributes.addFlashAttribute("messageOK", "Datos actualizados correctamente.");
        } else {
            log.error("No se encontró el usuario '{}' para actualizar.", usernameActual);
            model.addAttribute("messageKO", "No se pudo actualizar la información del usuario.");
            return "usuario/perfil";
        }

        // Redirigir al perfil para mostrar el mensaje de éxito
        return "redirect:/usuario";
    }

    private void actualizarAutenticacion(Usuario usuarioActual) {
        // Recargar los detalles del usuario actualizado
        UserDetails userDetails = userService.loadUserByUsername(usuarioActual.getUsername());

        // Crear una nueva autenticación con el nuevo nombre de usuario
        Authentication nuevaAutenticacion = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());

        // Reemplazar la autenticación actual en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(nuevaAutenticacion);
    }

    @PostMapping("/usuario/agregarRedSocial")
    public String agregarRedSocial(
            @ModelAttribute("redesUsuario") RedesUsuario redesUsuario,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        String username = auth.getName();
        Usuario usuario = usuarioService.getUsuarioByUsername(username);

        if (usuario == null) {
            log.warn("No se encontró usuario con el nombre: {}", username);
            redirectAttributes.addFlashAttribute("messageKO", "Error: Usuario no encontrado.");
            return "redirect:/usuario";
        }

        // Verificar si el usuario ya tiene 4 redes sociales registradas
        List<RedesUsuario> redesUsuarioList = redesUsuarioService.getRedesUsuarioByUsuario(usuario.getId());
        if (redesUsuarioList.size() >= 4) {
            log.warn("El usuario {} ha alcanzado el límite de 4 redes sociales", username);
            redirectAttributes.addFlashAttribute("messageKO", "No puedes registrar más de 4 redes sociales.");
            return "redirect:/usuario";
        }

        // Asignar el usuario al objeto RedesUsuario recibido
        redesUsuario.setUsuario(usuario);

        try {
            redesUsuarioService.addRedesUsuario(redesUsuario);
            log.info("Red social {} agregada para el usuario {}", redesUsuario.getRedes().getNombre(), username);
            redirectAttributes.addFlashAttribute("messageOK", "Red social agregada exitosamente.");
        } catch (Exception e) {
            log.error("Error al agregar la red social para el usuario {}", username, e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al agregar la red social.");
        }

        return "redirect:/usuario";
    }

    @PostMapping("/usuario/agregarMedioPago")
    public String agregarMedioPago(@ModelAttribute("nuevoMedioPago") MedioPago medioPago, Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {
            String username = auth.getName();
            Usuario usuario = usuarioService.getUsuarioByUsername(username);

            if (usuario == null) {
                log.warn("Usuario no encontrado al intentar agregar medio de pago");
                redirectAttributes.addFlashAttribute("messageKO", "Usuario no encontrado.");
                return "redirect:/usuario";
            }

            // Verificar el número máximo de medios de pago
            if (mediosPagoService.getMedioPagoByIdUsuario(usuario.getId()).size() >= 2) {
                log.warn("El usuario {} ya tiene el número máximo de medios de pago", username);
                redirectAttributes.addFlashAttribute("messageKO", "Ya tienes el número máximo de medios de pago.");
                return "redirect:/usuario";
            }

            medioPago.setUsuario(usuario);
            mediosPagoService.addMedioPago(medioPago);
            log.info("Medio de pago agregado exitosamente para el usuario: {}", username);
            redirectAttributes.addFlashAttribute("messageOK", "Medio de pago agregado exitosamente.");
            return "redirect:/usuario";
        } catch (Exception e) {
            log.error("Error al agregar medio de pago", e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al agregar medio de pago.");
            return "redirect:/usuario";
        }
    }

    /* +-+-+-+-+-+-+-+++++++++-+-+-+-+-+-+-+-+-+-+-+-+ */

    @PostMapping("/usuario/agregarActivacion")
    public String agregarActivacion(@ModelAttribute("nuevaActivacion") Activacion activacion,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {
            String username = auth.getName();
            Usuario usuario = usuarioService.getUsuarioByUsername(username);

            if (usuario == null) {
                log.warn("Usuario no encontrado al intentar agregar activación");
                redirectAttributes.addFlashAttribute("messageKO", "Usuario no encontrado.");
                return "redirect:/usuario";
            }

            // Validar fecha actual
            LocalDate fechaActual = LocalDate.now();
            if (fechaActual.getDayOfMonth() > 25) {
                log.warn("Intento de agregar activación fuera del período permitido");
                redirectAttributes.addFlashAttribute("messageKO",
                        "Solo puedes agregar una activación durante los primeros 25 días del mes.");
                return "redirect:/usuario";
            }

            // Validar última activación
            Activacion ultimaActivacion = activacionService.getUltimActivacion(usuario.getId());
            if (ultimaActivacion != null && ultimaActivacion.getFechaFin().isAfter(fechaActual)) {
                log.warn("El usuario {} ya tiene una activación vigente", username);
                redirectAttributes.addFlashAttribute("messageKO", "Ya tienes una activación vigente.");
                return "redirect:/usuario";
            }

            validarCantidadHijosActivos(usuario);

            double valorActivacion = calcularValorActivacion(validarCantidadHijosActivos(usuario));

            String nombreMes = fechaActual.getMonth().name().toLowerCase();
            String nombreComprobante;

            if (usuario.getWellPoints() >= valorActivacion) {
                // Si el usuario tiene suficientes WellPoints, no es necesario subir comprobante
                nombreComprobante = "WellPoints_" + usuario.getId() + "_" + nombreMes + ".jpg";
                activacion.setComprobante(nombreComprobante);

                // Si se sube un archivo, se ignora
                MultipartFile archivo = activacion.getComprobanteFile();
                if (archivo != null && !archivo.isEmpty()) {
                    log.warn("No se requiere archivo de comprobante ya que tienes suficientes WellPoints.");
                }

                // Actualizar WellPoints
                actualizarWellPoints(usuario, valorActivacion, fechaActual, activacion);
            } else {
                // Verificar si se ha proporcionado un archivo comprobante
                MultipartFile archivo = activacion.getComprobanteFile();
                if (archivo == null || archivo.isEmpty()) {
                    log.warn(
                            "El archivo de comprobante es obligatorio ya que el valor de la activación es mayor a los WellPoints.");
                    redirectAttributes.addFlashAttribute("messageKO", "El archivo de comprobante es obligatorio.");
                    return "redirect:/usuario";
                }

                // Guardar el archivo y asignar el nombre
                nombreComprobante = "activacion_" + usuario.getId() + "_" + nombreMes + ".jpg";
                guardarArchivo(archivo, nombreComprobante);
                activacion.setComprobante(nombreComprobante);
            }

            // Configurar fechas de activación
            if (ultimaActivacion == null) {
                activacion.setFechaFin(fechaActual.withDayOfMonth(fechaActual.lengthOfMonth()).plusMonths(1));
            } else {
                activacion.setFechaFin(fechaActual.withDayOfMonth(fechaActual.lengthOfMonth()));
            }

            activacion.setFecha(fechaActual);
            activacion.setUsuario(usuario);
            activacion.setEstadoActivacion(estadoActivacionService.getEstadoActivacionByNombre("Pre Activado"));
            activacionService.addActivacion(activacion);

            log.info("Activación creada exitosamente para el usuario: {}", username);
            redirectAttributes.addFlashAttribute("messageOK", "Activación creada exitosamente.");
            return "redirect:/usuario";
        } catch (Exception e) {
            log.error("Error al agregar activación", e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al agregar activación.");
            return "redirect:/usuario";
        }
    }

    private double calcularValorActivacion(long cantidadHijos) {
        if (cantidadHijos >= 31) {
            return 95000;
        } else if (cantidadHijos >= 21) {
            return 75000;
        } else if (cantidadHijos >= 11) {
            return 55000;
        } else {
            return 35000;
        }
    }

    private void actualizarWellPoints(Usuario usuario, double valor, LocalDate fechaActual, Activacion activacion) {
        WellPoints wellPoints = new WellPoints();
        wellPoints.setUsuario(usuario);
        wellPoints.setAccion(accionWellPointsService.getAccionWellPointsByNombre("Retirar"));
        wellPoints.setDescripcion("Retiro para cubrir el valor de la activación.");
        wellPoints.setCantidad(valor);
        wellPoints.setTotal(usuario.getWellPoints() - valor);
        wellPoints.setFecha(fechaActual);
        WellPoints savedWellPoints = wellPointsService.addWellPoints(wellPoints);
        activacion.setWellPoints(savedWellPoints);

        usuario.setWellPoints(usuario.getWellPoints() - valor);
        usuarioService.addUsuario(usuario);
    }

    private void guardarArchivo(MultipartFile archivo, String nombreArchivo) throws IOException {
        Path ruta = Paths.get("src/main/resources/static/images/activaciones/" + nombreArchivo);
        Files.createDirectories(ruta.getParent());
        Files.write(ruta, archivo.getBytes());
    }

    public int validarCantidadHijosActivos(Usuario usuario) {
        // Validar cantidad de hijos
        var hijosActivos = 0;
        var equipo = equipoService.getEquipoByIdUsuario(usuario.getId());

        for (Equipo equiH : equipo) {
            equiH.getIdHijo()
                    .setUltimaActivacion(activacionService.getUltimActivacion(equiH.getIdHijo().getId()));
            if (equiH.getIdHijo().getUltimaActivacion() != null) {
                if (equiH.getIdHijo().getUltimaActivacion().getEstadoActivacion().getNombre()
                        .equals("Pre Activado")
                        || equiH.getIdHijo().getUltimaActivacion().getEstadoActivacion().getNombre()
                                .equals("Activado")
                        || equiH.getIdHijo().getUltimaActivacion().getEstadoActivacion().getNombre()
                                .equals("Validado")) {
                    hijosActivos++;
                }
            }

        }
        return hijosActivos;

    }
}
