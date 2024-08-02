package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Mensaje;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

}
