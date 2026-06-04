package com.proyecto1.proyecto1progra4.Controllers;

import com.proyecto1.proyecto1progra4.Data.Empresa;
import com.proyecto1.proyecto1progra4.Data.Oferente;
import com.proyecto1.proyecto1progra4.Data.Usuario;
import com.proyecto1.proyecto1progra4.Data.dto.AuthDtos;
import com.proyecto1.proyecto1progra4.Services.AuthService;
import com.proyecto1.proyecto1progra4.Services.RegistroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<?> registrarEmpresa(@RequestBody Map<String, String> body) {
        Usuario u = new Usuario();
        u.setUsername(body.get("correo"));
        u.setClave(passwordEncoder.encode(body.get("clave")));

        Empresa e = new Empresa();
        e.setNombre(body.get("nombre"));
        e.setLocalizacion(body.get("localizacion"));
        e.setTelefono(body.get("telefono"));
        e.setDescripcion(body.get("descripcion"));

        registroService.registrarEmpresa(u, e);
        return ResponseEntity.ok(Map.of("message", "Registro de empresa enviado"));
    }

    @PostMapping("/registro/oferente")
    public ResponseEntity<?> registrarOferente(@RequestBody Map<String, String> body) {
        Usuario u = new Usuario();
        u.setUsername(body.get("correo"));
        u.setClave(passwordEncoder.encode(body.get("clave")));

        Oferente o = new Oferente();
        o.setIdentificacion(body.get("identificacion"));
        o.setNombre(body.get("nombre"));
        o.setPrimerApellido(body.get("primerApellido"));
        o.setNacionalidad(body.get("nacionalidad"));
        o.setTelefono(body.get("telefono"));
        o.setLugarResidencia(body.get("lugarResidencia"));

        registroService.registrarOferente(u, o);
        return ResponseEntity.ok(Map.of("message", "Registro de oferente enviado"));
    }
}
