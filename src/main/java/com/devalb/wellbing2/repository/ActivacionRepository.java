package com.devalb.wellbing2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Activacion;

@Repository
public interface ActivacionRepository extends JpaRepository<Activacion, Long> {

    @Query(value = "SELECT * FROM activacion where usuario_id = ?1", nativeQuery = true)
    public List<Activacion> findAllByIdUsuario(Long id);

    @Query(value = "SELECT * FROM activacion WHERE usuario_id = ?1 ORDER BY fecha DESC LIMIT 1", nativeQuery = true)
    public Activacion findLastActivationByIdUsuario(Long id);

    @Query(value = "SELECT * FROM activacion WHERE estado_activacion_id = ?1", nativeQuery = true)
    public List<Activacion> findAllByEstadoActivacion(int id);
}
