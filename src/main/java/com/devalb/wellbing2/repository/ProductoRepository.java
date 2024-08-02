package com.devalb.wellbing2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query(value = "SELECT * FROM producto WHERE visible= true", nativeQuery = true)
    public List<Producto> findAllVisible();

    @Query(value = "SELECT * FROM producto WHERE categoria_id = ?1", nativeQuery = true)
    public List<Producto> findAllByCategoria(Long id);
}
