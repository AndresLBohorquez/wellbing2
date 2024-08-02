package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.EstadoPago;

@Repository
public interface EstadoPagoRepository extends JpaRepository<EstadoPago, Long> {

    public EstadoPago findByNombre(String nombre);
}
