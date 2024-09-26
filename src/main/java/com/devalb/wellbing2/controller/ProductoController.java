package com.devalb.wellbing2.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devalb.wellbing2.entity.Producto;
import com.devalb.wellbing2.service.CategoriaService;
import com.devalb.wellbing2.service.ItemsOrdenService;
import com.devalb.wellbing2.service.ProductoService;
import com.devalb.wellbing2.service.VistaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProductoController {

    @Autowired
    private ItemsOrdenService itemsOrdenService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private VistaService vService;

    @GetMapping("/reportes/top-productos")
    public String verTopProductos(Model model) {
        List<Object[]> productosMasVendidos = itemsOrdenService.obtenerTop10ProductosMasVendidos();
        model.addAttribute("productosMasVendidos", productosMasVendidos);
        return "reportes/top-productos";
    }

    @GetMapping("/admin/productos")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public String goToProductos(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("productos", productoService.getProductos());
        return "admin/productos";
    }

    @GetMapping("/admin/productos/detalle/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public String goToDetalleProducto(@PathVariable Long id, Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("producto", productoService.getProductoById(id));
        return "admin/detalle-producto";
    }

    @GetMapping("/admin/productos/crear")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public String goToCrearProducto(Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("categorias", categoriaService.getCategorias());
        model.addAttribute("producto", new Producto());
        return "admin/productos-form";
    }

    @GetMapping("/admin/productos/editar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public String goToEditarProducto(@PathVariable Long id, Model model, Authentication auth) {
        vService.cargarVistasAdmin(model, auth);
        model.addAttribute("categorias", categoriaService.getCategorias());
        model.addAttribute("titulo", "Editar producto");
        model.addAttribute("producto", productoService.getProductoById(id));
        return "admin/productos-form";
    }

    @PostMapping("/admin/productos/guardar")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public String guardarProducto(@RequestParam(value = "id", required = false) Long id,
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio, @RequestParam("bono") Double bono,
            @RequestParam(value = "visible", required = false) Boolean visible,
            @RequestParam("categoria.id") Long categoriaId, Model model, Authentication auth,
            RedirectAttributes redirectAttributes) {
        vService.cargarVistasAdmin(model, auth);

        try {
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setBono(bono);
            producto.setCategoria(categoriaService.getCategoriaById(categoriaId));

            if (id != null) {
                producto.setId(id);
                producto.setFecha(productoService.getProductoById(id).getFecha());
                producto.setImagen(productoService.getProductoById(id).getImagen());
                if (visible == null) {
                    producto.setVisible(false);
                } else {
                    producto.setVisible(true);
                }

            } else {
                producto.setFecha(LocalDate.now());
                producto.setVisible(true);
            }

            if (id != null) {
                productoService.editProducto(producto);
                log.info("Producto editado correctamente");
                redirectAttributes.addFlashAttribute("messageOK", "Producto editado correctamente");
            } else {
                producto = productoService.addProducto(producto);
                log.info("Producto creado correctamente");
                redirectAttributes.addFlashAttribute("messageOK", "Producto creado correctamente");
            }

            if (!imagen.isEmpty()) {
                String nombreImagen = "producto_" + producto.getId() + ".png";
                producto.setImagen(nombreImagen);
                try {
                    byte[] bytes = imagen.getBytes();
                    Path ruta = Paths.get("src/main/resources/static/images/productos/" + nombreImagen);
                    if (Files.exists(ruta)) {
                        Files.delete(ruta);
                    }
                    Files.write(ruta, bytes);
                    log.info("Imagen subida correctamente");
                    redirectAttributes.addFlashAttribute("messageOK", "Imagen subida correctamente");
                } catch (IOException e) {
                    log.error("Error al subir la imagen", e);
                    redirectAttributes.addFlashAttribute("messageKO", "Error al subir la imagen");
                }
                productoService.editProducto(producto);
            } else if (id != null) {
                producto.setImagen(productoService.getProductoById(id).getImagen());
            }

            return "redirect:/admin/productos";
        } catch (Exception e) {
            log.error("Error al guardar el producto", e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al guardar el producto");
            return "redirect:/admin/productos";
        }
    }

    @GetMapping("/admin/productos/eliminar/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Secretario', 'Domiciliario')")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            productoService.deleteProducto(id);
            log.info("Producto eliminado correctamente");
            redirectAttributes.addFlashAttribute("messageOK", "Producto eliminado correctamente");
        } catch (Exception e) {
            log.error("Error al eliminar el producto", e);
            redirectAttributes.addFlashAttribute("messageKO", "Error al eliminar el producto");
        }
        return "redirect:/admin/productos";
    }
}