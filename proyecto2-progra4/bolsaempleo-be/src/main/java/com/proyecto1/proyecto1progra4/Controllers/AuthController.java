package com.proyecto1.proyecto1progra4.Controllers;

import com.proyecto1.proyecto1progra4.Data.Empresa;
import com.proyecto1.proyecto1progra4.Data.Oferente;
import com.proyecto1.proyecto1progra4.Data.Usuario;
import com.proyecto1.proyecto1progra4.Data.dto.AuthDtos;
import com.proyecto1.proyecto1progra4.Data.dto.RegistroDtos;
import com.proyecto1.proyecto1progra4.Services.AuthService;
import com.proyecto1.proyecto1progra4.Services.RegistroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RegistroService registroService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService,
                          RegistroService registroService,
                          PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.registroService = registroService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.LoginResponse> login(@Valid @RequestBody AuthDtos.LoginRequest req) {
        String token = authService.login(req.username(), req.clave());
        Usuario usuario = authService.getUsuarioByUsername(req.username());
        return ResponseEntity.ok(new AuthDtos.LoginResponse(token, usuario.getUsername(), usuario.getRol()));
    }

    @PostMapping("/registro/empresa")
    public ResponseEntity<?> registrarEmpresa(@Valid @RequestBody RegistroDtos.RegistroEmpresaRequest body) {
        Usuario u = new Usuario();
        u.setUsername(body.correo());
        u.setClave(passwordEncoder.encode(body.clave()));

        Empresa e = new Empresa();
        e.setNombre(body.nombre());
        e.setLocalizacion(body.localizacion());
        e.setTelefono(body.telefono());
        e.setDescripcion(body.descripcion());

        registroService.registrarEmpresa(u, e);
        return ResponseEntity.ok(java.util.Map.of("message", "Registro de empresa enviado"));
    }

    @PostMapping("/registro/oferente")
    public ResponseEntity<?> registrarOferente(@Valid @RequestBody RegistroDtos.RegistroOferenteRequest body) {
        Usuario u = new Usuario();
        u.setUsername(body.correo());
        u.setClave(passwordEncoder.encode(body.clave()));

        Oferente o = new Oferente();
        o.setIdentificacion(body.identificacion());
        o.setNombre(body.nombre());
        o.setPrimerApellido(body.primerApellido());
        o.setNacionalidad(body.nacionalidad());
        o.setTelefono(body.telefono());
        o.setLugarResidencia(body.lugarResidencia());

        registroService.registrarOferente(u, o);
        return ResponseEntity.ok(java.util.Map.of("message", "Registro de oferente enviado"));
    }
}
