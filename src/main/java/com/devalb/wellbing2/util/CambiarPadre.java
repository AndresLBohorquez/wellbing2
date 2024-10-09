package com.devalb.wellbing2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Equipo;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.EquipoService;
import com.devalb.wellbing2.service.UsuarioService;

@Service
public class CambiarPadre {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EquipoService equipoService;

    public void cambiarPadre(Usuario usuario) {
        var equipoUsuario = equipoService.getEquipoByIdUsuario(usuario.getId());
        Usuario usuAdmin = usuarioService.getUsuarioByUsername("WellBing-Admin");

        for (Equipo equipo : equipoUsuario) {
            equipo.setUsuario(usuAdmin);
            equipoService.editEquipo(equipo);
        }
    }
}
