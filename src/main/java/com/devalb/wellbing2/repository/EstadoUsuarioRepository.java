package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.EstadoUsuario;

@Repository
public interface EstadoUsuarioRepository extends JpaRepository<EstadoUsuario, Long> {

    public EstadoUsuario findByNombre(String nombre);

}
