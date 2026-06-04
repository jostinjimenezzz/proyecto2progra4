package com.proyecto1.proyecto1progra4.Services;

import com.proyecto1.proyecto1progra4.Data.Usuario;
import com.proyecto1.proyecto1progra4.Repositories.UsuariosRepository;
import com.proyecto1.proyecto1progra4.Security.JwtProvider;
import com.proyecto1.proyecto1progra4.Security.UserDetailsImp;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UsuariosRepository usuariosRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider,
                       UsuariosRepository usuariosRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.usuariosRepository = usuariosRepository;
    }

    public String login(String username, String clave) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, clave)
            );
            return jwtProvider.generateToken((UserDetailsImp) authentication.getPrincipal());
        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
    }

    public Usuario getUsuarioByUsername(String username) {
        return usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
