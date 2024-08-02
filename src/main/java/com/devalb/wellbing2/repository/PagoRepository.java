package com.devalb.wellbing2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Query(value = "SELECT * FROM pago WHERE estado_pago_id = ?1", nativeQuery = true)
    public List<Pago> findAllByEstado(Long id);

    @Query(value = "SELECT * FROM pago WHERE usuario_id = ?1", nativeQuery = true)
    public List<Pago> findAllByUsuarioId(Long id);

}
