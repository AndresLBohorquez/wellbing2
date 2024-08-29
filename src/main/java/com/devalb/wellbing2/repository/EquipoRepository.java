package com.devalb.wellbing2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Equipo;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    @Query(value = "SELECT * FROM equipo WHERE usuario_id = ?1", nativeQuery = true)
    public List<Equipo> findAllByIdUsuario(Long id);

    @Query(value = "SELECT * FROM equipo WHERE visible = true", nativeQuery = true)
    public List<Equipo> findAllVisible();

    @Query(value = "SELECT * FROM equipo WHERE visible = true and usuario_id = ?1", nativeQuery = true)
    public List<Equipo> findAllVisibleByUsuario(Long id);

}
