package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.Carrito;

public interface CarritoService {

    public List<Carrito> getCarritos();

    public Carrito getCarritoById(Long id);

    public Carrito addCarrito(Carrito Carrito);

    public Carrito editCarrito(Carrito Carrito);

    public void deleteCarrito(Long id);

    public List<Carrito> getCarritoByUsuario(Long id);

    public double calcularTotalCarrito(Long usuarioId);

    public void deleteAllElements(Long userId);

}
