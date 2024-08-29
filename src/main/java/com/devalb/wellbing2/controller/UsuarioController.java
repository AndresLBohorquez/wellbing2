package com.devalb.wellbing2.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.Rol;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.EstadoUsuarioService;
import com.devalb.wellbing2.service.RolService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.util.CrearCodigo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EstadoUsuarioService estadoUsuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private CrearCodigo crearCodigo;

    @Autowired
    private final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(4);

    @GetMapping("/signup")
    public String goToRegister(Model model) {
        log.info("Accediendo a la página de registro.");
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/signup")
    public String registrarUsuario(Usuario usuario, Model model, RedirectAttributes redirectAttributes) {
        log.info("Iniciando proceso de registro para el usuario: {}", usuario.getUsername());

        // Validación de email o username duplicados
        if (isUsuarioExistente(usuario)) {
            log.warn("El correo electrónico {} o el nombre de usuario {} ya están en uso.",
                    usuario.getEmail(), usuario.getUsername());
            model.addAttribute("messageKO", "El correo electrónico o nombre de usuario ya está en uso.");
            return "register";
        }

        // Preparar el nuevo usuario
        prepararNuevoUsuario(usuario);

        // Guardar el usuario
        usuarioService.addUsuario(usuario);
        log.info("Usuario {} registrado exitosamente.", usuario.getUsername());

        // Redirigir con mensaje de éxito
        redirectAttributes.addFlashAttribute("messageOK", "Registro exitoso. Ahora puedes iniciar sesión.");
        return "redirect:/login";
    }

    private boolean isUsuarioExistente(Usuario usuario) {
        return usuarioService.getUsuarioByEmail(usuario.getEmail()) != null ||
                usuarioService.getUsuarioByUsername(usuario.getUsername()) != null;
    }

    private void prepararNuevoUsuario(Usuario usuario) {
        // Generar código de usuario único
        String codigoUsuario = crearCodigo.generarCodigo(usuarioService.getUsuarios().stream()
                .map(Usuario::getCodigoUsuario).toList());
        usuario.setCodigoUsuario(codigoUsuario);
        log.info("Código de usuario generado: {}", codigoUsuario);

        // Establecer otros campos
        usuario.setPassword(ENCODER.encode(usuario.getPassword()));
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setVisible(true);
        usuario.setInactivo(0);
        usuario.setWellPoints(0.0);
        usuario.setEstadoUsuario(estadoUsuarioService.getEstadoUsuarioByNombre("Activo"));

        // Asignar rol de usuario
        List<Rol> rolUsuario = new ArrayList<>();
        rolUsuario.add(rolService.getRolByNombre("Usuario"));
        usuario.setRoles(rolUsuario);
    }
}
