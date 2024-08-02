package com.devalb.wellbing2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.WellPoints;

@Repository
public interface WellPointsRepository extends JpaRepository<WellPoints, Long> {

    @Query(value = "SELECT * FROM well_points WHERE usuario_id = ?1", nativeQuery = true)
    public WellPoints findByUsuario(Long id);

}
