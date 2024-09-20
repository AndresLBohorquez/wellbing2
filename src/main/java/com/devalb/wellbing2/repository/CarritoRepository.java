package com.devalb.wellbing2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Carrito;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    @Query(value = "SELECT * FROM carrito WHERE usuario_id = ?1", nativeQuery = true)
    public List<Carrito> findCarritoByUsuario(Long id);
}
