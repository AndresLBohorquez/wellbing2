package com.devalb.wellbing2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.MedioPago;

@Repository
public interface MedioPagoRepository extends JpaRepository<MedioPago, Long> {

    @Query(value = "SELECT * FROM medio_pago WHERE usuario_id = ?1", nativeQuery = true)
    public List<MedioPago> findAllByIdUsuario(Long id);
}
