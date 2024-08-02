package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    public Rol findByNombre(String nombre);

}
