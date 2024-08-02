package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Redes;

@Repository
public interface RedesRepository extends JpaRepository<Redes, Long> {

    public Redes findByNombre(String nombre);
}
