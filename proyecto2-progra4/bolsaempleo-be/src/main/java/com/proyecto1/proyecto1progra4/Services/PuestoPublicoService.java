package com.proyecto1.proyecto1progra4.Services;

import com.proyecto1.proyecto1progra4.Repositories.PuestoRepository;
import com.proyecto1.proyecto1progra4.Repositories.PuestocaracteristicaRepository;
import com.proyecto1.proyecto1progra4.Data.Puesto;
import com.proyecto1.proyecto1progra4.Data.Puestocaracteristica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class PuestoPublicoService {

    @Autowired private PuestoRepository puestos;
    @Autowired private PuestocaracteristicaRepository puestoCaracteristicaRepo;

    @Transactional
    public void guardarRequisito(Puestocaracteristica pc) {
        puestoCaracteristicaRepo.save(pc);
    }

    @Transactional(readOnly = true)
    public List<Puesto> top5PuestosPublicosRecientes() {
        return puestos.findTop5ByTipoPublicacionOrderByFechaRegistroDesc("PUBLICO");
    }

    @Transactional(readOnly = true)
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



    @Transactional(readOnly = true)
    public Puesto buscarPuestoPorId(Long id) {
        return puestos.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no existe: " + id));
    }

    @Transactional(readOnly = true)
    public List<Puestocaracteristica> requisitosDelPuesto(Long puestoId) {
        Puesto p = puestos.findById(puestoId)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no existe"));
        return puestoCaracteristicaRepo.findByPuesto(p);
    }

    @Transactional(readOnly = true)
    public Map<Long, List<Puestocaracteristica>> requisitosPorPuestos(List<Puesto> listaPuestos) {
        Map<Long, List<Puestocaracteristica>> mapa = new java.util.HashMap<>();
        for (Puesto p : listaPuestos) {
            mapa.put(p.getId(), puestoCaracteristicaRepo.findByPuesto(p));
        }
        return mapa;
    }

    @Transactional(readOnly = true)
    public List<Puesto> top5PuestosRecientes() {
        return puestos.findAll().stream()
                .filter(p -> p.getActivo())
                .sorted((a, b) -> b.getFechaRegistro().compareTo(a.getFechaRegistro()))
                .limit(5)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Puesto> buscarPuestos(List<Long> caracteristicaIds, boolean incluirPrivados) {
        if (caracteristicaIds == null || caracteristicaIds.isEmpty()) {
            if (incluirPrivados)
                return top5PuestosRecientes();
            else
                return top5PuestosPublicosRecientes();
        }

        return puestos.findAll().stream()
                .filter(p -> p.getActivo())
                .filter(p -> incluirPrivados || "PUBLICO".equals(p.getTipoPublicacion()))
                .filter(p -> {
                    List<Puestocaracteristica> requisitos = puestoCaracteristicaRepo.findByPuesto(p);
                    return requisitos.stream()
                            .anyMatch(r -> caracteristicaIds.contains(r.getCaracteristica().getId()));
                })
                .collect(Collectors.toList());
    }
}