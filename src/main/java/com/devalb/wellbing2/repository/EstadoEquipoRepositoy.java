package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.EstadoEquipo;

@Repository
public interface EstadoEquipoRepositoy extends JpaRepository<EstadoEquipo, Long> {

    public EstadoEquipo findByNombre(String nombre);
}
