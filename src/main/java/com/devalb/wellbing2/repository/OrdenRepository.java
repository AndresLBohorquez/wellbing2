package com.devalb.wellbing2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Orden;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    @Query(value = "SELECT * FROM orden WHERE estado_orden_id = ?1", nativeQuery = true)
    public List<Orden> findAllByEstado(Long id);

    @Query(value = "SELECT * FROM orden WHERE usuario_id = ?1", nativeQuery = true)
    public List<Orden> findAllByUsuarioId(Long id);

    @Query(value = "SELECT * FROM orden WHERE visible = true", nativeQuery = true)
    public List<Orden> findAllVisible();

    @Query(value = "SELECT * FROM orden WHERE visible = true and usuario_id = ?1", nativeQuery = true)
    public List<Orden> findAllVisibleByUsuario(Long id);
}
