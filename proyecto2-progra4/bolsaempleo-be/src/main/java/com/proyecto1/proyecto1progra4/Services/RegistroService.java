package com.proyecto1.proyecto1progra4.Services;

import com.proyecto1.proyecto1progra4.Repositories.EmpresaRepository;
import com.proyecto1.proyecto1progra4.Repositories.OferenteRepository;
import com.proyecto1.proyecto1progra4.Repositories.UsuariosRepository;
import com.proyecto1.proyecto1progra4.Data.Empresa;
import com.proyecto1.proyecto1progra4.Data.Oferente;
import com.proyecto1.proyecto1progra4.Data.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@org.springframework.stereotype.Service
public class RegistroService {

    @Autowired private UsuariosRepository usuarios;
    @Autowired private EmpresaRepository empresas;
    @Autowired private OferenteRepository oferentes;

    @Transactional
    public void registrarEmpresa(Usuario u, Empresa e) {
        if (u.getId() != null) throw new IllegalArgumentException("Usuario ya tiene id");
        u.setRol("EMPRESA");
        if (u.getFechaCreacion() == null) u.setFechaCreacion(Instant.now());
        if (u.getHabilitado() == null) u.setHabilitado(true);

        usuarios.save(u);

        e.setUsuario(u);
        e.setEstadoAprobacion("PENDIENTE");
        if (e.getFechaRegistro() == null) e.setFechaRegistro(Instant.now());

        empresas.save(e);
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
}