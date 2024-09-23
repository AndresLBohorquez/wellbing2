package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.devalb.wellbing2.entity.Carrito;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.CarritoService;
import com.devalb.wellbing2.service.ItemsOrdenService;
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
            model.addAttribute("listaRolesUsuario", usuario.getRoles());
            model.addAttribute("nombre", ft.primeraPalabra(usuario.getNombre()));
            model.addAttribute("apellido", ft.primeraPalabra(usuario.getApellido()));
        } catch (Exception e) {
            log.warn("ha ocurrido un error al cargar la vista");
            System.out.println("VistaService.cargarVistas()" + e.getMessage());
        }
    }

    @Override
    public void verTopProductos(Model model) {
        log.info("Iniciando la carga de los productos más vendidos.");

        try {
            List<Object[]> productosMasVendidos = itemsOrdenService.obtenerTop10ProductosMasVendidos();
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

}
