package com.devalb.wellbing2.service;

import java.util.List;

import com.devalb.wellbing2.entity.Categoria;

public interface CategoriaService {

    public List<Categoria> getCategorias();

    public Categoria getCategoriaById(Long id);

    public Categoria addCategoria(Categoria categoria);
    
    public Categoria editCategoria(Categoria categoria);

    public void deleteCategoria(Long id);

    public Categoria getCategoriaByNombre(String nombre);

}
