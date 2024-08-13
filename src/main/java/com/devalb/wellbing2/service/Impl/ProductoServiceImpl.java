package com.devalb.wellbing2.service.Impl;

import java.util.List;

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

}
