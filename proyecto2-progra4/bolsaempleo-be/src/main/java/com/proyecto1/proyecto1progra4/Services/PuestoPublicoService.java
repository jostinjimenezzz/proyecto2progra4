package com.proyecto1.proyecto1progra4.Services;

import com.proyecto1.proyecto1progra4.Repositories.PuestoRepository;
import com.proyecto1.proyecto1progra4.Repositories.PuestocaracteristicaRepository;
import com.proyecto1.proyecto1progra4.Data.Puesto;
import com.proyecto1.proyecto1progra4.Data.Puestocaracteristica;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class PuestoPublicoService {

    @Autowired private PuestoRepository puestos;
    @Autowired private PuestocaracteristicaRepository puestoCaracteristicaRepo;

    public List<Puesto> top5PuestosPublicosRecientes() {
        return puestos.findTop5ByTipoPublicacionOrderByFechaRegistroDesc("PUBLICO");
    }

    public List<Puesto> buscarPuestosPublicos(List<Long> caracteristicaIds) {
        if (caracteristicaIds == null || caracteristicaIds.isEmpty()) {
            return puestos.findTop5ByTipoPublicacionOrderByFechaRegistroDesc("PUBLICO");
        }

        return puestos.findAll().stream()
                .filter(p -> "PUBLICO".equals(p.getTipoPublicacion()) && p.getActivo())
                .filter(p -> {
                    List<Puestocaracteristica> requisitos = puestoCaracteristicaRepo.findByPuesto(p);
                    return requisitos.stream()
                            .anyMatch(r -> caracteristicaIds.contains(r.getCaracteristica().getId()));
                })
                .collect(Collectors.toList());
    }

    public Puesto buscarPuestoPorId(Long id) {
        return puestos.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no existe: " + id));
    }

    public List<Puestocaracteristica> requisitosDelPuesto(Long puestoId) {
        Puesto p = puestos.findById(puestoId)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no existe"));
        return puestoCaracteristicaRepo.findByPuesto(p);
    }

    public Map<Long, List<Puestocaracteristica>> requisitosPorPuestos(List<Puesto> listaPuestos) {
        Map<Long, List<Puestocaracteristica>> mapa = new java.util.HashMap<>();
        for (Puesto p : listaPuestos) {
            mapa.put(p.getId(), puestoCaracteristicaRepo.findByPuesto(p));
        }
        return mapa;
    }
}