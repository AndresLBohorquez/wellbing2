package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.TipoPqrs;

@Repository
public interface TipoPqrsRepository extends JpaRepository<TipoPqrs, Long> {

    public TipoPqrs findByNombre(String nombre);

}
