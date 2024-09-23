package com.devalb.wellbing2.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.devalb.wellbing2.entity.Carrito;
import com.devalb.wellbing2.entity.EstadoOrden;
import com.devalb.wellbing2.entity.ItemsOrden;
import com.devalb.wellbing2.entity.Orden;
import com.devalb.wellbing2.entity.Producto;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.CarritoService;
import com.devalb.wellbing2.service.EstadoOrdenService;
import com.devalb.wellbing2.service.ItemsOrdenService;
import com.devalb.wellbing2.service.OrdenService;
import com.devalb.wellbing2.service.ProductoService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;
import com.devalb.wellbing2.util.PlantillaNotificacion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EstadoOrdenService estadoOrdenService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private ItemsOrdenService itemsOrdenService;

    @Autowired
    private VistaService vService;

    @Autowired
    private PlantillaNotificacion pNotificacion;

    @GetMapping()
    public String verCarrito(Authentication auth, Model model) {
        log.info("Obteniendo carrito de usuario {}", auth.getName());
        vService.cargarVistas(model, auth);

        Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
        log.info("Usuario autenticado: {}", usuario.getNombre());

        List<Carrito> carritos = carritoService.getCarritoByUsuario(usuario.getId());
        log.info("Carritos del usuario: {}", carritos.size());

        double totalCarrito = carritoService.calcularTotalCarrito(usuario.getId());
        log.info("Total del carrito: {}", totalCarrito);

        // Añadir los carritos al modelo para mostrarlos en la vista
        model.addAttribute("carritos", carritos);
        model.addAttribute("totalCarrito", totalCarrito);

        return "carrito/carrito";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam("productoId") Long productoId,
            @RequestParam("cantidad") int cantidad,
            Authentication auth, Model model, RedirectAttributes redirectAttributes) {
        log.info("Agregando producto {} al carrito de usuario {}", productoId, auth.getName());

        // Obtener el usuario actualmente autenticado
        Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
        log.info("Usuario autenticado: {}", usuario.getNombre());

        Producto producto = productoService.getProductoById(productoId);
        if (producto == null) {
            log.error("Producto no encontrado con ID {}", productoId);
            model.addAttribute("messageKO", "Producto no encontrado.");
            return "redirect:/productos";
        }

        // Buscar si ya existe el producto en el carrito del usuario
        List<Carrito> carritosUsuario = carritoService.getCarritoByUsuario(usuario.getId());
        Carrito carritoExistente = carritosUsuario.stream()
                .filter(c -> c.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);

        if (carritoExistente != null) {
            log.info("Producto ya existe en el carrito, aumentando cantidad");
            // Si el producto ya está en el carrito, aumentar la cantidad
            carritoExistente.setCantidad(carritoExistente.getCantidad() + cantidad);
            carritoService.editCarrito(carritoExistente);
        } else {
            log.info("Producto no existe en el carrito, agregando nuevo ítem");
            // Si el producto no está en el carrito, agregar un nuevo ítem al carrito
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setProducto(producto);
            nuevoCarrito.setUsuario(usuario);
            nuevoCarrito.setCantidad(cantidad);
            carritoService.addCarrito(nuevoCarrito);
        }
        redirectAttributes.addFlashAttribute("messageOK", "Producto agregado al carrito correctamente.");
        return "redirect:/productos";
    }

    @PostMapping("/actualizarCantidad")
    public String actualizarCantidad(@RequestParam("carritoId") Long carritoId,
            @RequestParam("cantidad") Integer cantidad, @RequestParam("action") String action,
            RedirectAttributes redirectAttributes) {
        log.info("Actualizando cantidad de carrito {} con acción {}", carritoId, action);

        Carrito carrito = carritoService.getCarritoById(carritoId);
        log.info("Carrito encontrado: {}", carrito);

        switch (action) {
            case "sumarCantidad":
                log.info("Aumentando cantidad de carrito");
                carrito.setCantidad(cantidad + 1);
                break;
            case "restarCantidad":
                log.info("Disminuyendo cantidad de carrito");
                carrito.setCantidad(cantidad - 1);
                if (carrito.getCantidad() == 0) {
                    log.info("Cantidad de carrito es 0, eliminando carrito");
                    eliminarDelCarrito(carrito.getId(), redirectAttributes);
                    return "redirect:/carrito";

                }
                break;
        }
        carritoService.editCarrito(carrito);
        log.info("Carrito actualizado correctamente");

        return "redirect:/carrito";
    }

    @PostMapping("/eliminar")
    public String eliminarDelCarrito(@RequestParam("carritoId") Long carritoId, RedirectAttributes redirectAttributes) {
        log.info("Eliminando carrito {}", carritoId);

        carritoService.deleteCarrito(carritoId);
        log.info("Carrito eliminado correctamente");

        redirectAttributes.addFlashAttribute("messageOK", "Producto eliminado del carrito correctamente.");

        return "redirect:/carrito";
    }

    @GetMapping("/checkout")
    public String goToCheckout(Model model, Authentication auth) {
        vService.cargarVistas(model, auth);
        Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
        List<Carrito> carritos = carritoService.getCarritoByUsuario(usuario.getId());
        model.addAttribute("carritos", carritos);
        model.addAttribute("usuario", usuario);

        double total = calcularTotalCarrito(carritos);
        model.addAttribute("total", total);

        return "carrito/checkout";
    }

    public double calcularTotalCarrito(List<Carrito> carritos) {
        double total = 0;
        for (Carrito carrito : carritos) {
            total += carrito.getProducto().getPrecio() * carrito.getCantidad();
        }
        return total;
    }

    @PostMapping("/createOrden")
    public RedirectView createOrden(@RequestParam("total") Double total,
            @RequestParam("usuarioId") Long usuarioId,
            @RequestParam("nombre") String nombre,
            @RequestParam("direccion") String direccion,
            @RequestParam("ciudad") String ciudad,
            @RequestParam("barrio") String barrio,
            RedirectAttributes redirectAttributes) {

        log.info("Creando orden para el usuario con ID {}", usuarioId);

        Usuario usuario = usuarioService.getUsuarioById(usuarioId);
        EstadoOrden estadoOrden = estadoOrdenService.getEstadoOrdenByNombre("Pendiente");

        Orden orden = new Orden();
        orden.setUsuario(usuario);
        orden.setEstadoOrden(estadoOrden);
        orden.setTotal(total);
        orden.setFechaCreacion(LocalDateTime.now());
        orden.setFechaActualizacion(LocalDateTime.now());
        orden.setVisible(true);
        orden.setNombre(nombre);
        orden.setDireccion(direccion);
        orden.setCiudad(ciudad);
        orden.setBarrio(barrio);

        ordenService.addOrden(orden);

        log.info("Orden creada correctamente");

        // Registrar items de la orden
        List<Carrito> carritoElements = carritoService.getCarritoByUsuario(usuarioId);
        for (Carrito carritoElement : carritoElements) {
            ItemsOrden itemsOrden = new ItemsOrden();
            itemsOrden.setOrden(orden);
            itemsOrden.setProducto(carritoElement.getProducto());
            itemsOrden.setCantidad(carritoElement.getCantidad());
            itemsOrdenService.addItemsOrden(itemsOrden);
        }

        log.info("Items de la orden registrados correctamente");

        // Borrar todos los elementos del carrito
        carritoService.deleteAllElements(usuario.getId());
        log.info("Carrito vaciado correctamente");

        // Enviar email con la información de la orden
        try {
            pNotificacion.enviarEmailDeConfirmacionOrden(usuario, orden, carritoElements);
        } catch (Exception e) {
            log.error("Error al enviar el correo de confirmación: {}", e.getMessage());
        }

        redirectAttributes.addFlashAttribute("messageOK", "La orden ha sido creada correctamente.");

        return new RedirectView("/usuario/ordenes", true);
    }
}