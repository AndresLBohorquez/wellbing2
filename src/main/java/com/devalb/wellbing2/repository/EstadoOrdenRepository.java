package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.EstadoOrden;

@Repository
public interface EstadoOrdenRepository extends JpaRepository<EstadoOrden, Long> {

    public EstadoOrden findByNombre(String nombre);
}
