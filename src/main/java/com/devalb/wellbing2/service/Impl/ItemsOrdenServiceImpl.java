package com.devalb.wellbing2.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.ItemsOrden;
import com.devalb.wellbing2.repository.ItemsOrdenRepository;
import com.devalb.wellbing2.service.ItemsOrdenService;

@Service
public class ItemsOrdenServiceImpl implements ItemsOrdenService {

    @Autowired
    private ItemsOrdenRepository itemsOrdenRepository;

    @Override
    public List<ItemsOrden> getItemsOrden() {
        return itemsOrdenRepository.findAll();
    }

    @Override
    public ItemsOrden getItemsOrdenById(Long id) {
        return itemsOrdenRepository.findById(id).orElse(null);
    }

    @Override
    public ItemsOrden addItemsOrden(ItemsOrden itemsOrden) {
        return itemsOrdenRepository.save(itemsOrden);
    }

    @Override
    public ItemsOrden editItemsOrden(ItemsOrden itemsOrden) {
        return itemsOrdenRepository.save(itemsOrden);
    }

    @Override
    public void deleteItemsOrden(Long id) {
        itemsOrdenRepository.deleteById(id);
    }

    @Override
    public List<ItemsOrden> getItemsOrdenByIdOrden(Long id) {
        return itemsOrdenRepository.findAllByIdOrden(id);
    }

    @Override
    public List<Object[]> obtenerTop10ProductosMasVendidos() {
        return itemsOrdenRepository.findTop10ProductosMasVendidos();
    }
}
