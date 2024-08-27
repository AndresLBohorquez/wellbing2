package com.devalb.wellbing2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.devalb.wellbing2.service.CategoriaService;
import com.devalb.wellbing2.service.ProductoService;
import com.devalb.wellbing2.service.UsuarioService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String goToIndex(Model model, Authentication auth) {
        model.addAttribute("listaProductosTop", productoService.getProductos());
        if (auth != null) {
            model.addAttribute("usuLog", usuarioService.getUsuarioByUsername(auth.getName()));
        }
        return "index";
    }

    @GetMapping("/login")
    public String goToLogin(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, Model model) {
        if (error != null) {
            model.addAttribute("messageKO", "Nombre de usuario o contraseña incorrectos, o el usuario no está activo.");
        }
        if (logout != null) {
            model.addAttribute("messageOK", "Te has desconectado correctamente.");
        }
        return "login";
    }

    @GetMapping("/login?error=true")
    public String goToLoginError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("messageKO", "Producto eliminado correctamente");
        return "login";
    }

    @GetMapping("/signup")
    public String goToRegister() {
        return "register";
    }

    @GetMapping("/productos")
    public String goToProductos(Model model) {
        model.addAttribute("maxPrecio", productoService.getMaxPrecioProducto());
        model.addAttribute("listaCategorias", categoriaService.getCategorias());
        model.addAttribute("listaProductos", productoService.getProductos());

        return "productos";
    }

    @GetMapping("/nosotros")
    public String goToNosotros() {
        return "nosotros";
    }

    @GetMapping("/recuperar-pass")
    public String goToRecuperarPass() {
        return "recuperar-pass";
    }

    @GetMapping("/producto_detalle")
    public String goToProductoDetalle() {
        return "producto_detalle";
    }

    @GetMapping("/politicas_privacidad")
    public String goToPoliticasPrivacidad() {
        return "politicas_privacidad";
    }

    @GetMapping("/aviso_legal")
    public String goToAvisoLegal() {
        return "aviso_legal";
    }

    @GetMapping("/terminos_condiciones")
    public String goToTerminosCondiciones() {
        return "terminos_condiciones";
    }

    @GetMapping("/usuario")
    public String getMethodName(Model model, Authentication auth) {
        model.addAttribute("usuLog", usuarioService.getUsuarioByUsername((auth.getName())));
        return "usuario/perfil";
    }

    @GetMapping("/usuario/prueba")
    public String goToPrueba() {
        return "usuario/prueba";
    }

    @GetMapping("/carrito")
    public String goToCarrito() {
        return "carrito";
    }

}
