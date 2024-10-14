package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.devalb.wellbing2.entity.Carrito;
import com.devalb.wellbing2.entity.ItemsOrden;
import com.devalb.wellbing2.entity.Rol;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.CarritoService;
import com.devalb.wellbing2.service.ItemsOrdenService;
import com.devalb.wellbing2.service.OrdenService;
import com.devalb.wellbing2.service.PqrsService;
import com.devalb.wellbing2.service.ProductoService;
import com.devalb.wellbing2.service.UsuarioService;
import com.devalb.wellbing2.service.VistaService;
import com.devalb.wellbing2.util.FormatoTexto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VistaServiceImpl implements VistaService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ItemsOrdenService itemsOrdenService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private PqrsService pqrsService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private FormatoTexto ft;

    @Override
    public void cargarVistas(Model model, Authentication auth) {
        try {
            log.info("Accediendo a cargar vista");
            Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());
            List<Carrito> carritos = carritoService.getCarritoByUsuario(usuario.getId());
            int cantidadTotalProductos = 0;
            for (Carrito carrito : carritos) {
                cantidadTotalProductos += carrito.getCantidad();
            }
            model.addAttribute("cantidadCarrito", cantidadTotalProductos);
            model.addAttribute("usuLog", usuario);

            String rolPrincipal = obtenerRolPrincipal(usuario.getRoles());
            model.addAttribute("rolPrincipal", rolPrincipal);

            model.addAttribute("nombre", ft.primeraPalabra(usuario.getNombre()));
            model.addAttribute("apellido", ft.primeraPalabra(usuario.getApellido()));
        } catch (Exception e) {
            log.warn("ha ocurrido un error al cargar la vista");
            System.out.println("VistaService.cargarVistas()" + e.getMessage());
        }
    }

    @Override
    public void cargarVistasAdmin(Model model, Authentication auth) {
        try {
            log.info("Accediendo a cargar vista Admin");
            Usuario usuario = usuarioService.getUsuarioByUsername(auth.getName());

            model.addAttribute("usuLog", usuario);
            model.addAttribute("listaRolesUsuario", usuario.getRoles());
            String rolPrincipal = obtenerRolPrincipal(usuario.getRoles());
            model.addAttribute("rolPrincipal", rolPrincipal);
            model.addAttribute("nombre", ft.primeraPalabra(usuario.getNombre()));
            model.addAttribute("apellido", ft.primeraPalabra(usuario.getApellido()));

            // Datos Gráficas
            model.addAttribute("cantidadUsuarios", usuarioService.getUsuariosVisibles().size());
            model.addAttribute("registrosMensualesUsuarios", usuarioService.getUsuariosLast6Months());

            model.addAttribute("cantidadOrdenes", ordenService.getOrdenesVisibles().size());
            model.addAttribute("registrosMensualesOrdenes", ordenService.getOrdenesLast6Months());

            model.addAttribute("cantidadPqrs", pqrsService.getPqrs().size());
            model.addAttribute("registrosMensualesPqrs", pqrsService.getPqrsLast6Months());

            model.addAttribute("cantidadProductos", productoService.getProductosVisibles().size());
            model.addAttribute("registrosMensualesProductos", productoService.getProductosLast6Months());
        } catch (Exception e) {
            log.warn("ha ocurrido un error al cargar la vista");
            System.out.println("VistaService.cargarVistas()" + e.getMessage());
        }
    }

    @Override
    public void verTopProductos(Model model) {
        log.info("Iniciando la carga de los productos más vendidos.");

        try {
            List<ItemsOrden> productosMasVendidos = itemsOrdenService.obtenerTop10ProductosMasVendidos();
            log.debug("Cantidad de productos obtenidos: {}", productosMasVendidos.size());

            if (productosMasVendidos.isEmpty()) {
                log.warn("No se encontraron productos más vendidos.");
            } else {
                log.info("Productos más vendidos cargados correctamente.");
            }

            model.addAttribute("listaProductosTop", productosMasVendidos);
        } catch (Exception e) {
            log.error("Error al intentar cargar los productos más vendidos: {}", e.getMessage(), e);
            model.addAttribute("messageKO", "Error al intentar cargar los productos más vendidos");
        }
    }

    @Override
    public String obtenerRolPrincipal(List<Rol> roles) {
        for (Rol rol : roles) {
            if (rol.getNombre().equals("Admin")) {
                return "Admin";
            } else if (rol.getNombre().equals("Secretario")) {
                return "Secretario";
            } else if (rol.getNombre().equals("Tesorero")) {
                return "Tesorero";
            } else if (rol.getNombre().equals("Domiciliario")) {
                return "Domiciliario";
            } else {
                return "Usuario";
            }
        }
        return "";
    }
}
