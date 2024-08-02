package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.EstadoPqrs;

@Repository
public interface EstadoPqrsRepository extends JpaRepository<EstadoPqrs, Long> {

    public EstadoPqrs findByNombre(String nombre);
}
