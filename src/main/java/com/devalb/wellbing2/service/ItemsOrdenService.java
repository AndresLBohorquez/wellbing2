package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.ItemsOrden;

public interface ItemsOrdenService {

    public List<ItemsOrden> getItemsOrden();

    public ItemsOrden getItemsOrdenById(Long id);

    public ItemsOrden addItemsOrden(ItemsOrden itemsOrden);

    public ItemsOrden editItemsOrden(ItemsOrden itemsOrden);

    public void deleteItemsOrden(Long id);

    public List<ItemsOrden> getItemsOrdenByIdOrden(Long id);
}
