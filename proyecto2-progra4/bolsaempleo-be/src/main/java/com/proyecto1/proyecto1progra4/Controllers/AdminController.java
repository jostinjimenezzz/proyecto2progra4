package com.proyecto1.proyecto1progra4.Controllers;


import com.proyecto1.proyecto1progra4.Services.AprobacionService;
import com.proyecto1.proyecto1progra4.Services.CaracteristicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AprobacionService aprobacionService;
    private final CaracteristicaService caracteristicaService;

    public AdminController(AprobacionService aprobacionService, CaracteristicaService caracteristicaService) {
        this.aprobacionService = aprobacionService;
        this.caracteristicaService = caracteristicaService;
    }

    record EmpresaDTO(Long id, String correo, String nombre,
                      String localizacion, String telefono, String descripcion) {}

    record OferenteDTO(Long id, String correo, String nombre, String primerApellido,
                       String identificacion, String nacionalidad,
                       String telefono, String lugarResidencia) {}

    @GetMapping("/empresas/pendientes")
    @Transactional(readOnly = true)
    public ResponseEntity<List<EmpresaDTO>> empresasPendientes() {
        List<EmpresaDTO> lista = aprobacionService.empresasPendientes()
                .stream()
                .map(e -> new EmpresaDTO(
                        e.getId(),
                        e.getUsuario() != null ? e.getUsuario().getUsername() : "",
                        e.getNombre(),
                        e.getLocalizacion(),
                        e.getTelefono(),
                        e.getDescripcion()
                ))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/oferentes/pendientes")
    @Transactional(readOnly = true)
    public ResponseEntity<List<OferenteDTO>> oferentesPendientes() {
        List<OferenteDTO> lista = aprobacionService.oferentesPendientes()
                .stream()
                .map(o -> new OferenteDTO(
                        o.getId(),
                        o.getUsuario() != null ? o.getUsuario().getUsername() : "",
                        o.getNombre(),
                        o.getPrimerApellido(),
                        o.getIdentificacion(),
                        o.getNacionalidad(),
                        o.getTelefono(),
                        o.getLugarResidencia()
                ))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/empresas/{id}/aprobar")
    public ResponseEntity<?> aprobarEmpresa(@PathVariable Long id) {
        aprobacionService.aprobarEmpresa(id);
        return ResponseEntity.ok(Map.of("message", "Empresa aprobada"));
    }

    @PostMapping("/empresas/{id}/rechazar")
    public ResponseEntity<?> rechazarEmpresa(@PathVariable Long id) {
        aprobacionService.rechazarEmpresa(id);
        return ResponseEntity.ok(Map.of("message", "Empresa rechazada"));
    }

    @PostMapping("/oferentes/{id}/rechazar")
    public ResponseEntity<?> rechazarOferente(@PathVariable Long id) {
        aprobacionService.rechazarOferente(id);
        return ResponseEntity.ok(Map.of("message", "Oferente rechazado"));
    }

    @PostMapping("/oferentes/{id}/aprobar")
    public ResponseEntity<?> aprobarOferente(@PathVariable Long id) {
        aprobacionService.aprobarOferente(id);
        return ResponseEntity.ok(Map.of("message", "Oferente aprobado"));
    }

    @PostMapping("/caracteristicas/crear")
    @Transactional
    public ResponseEntity<?> crearCaracteristica(
            @RequestParam String nombre,
            @RequestParam(required = false) Long idPadre) {
        caracteristicaService.crearCaracteristica(nombre, idPadre);
        return ResponseEntity.ok(Map.of("message", "Característica creada"));
    }
}