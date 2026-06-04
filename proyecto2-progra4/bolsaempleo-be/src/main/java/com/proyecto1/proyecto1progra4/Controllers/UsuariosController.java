package com.proyecto1.proyecto1progra4.Controllers;

import com.proyecto1.proyecto1progra4.Data.Usuario;
import com.proyecto1.proyecto1progra4.Repositories.UsuariosRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    private final UsuariosRepository usuariosRepository;

    public UsuariosController(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        Usuario u = usuariosRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "role", u.getRol(),
                "habilitado", u.getHabilitado()
        ));
    }
}
