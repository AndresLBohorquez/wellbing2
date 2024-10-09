package com.devalb.wellbing2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.PagoMensual;

@Repository
public interface PagoMensualRepository extends JpaRepository<PagoMensual, Long> {

    @Query(value = "SELECT * FROM pago_mensual WHERE usuario_id = ?1", nativeQuery = true)
    public List<PagoMensual> findAllByUsuarioId(Long id);
}
