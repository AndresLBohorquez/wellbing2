package com.devalb.wellbing2.service.Impl;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Producto;
import com.devalb.wellbing2.repository.ProductoRepository;
import com.devalb.wellbing2.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public Producto addProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto editProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            producto.setVisible(false);
            productoRepository.save(producto);
        }
    }

    @Override
    public List<Producto> getProductosVisibles() {
        return productoRepository.findAllVisible();
    }

    @Override
    public List<Producto> getProductosByCategoria(Long id) {
        return productoRepository.findAllByCategoria(id);
    }

    @Override
    public Double getMaxPrecioProducto() {
        return productoRepository.findMaxPrice();
    }

    @Override
    public Map<String, Integer> getProductosLast6Months() {
        Map<String, Integer> registrosMensualesProductos = new LinkedHashMap<>();

        for (int i = 5; i >= 0; i--) {
            LocalDate fechaInicioMes = LocalDate.now().minusMonths(i).withDayOfMonth(1);
            LocalDate fechaFinMes = fechaInicioMes.withDayOfMonth(fechaInicioMes.lengthOfMonth());

            int cantidadProductos = productoRepository.countByFechaBetween(fechaInicioMes, fechaFinMes);
            registrosMensualesProductos.put(
                    fechaInicioMes.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")),
                    cantidadProductos);
        }

        return registrosMensualesProductos;
    }

    @Override
    public List<Producto> obtenerTop10ProductosMasVendidos() {
        return productoRepository.findTop10ProductosMasVendidos();
    }

    

}
