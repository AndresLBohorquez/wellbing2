package com.devalb.wellbing2.util;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.entity.WellPoints;
import com.devalb.wellbing2.service.AccionWellPointsService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.WellPointsService;

@Service
public class WellPointsUtil {

    @Autowired
    private AccionWellPointsService accionWellPointsService;

    @Autowired
    private WellPointsService wellPointsService;

    @Autowired
    private UsuarioService usuarioService;

    public void quitarWellPoints(Usuario usuario, String mensaje) {
        var usuAdmin = usuarioService.getUsuarioByUsername("WellBing-Admin");
        WellPoints wellPoints = new WellPoints();

        usuAdmin.setWellPoints(usuAdmin.getWellPoints() + usuario.getWellPoints());
        usuarioService.editUsuario(usuAdmin);
        usuario.setWellPoints(0.0);
        usuarioService.editUsuario(usuario);

        wellPoints.setAccion(accionWellPointsService.getAccionWellPointsByNombre("Retirar"));
        wellPoints.setCantidad(usuario.getWellPoints());
        wellPoints.setDescripcion(mensaje);
        wellPoints.setTotal(usuAdmin.getWellPoints());
        wellPoints.setFecha(LocalDate.now());
        wellPoints.setUsuario(usuario);
        wellPointsService.addWellPoints(wellPoints);

    }

    public void pasarWellPoints(Usuario usuarioDonante, Usuario usuarioReceptor, double cantidad) {
        WellPoints wellPoints = new WellPoints();
        wellPoints.setCantidad(cantidad);
        wellPoints.setDescripcion("Transferencia de puntos desde usuario " + usuarioDonante.getUsername());
        wellPoints.setFecha(LocalDate.now());
        wellPoints.setTotal(usuarioReceptor.getWellPoints() + cantidad);

        var aw = accionWellPointsService.getAccionWellPointsByNombre("Adicionar");
        wellPoints.setAccion(aw);
        wellPoints.setUsuario(usuarioReceptor);
        wellPointsService.addWellPoints(wellPoints);

        WellPoints wellPoints2 = new WellPoints();
        wellPoints2.setCantidad(cantidad);
        wellPoints2.setDescripcion("Transferencia de puntos hacia usuario " + usuarioReceptor.getUsername());
        wellPoints2.setFecha(LocalDate.now());
        wellPoints2.setTotal(usuarioReceptor.getWellPoints() + cantidad);

        var aw2 = accionWellPointsService.getAccionWellPointsByNombre("Retirar");
        wellPoints2.setAccion(aw2);
        wellPoints2.setUsuario(usuarioDonante);
        wellPointsService.addWellPoints(wellPoints2);

        usuarioDonante.setWellPoints(usuarioDonante.getWellPoints() - cantidad);
        usuarioReceptor.setWellPoints(usuarioReceptor.getWellPoints() + cantidad);

        usuarioService.editUsuario(usuarioDonante);
        usuarioService.editUsuario(usuarioReceptor);
    }

    public void gananciaWellPoints(Usuario usuario, double cantidad, int hijosActivos) {
        if (cantidad > 0) {
            WellPoints wellPoints = new WellPoints();
            wellPoints.setCantidad(cantidad);
            wellPoints.setDescripcion("Ganancia por " + hijosActivos + "  hijos activos");

            var aw = accionWellPointsService.getAccionWellPointsByNombre("Adicionar");
            wellPoints.setAccion(aw);

            wellPoints.setFecha(LocalDate.now());
            wellPoints.setTotal(usuario.getWellPoints() + cantidad);
            wellPoints.setUsuario(usuario);

            wellPointsService.addWellPoints(wellPoints);

            usuario.setWellPoints(usuario.getWellPoints() + cantidad);
            usuarioService.editUsuario(usuario);
        }
    }

    public void wellPointPagos(Usuario usuario) {
        WellPoints wellPoints = new WellPoints();
        wellPoints.setCantidad(usuario.getWellPoints());
        wellPoints.setDescripcion("Retiro generado por pago mensual");

        var aw = accionWellPointsService.getAccionWellPointsByNombre("Retirar");
        wellPoints.setAccion(aw);

        wellPoints.setFecha(LocalDate.now());
        wellPoints.setTotal(0.0);
        wellPoints.setUsuario(usuario);

        wellPointsService.addWellPoints(wellPoints);

        usuario.setWellPoints(0.0);
        usuarioService.editUsuario(usuario);
    }

}
