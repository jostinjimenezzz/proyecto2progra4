package com.proyecto1.proyecto1progra4.Repositories;

import com.proyecto1.proyecto1progra4.Data.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}