package com.proyecto1.proyecto1progra4.Controllers;

import com.proyecto1.proyecto1progra4.Services.AprobacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AprobacionService aprobacionService;

    public AdminController(AprobacionService aprobacionService) {
        this.aprobacionService = aprobacionService;
    }

    @GetMapping("/empresas/pendientes")
    public ResponseEntity<?> empresasPendientes() {
        return ResponseEntity.ok(aprobacionService.empresasPendientes());
    }

    @GetMapping("/oferentes/pendientes")
    public ResponseEntity<?> oferentesPendientes() {
        return ResponseEntity.ok(aprobacionService.oferentesPendientes());
    }

    @PostMapping("/empresas/{id}/aprobar")
    public ResponseEntity<?> aprobarEmpresa(@PathVariable Long id) {
        aprobacionService.aprobarEmpresa(id);
        return ResponseEntity.ok(Map.of("message", "Empresa aprobada"));
    }

    @PostMapping("/oferentes/{id}/aprobar")
    public ResponseEntity<?> aprobarOferente(@PathVariable Long id) {
        aprobacionService.aprobarOferente(id);
        return ResponseEntity.ok(Map.of("message", "Oferente aprobado"));
    }
}
