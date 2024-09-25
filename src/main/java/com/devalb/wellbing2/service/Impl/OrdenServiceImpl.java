package com.devalb.wellbing2.service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Orden;
import com.devalb.wellbing2.repository.OrdenRepository;
import com.devalb.wellbing2.service.OrdenService;

@Service
public class OrdenServiceImpl implements OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Override
    public List<Orden> getOrdenes() {
        return ordenRepository.findAll();
    }

    @Override
    public Orden getOrdenById(Long id) {
        return ordenRepository.findById(id).orElse(null);
    }

    @Override
    public Orden addOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    @Override
    public Orden editOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    @Override
    public void deleteOrden(Long id) {
        Orden orden = ordenRepository.findById(id).orElse(null);
        if (orden != null) {
            orden.setVisible(false);
            ordenRepository.save(orden);
        }
    }

    @Override
    public List<Orden> getOrdenesByEstado(Long id) {
        return ordenRepository.findAllByEstado(id);
    }

    @Override
    public List<Orden> getOrdenesByUsuario(Long id) {
        return ordenRepository.findAllByUsuarioId(id);
    }

    @Override
    public List<Orden> getOrdenesVisibles() {
        return ordenRepository.findAllVisible();
    }

    @Override
    public List<Orden> getOrdenesVisiblesByUsuario(Long id) {
        return ordenRepository.findAllVisibleByUsuario(id);
    }

    @Override
    public Map<String, Integer> getOrdenesLast6Months() {
        Map<String, Integer> registrosMensualesOrdenes = new LinkedHashMap<>();

        // Obtener los últimos 6 meses
        for (int i = 5; i >= 0; i--) {
            LocalDateTime fechaInicioMes = LocalDate.now().minusMonths(i).withDayOfMonth(1).atStartOfDay();
            LocalDateTime fechaFinMes = fechaInicioMes.withDayOfMonth(fechaInicioMes.toLocalDate().lengthOfMonth())
                    .withHour(23).withMinute(59).withSecond(59);

            int cantidadOrdenes = ordenRepository.countByFechaCreacionBetween(fechaInicioMes, fechaFinMes);

            registrosMensualesOrdenes.put(
                    fechaInicioMes.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")),
                    cantidadOrdenes);
        }

        return registrosMensualesOrdenes;
    }

    @Override
    public Map<String, Double> getIngresosLast6Months() {
        Map<String, Double> ingresosMensuales = new LinkedHashMap<>();

        for (int i = 5; i >= 0; i--) {

            LocalDateTime fechaInicioMes = LocalDate.now().minusMonths(i).withDayOfMonth(1).atStartOfDay();
            LocalDateTime fechaFinMes = fechaInicioMes.withDayOfMonth(fechaInicioMes.toLocalDate().lengthOfMonth())
                    .withHour(23).withMinute(59).withSecond(59);

            // Sumar el total de las órdenes del mes
            Double totalIngresos = ordenRepository.sumTotalByFechaCreacionBetween(fechaInicioMes, fechaFinMes);

            // Si no hay ingresos, asignar 0.0
            if (totalIngresos == null) {
                totalIngresos = 0.0;
            }

            // Agregar al mapa el nombre del mes y el total de ingresos
            ingresosMensuales.put(
                    fechaInicioMes.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")),
                    totalIngresos);
        }

        return ingresosMensuales;
    }

}
