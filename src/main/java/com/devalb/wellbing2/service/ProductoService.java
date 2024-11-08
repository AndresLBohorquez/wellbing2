package com.devalb.wellbing2.service;

import java.util.List;
import java.util.Map;

import com.devalb.wellbing2.entity.Producto;

public interface ProductoService {

    public List<Producto> getProductos();

    public Producto getProductoById(Long id);

    public Producto addProducto(Producto producto);

    public Producto editProducto(Producto producto);

    public void deleteProducto(Long id);

    public List<Producto> getProductosVisibles();

    public List<Producto> getProductosByCategoria(Long id);

    public Double getMaxPrecioProducto();

    public Map<String, Integer> getProductosLast6Months();

    public List<Producto> obtenerTop10ProductosMasVendidos();
}
