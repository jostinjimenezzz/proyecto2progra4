package com.proyecto1.proyecto1progra4.Services;

import com.proyecto1.proyecto1progra4.Data.*;
import com.proyecto1.proyecto1progra4.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class Service {
/*
    @Autowired private PuestocaracteristicaRepository puestoCaracteristicaRepo;
    @Autowired private UsuariosRepository usuarios;
    @Autowired private EmpresaRepository empresas;
    @Autowired private OferenteRepository oferentes;
    @Autowired private PuestoRepository puestos;
    @Autowired private CaracteristicaRepository caracteristicas;


    public List<Puesto> top5PuestosPublicosRecientes() {
        return puestos.findTop5ByTipoPublicacionOrderByFechaRegistroDesc("PUBLICO");
    }

    @Transactional
    public void registrarEmpresa(Usuario u, Empresa e) {
        // Reglas básicas
        if (u.getId() != null) throw new IllegalArgumentException("Usuario ya tiene id");
        u.setRol("EMPRESA");
        if (u.getFechaCreacion() == null) u.setFechaCreacion(Instant.now());
        if (u.getHabilitado() == null) u.setHabilitado(true);

        usuarios.save(u);

        // enlazar
        e.setUsuario(u);
        e.setEstadoAprobacion("PENDIENTE");
        if (e.getFechaRegistro() == null) e.setFechaRegistro(Instant.now());

        empresas.save(e);
    }

    public List<Caracteristica> todasLasCaracteristicas() {
        return caracteristicas.findAll();
    }

    public List<Puesto> buscarPuestosPublicos(List<Long> caracteristicaIds) {
        if (caracteristicaIds == null || caracteristicaIds.isEmpty()) {
            return puestos.findTop5ByTipoPublicacionOrderByFechaRegistroDesc("PUBLICO");
        }
        return puestos.findAll().stream()
                .filter(p -> "PUBLICO".equals(p.getTipoPublicacion()) && p.getActivo())
                .filter(p -> {
                    List<Puestocaracteristica> requisitos =
                            puestoCaracteristicaRepo.findByPuesto(p);
                    return requisitos.stream()
                            .anyMatch(r -> caracteristicaIds.contains(r.getCaracteristica().getId()));
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public void crearCaracteristica(String nombre, Long idPadre) {
        Caracteristica c = new Caracteristica();
        c.setNombre(nombre);
        if (idPadre != null) {
            Caracteristica padre = caracteristicas.findById(idPadre)
                    .orElseThrow(() -> new IllegalArgumentException("Padre no existe"));
            c.setIdPadre(padre);
        }
        caracteristicas.save(c);
    }

    @Transactional
    public void registrarOferente(Usuario u, Oferente o) {
        if (u.getId() != null) throw new IllegalArgumentException("Usuario ya tiene id");
        u.setRol("OFERENTE");
        if (u.getFechaCreacion() == null) u.setFechaCreacion(Instant.now());
        if (u.getHabilitado() == null) u.setHabilitado(true);

        usuarios.save(u);

        o.setUsuario(u);
        o.setEstadoAprobacion("PENDIENTE");
        if (o.getFechaRegistro() == null) o.setFechaRegistro(Instant.now());

        oferentes.save(o);
    }

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

    @Transactional
    public void aprobarOferente(Long oferenteId) {
        Oferente o = oferentes.findById(oferenteId)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no existe: " + oferenteId));
        o.setEstadoAprobacion("APROBADO");
        o.setFechaAprobacion(Instant.now());
        oferentes.save(o);
    }

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

    public List<Caracteristica> caracteristicasRaiz() {
        // raíces: idPadre = null
        return caracteristicas.findRaices();
    }

    public List<Caracteristica> caracteristicasHijas(Long idPadre) {
        return caracteristicas.findByIdPadre(idPadre);
    }
 */
}