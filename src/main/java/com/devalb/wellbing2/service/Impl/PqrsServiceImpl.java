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

import com.devalb.wellbing2.entity.Pqrs;
import com.devalb.wellbing2.repository.PqrsRepository;
import com.devalb.wellbing2.service.PqrsService;

@Service
public class PqrsServiceImpl implements PqrsService {

    @Autowired
    private PqrsRepository pqrsRepository;

    @Override
    public List<Pqrs> getPqrs() {
        return pqrsRepository.findAll();
    }

    @Override
    public Pqrs getPqrsById(Long id) {
        return pqrsRepository.findById(id).orElse(null);
    }

    @Override
    public Pqrs addPqrs(Pqrs pqrs) {
        return pqrsRepository.save(pqrs);
    }

    @Override
    public Pqrs editPqrs(Pqrs pqrs) {
        return pqrsRepository.save(pqrs);
    }

    @Override
    public void deletePqrs(Long id) {
        pqrsRepository.deleteById(id);
    }

    @Override
    public List<Pqrs> getPqrsByEstado(Long id) {
        return pqrsRepository.findAllByEstado(id);
    }

    @Override
    public List<Pqrs> getPqrsByTipo(Long id) {
        return pqrsRepository.findAllByTipo(id);
    }

    @Override
    public List<Pqrs> getPqrsByUsuario(Long id) {
        return pqrsRepository.findAllByUsuarioId(id);
    }

    @Override
    public Map<String, Integer> getPqrsLast6Months() {
        Map<String, Integer> registrosMensualesPqrs = new LinkedHashMap<>();

        for (int i = 5; i >= 0; i--) {

            LocalDateTime fechaInicioMes = LocalDate.now().minusMonths(i).withDayOfMonth(1).atStartOfDay();
            LocalDateTime fechaFinMes = fechaInicioMes.withDayOfMonth(fechaInicioMes.toLocalDate().lengthOfMonth())
                    .withHour(23).withMinute(59).withSecond(59);

            int cantidadPqrs = pqrsRepository.countByFechaRegistroBetween(fechaInicioMes, fechaFinMes);

            registrosMensualesPqrs.put(
                    fechaInicioMes.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")),
                    cantidadPqrs);
        }

        return registrosMensualesPqrs;
    }

}
