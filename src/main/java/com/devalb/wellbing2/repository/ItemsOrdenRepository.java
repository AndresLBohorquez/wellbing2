package com.devalb.wellbing2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.ItemsOrden;

@Repository
public interface ItemsOrdenRepository extends JpaRepository<ItemsOrden, Long> {

    @Query(value = "SELECT * FROM items_orden WHERE orden_id = ?1", nativeQuery = true)
    public List<ItemsOrden> findAllByIdOrden(Long id);

    @Query("SELECT io.producto, SUM(io.cantidad) as totalCantidad " +
            "FROM ItemsOrden io " +
            "GROUP BY io.producto " +
            "ORDER BY totalCantidad DESC")
    List<Object[]> findTop10ProductosMasVendidos();
}
