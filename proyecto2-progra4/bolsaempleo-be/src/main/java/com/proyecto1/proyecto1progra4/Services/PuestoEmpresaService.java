package com.proyecto1.proyecto1progra4.Services;

import com.proyecto1.proyecto1progra4.Repositories.PuestoRepository;
import com.proyecto1.proyecto1progra4.Data.Puesto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@org.springframework.stereotype.Service
public class PuestoEmpresaService {

    @Autowired private PuestoRepository puestos;

    public List<Puesto> puestosPorEmpresa(Long empresaId) {
        return puestos.findByEmpresaIdOrderByFechaRegistroDesc(empresaId);
    }

    @Transactional
    public void crearPuesto(Puesto p) {
        if (p.getId() != null) throw new IllegalArgumentException("Puesto ya tiene id");
        if (p.getFechaRegistro() == null) p.setFechaRegistro(Instant.now());
        if (p.getActivo() == null) p.setActivo(true);
        puestos.save(p);
    }

    @Transactional
    public void desactivarPuesto(Long puestoId) {
        Puesto p = puestos.findById(puestoId)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no existe: " + puestoId));
        p.setActivo(false);
        p.setFechaDesactivacion(Instant.now());
        puestos.save(p);
    }
}