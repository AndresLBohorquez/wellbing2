package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Carrito;
import com.devalb.wellbing2.repository.CarritoRepository;
import com.devalb.wellbing2.service.CarritoService;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Override
    public List<Carrito> getCarritos() {
        return carritoRepository.findAll();
    }

    @Override
    public Carrito getCarritoById(Long id) {
        return carritoRepository.findById(id).orElse(null);
    }

    @Override
    public Carrito addCarrito(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Override
    public Carrito editCarrito(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Override
    public void deleteCarrito(Long id) {
        carritoRepository.deleteById(id);
    }

    @Override
    public List<Carrito> getCarritoByUsuario(Long id) {
        return carritoRepository.findCarritoByUsuario(id);
    }

    @Override
    public double calcularTotalCarrito(Long usuarioId) {
        // Obtener todos los productos del carrito del usuario
        List<Carrito> productosCarrito = carritoRepository.findCarritoByUsuario(usuarioId);

        // Calcular el total sumando (precio * cantidad) de cada producto
        return productosCarrito.stream()
                .mapToDouble(item -> item.getProducto().getPrecio() * item.getCantidad())
                .sum();
    }

    @Override
    public void deleteAllElements(Long userId) {
        // Buscar todos los elementos del carrito del usuario
        List<Carrito> carritoElements = carritoRepository.findCarritoByUsuario(userId);

        // Borrar cada elemento del carrito
        for (Carrito element : carritoElements) {
            carritoRepository.delete(element);
        }
    }

}