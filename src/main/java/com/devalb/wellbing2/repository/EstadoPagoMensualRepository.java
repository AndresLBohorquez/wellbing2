package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.EstadoPagoMensual;

@Repository
public interface EstadoPagoMensualRepository extends JpaRepository<EstadoPagoMensual, Long> {

    public EstadoPagoMensual findByNombre(String nombre);
}
