package com.devalb.wellbing2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.RedesUsuario;

@Repository
public interface RedesUsuarioRepository extends JpaRepository<RedesUsuario, Long> {

    @Query(value = "SELECT * FROM redes_usuario WHERE usuario_id = ?1", nativeQuery = true)
    public List<RedesUsuario> findAllByUsuario(Long id);
}
