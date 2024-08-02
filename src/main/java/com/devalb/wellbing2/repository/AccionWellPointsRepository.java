package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.AccionWellPoints;

@Repository
public interface AccionWellPointsRepository extends JpaRepository<AccionWellPoints, Long> {

    public AccionWellPoints findByNombre(String nombre);
}
