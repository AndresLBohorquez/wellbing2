package com.devalb.wellbing2.util;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.devalb.wellbing2.entity.Activacion;
import com.devalb.wellbing2.entity.Equipo;
import com.devalb.wellbing2.entity.EstadoActivacion;
import com.devalb.wellbing2.entity.EstadoPagoMensual;
import com.devalb.wellbing2.entity.EstadoUsuario;
import com.devalb.wellbing2.entity.PagoMensual;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.ActivacionService;
import com.devalb.wellbing2.service.EquipoService;
import com.devalb.wellbing2.service.EstadoActivacionService;
import com.devalb.wellbing2.service.EstadoPagoMensualService;
import com.devalb.wellbing2.service.EstadoUsuarioService;
import com.devalb.wellbing2.service.PagoMensualService;
import com.devalb.wellbing2.service.UsuarioService;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScheduledTasks {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ActivacionService activacionService;

    @Autowired
    private EstadoUsuarioService estadoUsuarioService;

    @Autowired
    private EstadoActivacionService estadoActivacionService;

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private PlantillaNotificacion pn;

    @Autowired
    private CambiarPadre cPadre;

    @Autowired
    private WellPointsUtil wpu;

    @Autowired
    private PagoMensualService pagoMensualService;

    @Autowired
    private EstadoPagoMensualService estadoPagoMensualService;

    // ejecutar todos los días 27 de cada mes a las 08:00:00
    @Scheduled(cron = "0 0 8 27 * ?", zone = "America/Bogota")
    public void desactivarUsuariosInactivos() {
        log.info("Iniciando tarea programada: Desactivar usuarios inactivos.");

        Usuario usuAdmin = usuarioService.getUsuarioByUsername("WellBing-Admin");

        try {
            log.info("Obteniendo lista de usuarios activos.");
            var usuariosActivos = usuarioService.getUsuariosVisibles();
            List<Activacion> listaUltimaActivacion = new ArrayList<>();
            List<Usuario> listaInactivos = new ArrayList<>();

            log.debug("Iterando sobre la lista de usuarios activos.");
            for (Usuario user : usuariosActivos) {
                if (activacionService.getUltimActivacion(user.getId()) == null) {
                    log.debug("Usuario {} no tiene activaciones previas.", user.getUsername());
                    Activacion nuevaAct = new Activacion();
                    nuevaAct.setFechaFin(LocalDate.of(1900, 1, 1));
                    nuevaAct.setUsuario(user);
                    listaUltimaActivacion.add(nuevaAct);
                } else {
                    listaUltimaActivacion.add(activacionService.getUltimActivacion(user.getId()));
                }
            }

            log.debug("Identificando usuarios inactivos.");
            for (Activacion act : listaUltimaActivacion) {
                if (act.getFechaFin().isBefore(LocalDate.now())) {
                    listaInactivos.add(act.getUsuario());
                }
            }

            log.info("Usuarios inactivos detectados: {}", listaInactivos.size());
            for (Usuario usu : listaInactivos) {
                log.debug("Procesando usuario inactivo: {}", usu.getUsername());

                if (usu.getRoles().size() > 1) {
                    if (usu.getInactivo() < 2) {
                        log.info("Usuario {} recibe advertencia de inactividad.", usu.getUsername());
                        usu.setInactivo(usu.getInactivo() + 1);
                        pn.enviarEmailInactividad(usu);
                    } else if (usu.getInactivo() == 2) {
                        log.info("Usuario {} ha sido baneado por inactividad.", usu.getUsername());
                        usu.setInactivo(usu.getInactivo() + 1);
                        usu.setVisible(false);
                        usu.setEmail("ban-" + usu.getEmail());
                        EstadoUsuario eu = estadoUsuarioService.getEstadoUsuarioByNombre("Baneado");
                        usuAdmin.setWellPoints(usuAdmin.getWellPoints() + usu.getWellPoints());
                        usu.setEstadoUsuario(eu);
                        wpu.quitarWellPoints(usu, "WellPoints retirados por motivo de baneo de usuario");
                        usu.setWellPoints(0.0);
                        cPadre.cambiarPadre(usu);
                        pn.enviarEmailBaneo(usu);
                    }
                    usuarioService.editUsuario(usu);
                    usuarioService.editUsuario(usuAdmin);
                }
            }
            log.info("Tarea finalizada: Usuarios inactivos editados.");
        } catch (Exception e) {
            log.error("Error en desactivarUsuariosInactivos: {}", e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?", zone = "America/Bogota")
    public void activarUsuarios() {
        log.info("Iniciando tarea programada: Activar usuarios al inicio de mes.");
        var listaUsuariosValidado = usuarioService.getUsuariosUltimaActivacionValidado();
        log.info("Usuarios validados obtenidos: {}", listaUsuariosValidado.size());

        for (Usuario usuario : listaUsuariosValidado) {
            log.debug("Procesando usuario: {}", usuario.getUsername());
            var act = activacionService.getUltimActivacion(usuario.getId());
            if (act.getFechaFin().getMonthValue() == LocalDate.now().getMonthValue()) {
                EstadoActivacion ea = estadoActivacionService.getEstadoActivacionByNombre("Activado");
                act.setEstadoActivacion(ea);
                activacionService.editActivacion(act);
                log.info("Usuario {} activado correctamente.", usuario.getUsername());
            }
        }
    }

    // ejecutar todos los días 27 de cada mes a las 08:30:00
    @Scheduled(cron = "0 30 8 27 * ?", zone = "America/Bogota")
    public void reporteMensual() throws MessagingException {
        log.info("Iniciando tarea programada: Generar reporte mensual.");
        var listaUsuariosActivos = usuarioService.getUsuariosUltimaActivacionOK();
        var usuAdmin = usuarioService.getUsuarioByUsername("WellBing-Admin");

        log.info("Usuarios activos obtenidos: {}", listaUsuariosActivos.size());

        for (Usuario usuario : listaUsuariosActivos) {
            log.debug("Procesando usuario para reporte mensual: {}", usuario.getUsername());

            var hijosActivos = 0;
            usuario.setUltimaActivacion(activacionService.getUltimActivacion(usuario.getId()));

            if (usuario.getUltimaActivacion().getFechaFin().getMonth().equals(LocalDate.now().getMonth())) {
                var equipo = equipoService.getEquipoByIdUsuario(usuario.getId());

                for (Equipo equi : equipo) {
                    var ultimaActHijo = activacionService.getUltimActivacion(equi.getIdHijo().getId());
                    if (ultimaActHijo != null
                            && ultimaActHijo.getFechaFin().getMonth().equals(LocalDate.now().getMonth())) {
                        hijosActivos++; // si el hijo tiene activación vigente
                    }
                }

                PagoMensual pMensual = new PagoMensual();
                pMensual.setUsuario(usuario);
                pMensual.setActivacion(usuario.getUltimaActivacion());
                pMensual.setCantidadHijos(hijosActivos);

                EstadoPagoMensual epm = estadoPagoMensualService.getEstadoPagoMensualByNombre("Pendiente");
                pMensual.setEstadoPagoMensual(epm);
                pMensual.setMes(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
                pMensual.setDescuento(calcularDescuentos(hijosActivos));
                pMensual.setGanancia(hijosActivos * 20000);

                wpu.gananciaWellPoints(usuario, pMensual.getGanancia(), hijosActivos);

                if (hijosActivos >= 11) {
                    Equipo equipoHijo = equipoService.getEquipoPorIdHijo(usuario.getId());
                    if (equipoHijo != null) {
                        Usuario usuarioPadre = equipoHijo.getUsuario();
                        wpu.pasarWellPoints(usuario, usuarioPadre, calcularDescuentos(hijosActivos));
                    } else {
                        wpu.pasarWellPoints(usuario, usuAdmin, calcularDescuentos(hijosActivos));
                    }
                }
                pMensual.setTotal(usuario.getWellPoints());
                pagoMensualService.addPagoMensual(pMensual);
                log.info("Reporte mensual generado para usuario: {}", usuario.getUsername());
                pn.enviarEmailReporteMensual(usuario, pMensual.getTotal());
                log.info("Email con reporte mensual enviado para usuario: {}", usuario.getUsername());
            }
        }
        log.info("Tarea finalizada: Reportes mensuales generados.");
    }

    public double calcularDescuentos(int hijosActivos) {
        log.debug("Calculando descuentos para {} hijos activos.", hijosActivos);
        if (hijosActivos >= 31) {
            return 60000;
        } else if (hijosActivos >= 21) {
            return 40000;
        } else if (hijosActivos >= 11) {
            return 20000;
        } else {
            return 0;
        }
    }
}