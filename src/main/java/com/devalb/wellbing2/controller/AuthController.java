package com.devalb.wellbing2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.Email;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.util.CrearCodigo;
import com.devalb.wellbing2.util.PlantillaNotificacion;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PlantillaNotificacion pNotificacion;

    @Autowired
    private CrearCodigo crearCodigo;

    @Autowired
    private final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(4);

    @GetMapping("/login")
    public String goToLogin(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, Model model) {
        if (error != null) {
            log.warn("Intento de inicio de sesión fallido.");
            model.addAttribute("messageKO", "Nombre de usuario o contraseña incorrectos, o el usuario no está activo.");
        }
        if (logout != null) {
            log.info("Usuario desconectado exitosamente.");
            model.addAttribute("messageOK", "Te has desconectado correctamente.");
        }
        return "login";
    }

    @GetMapping("/recuperar-pass")
    public String goToRecuperarPass(Model model) {
        model.addAttribute("email", new Email());
        return "recuperar-pass";
    }

    @PostMapping("/recuperar-pass")
    public String recuperarPassword(Email email, Model model, RedirectAttributes redirectAttributes) {
        log.info("Método recuperarPassword invocado con email: {}", email.getDestinatario());

        // Verifica si el objeto email tiene datos válidos
        if (email.getDestinatario() == null || email.getDestinatario().isEmpty()) {
            log.warn("El destinatario del email está vacío.");
            model.addAttribute("messageKO", "Por favor, ingresa un correo electrónico válido.");
            return "recuperar-pass";
        }

        // Buscar al usuario por correo electrónico
        Usuario usuario = usuarioService.getUsuarioByEmail(email.getDestinatario());
        if (usuario == null) {
            log.warn("No se encontró ningún usuario con el correo: {}", email.getDestinatario());
            model.addAttribute("messageKO", "No se encontró ningún usuario con ese correo electrónico.");
            return "recuperar-pass";
        }

        // Generar una nueva contraseña
        String nuevaPassword = crearCodigo.generarPass();
        usuario.setPassword(ENCODER.encode(nuevaPassword));

        // Actualizar la contraseña del usuario
        usuarioService.editUsuario(usuario);
        log.info("Se ha generado una nueva contraseña para el usuario: {}", usuario.getUsername());

        // Enviar email de recuperación de contraseña
        try {
            pNotificacion.enviarEmailRecuperacionPassword(usuario, nuevaPassword);
            log.info("Email de recuperación de contraseña enviado a {}", usuario.getEmail());
        } catch (MessagingException e) {
            log.error("Error al enviar el correo de recuperación: {}", e.getMessage());
            model.addAttribute("messageKO", "Hubo un problema al enviar el correo de recuperación.");
            return "recuperar-pass";
        }

        redirectAttributes.addFlashAttribute("messageOK",
                "Se ha enviado una nueva contraseña a tu correo electrónico.");
        return "redirect:/login";
    }
}
