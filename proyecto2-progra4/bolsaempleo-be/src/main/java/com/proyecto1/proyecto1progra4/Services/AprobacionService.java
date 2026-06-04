package com.proyecto1.proyecto1progra4.Services;

import com.proyecto1.proyecto1progra4.Repositories.EmpresaRepository;
import com.proyecto1.proyecto1progra4.Repositories.OferenteRepository;
import com.proyecto1.proyecto1progra4.Data.Empresa;
import com.proyecto1.proyecto1progra4.Data.Oferente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@org.springframework.stereotype.Service
public class AprobacionService {

    @Autowired private EmpresaRepository empresas;
    @Autowired private OferenteRepository oferentes;

    public List<Empresa> empresasPendientes() {
        return empresas.findByEstadoAprobacion("PENDIENTE");
    }

    public List<Oferente> oferentesPendientes() {
        return oferentes.findByEstadoAprobacion("PENDIENTE");
    }

    @Transactional
    public void aprobarEmpresa(Long empresaId) {
        Empresa e = empresas.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no existe: " + empresaId));
        e.setEstadoAprobacion("APROBADO");
        e.setFechaAprobacion(Instant.now());
        empresas.save(e);
    }

    @Transactional
    public void aprobarOferente(Long oferenteId) {
        Oferente o = oferentes.findById(oferenteId)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no existe: " + oferenteId));
        o.setEstadoAprobacion("APROBADO");
        o.setFechaAprobacion(Instant.now());
        oferentes.save(o);
    }
}