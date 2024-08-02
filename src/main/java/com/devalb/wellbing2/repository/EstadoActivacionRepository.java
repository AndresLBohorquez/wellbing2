package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.EstadoActivacion;

@Repository
public interface EstadoActivacionRepository extends JpaRepository<EstadoActivacion, Long> {

    public EstadoActivacion findByNombre(String nombre);
}
