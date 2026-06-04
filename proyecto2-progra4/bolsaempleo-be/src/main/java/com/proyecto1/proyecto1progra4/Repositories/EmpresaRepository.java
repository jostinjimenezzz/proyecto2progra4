package com.proyecto1.proyecto1progra4.Repositories;
import java.util.Optional;

import com.proyecto1.proyecto1progra4.Data.Empresa;
import com.proyecto1.proyecto1progra4.Data.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findByEstadoAprobacion(String estadoAprobacion);
    Optional<Empresa> findByUsuario(Usuario usuario);
}