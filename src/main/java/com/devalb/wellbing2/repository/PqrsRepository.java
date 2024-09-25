package com.devalb.wellbing2.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Pqrs;

@Repository
public interface PqrsRepository extends JpaRepository<Pqrs, Long> {

    @Query(value = "SELECT * FROM pqrs WHERE estado_pqrs_id = ?1", nativeQuery = true)
    public List<Pqrs> findAllByEstado(Long id);

    @Query(value = "SELECT * FROM pqrs WHERE tipo_pqrs_id = ?1", nativeQuery = true)
    public List<Pqrs> findAllByTipo(Long id);

    @Query(value = "SELECT * FROM pqrs WHERE usuario_id = ?1", nativeQuery = true)
    public List<Pqrs> findAllByUsuarioId(Long id);

    public int countByFechaRegistroBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

}
