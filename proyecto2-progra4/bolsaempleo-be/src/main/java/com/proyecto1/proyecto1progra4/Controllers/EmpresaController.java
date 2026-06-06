package com.proyecto1.proyecto1progra4.Controllers;

import com.proyecto1.proyecto1progra4.Data.*;
import com.proyecto1.proyecto1progra4.Repositories.*;
import com.proyecto1.proyecto1progra4.Services.CaracteristicaService;
import com.proyecto1.proyecto1progra4.Services.PuestoEmpresaService;
import com.proyecto1.proyecto1progra4.Services.PuestoPublicoService;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    private final EmpresaRepository empresaRepository;
    private final PuestoEmpresaService puestoEmpresaService;
    private final PuestoPublicoService puestoPublicoService;
    private final CaracteristicaService caracteristicaService;
    private final OferenteRepository oferenteRepository;
    private final OferentehabilidadRepository habilidadRepository;

    public EmpresaController(EmpresaRepository empresaRepository,
                             PuestoEmpresaService puestoEmpresaService,
                             PuestoPublicoService puestoPublicoService,
                             CaracteristicaService caracteristicaService,
                             OferenteRepository oferenteRepository,
                             OferentehabilidadRepository habilidadRepository) {
        this.empresaRepository = empresaRepository;
        this.puestoEmpresaService = puestoEmpresaService;
        this.puestoPublicoService = puestoPublicoService;
        this.caracteristicaService = caracteristicaService;
        this.oferenteRepository = oferenteRepository;
        this.habilidadRepository = habilidadRepository;
    }

    record CaracteristicaDTO(Long id, String nombre, Long idPadre) {}
    record RequisitoDTO(Long caracteristicaId, String nombreCaracteristica, Integer nivelDeseado) {}
    record PuestoDTO(Long id, String descripcionGeneral, BigDecimal salarioOfrecido,
                     String tipoPublicacion, Boolean activo, List<RequisitoDTO> requisitos) {}

    @GetMapping("/puestos")
    @Transactional(readOnly = true)
    public ResponseEntity<List<PuestoDTO>> misPuestos(Authentication auth) {
        Usuario usuario = getUsuario(auth);
        Empresa empresa = empresaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        List<PuestoDTO> lista = puestoEmpresaService.puestosPorEmpresa(empresa.getId())
                .stream()
                .map(p -> {
                    List<RequisitoDTO> reqs = puestoPublicoService.requisitosDelPuesto(p.getId())
                            .stream()
                            .map(r -> new RequisitoDTO(
                                    r.getCaracteristica().getId(),
                                    r.getCaracteristica().getNombre(),
                                    r.getNivelDeseado()
                            ))
                            .toList();
                    return new PuestoDTO(p.getId(), p.getDescripcionGeneral(),
                            p.getSalarioOfrecido(), p.getTipoPublicacion(), p.getActivo(), reqs);
                })
                .toList();
        return ResponseEntity.ok(lista);
    }

    record HabilidadDTO(String nombre, Integer nivel) {}
    record CandidatoDetalleDTO(Long id, String nombre, String primerApellido,
                               String identificacion, String nacionalidad,
                               String telefono, String lugarResidencia,
                               boolean tieneCv, List<HabilidadDTO> habilidades) {}

    @GetMapping("/candidatos/{id}/detalle")
    @Transactional(readOnly = true)
    public ResponseEntity<CandidatoDetalleDTO> detalleCandidato(@PathVariable Long id) {
        Oferente oferente = oferenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferente no encontrado"));

        List<HabilidadDTO> habilidades = habilidadRepository.findByOferente(oferente)
                .stream()
                .map(h -> new HabilidadDTO(
                        h.getCaracteristica().getNombre(),
                        h.getNivel()
                ))
                .toList();

        return ResponseEntity.ok(new CandidatoDetalleDTO(
                oferente.getId(),
                oferente.getNombre(),
                oferente.getPrimerApellido(),
                oferente.getIdentificacion(),
                oferente.getNacionalidad(),
                oferente.getTelefono(),
                oferente.getLugarResidencia(),
                oferente.getCvPath() != null && !oferente.getCvPath().isBlank(),
                habilidades
        ));
    }

    @GetMapping("/caracteristicas")
    @Transactional(readOnly = true)
    public ResponseEntity<List<CaracteristicaDTO>> caracteristicas() {
        List<CaracteristicaDTO> lista = caracteristicaService.todasLasCaracteristicas()
                .stream()
                .map(c -> new CaracteristicaDTO(
                        c.getId(),
                        c.getNombre(),
                        c.getIdPadre() != null ? c.getIdPadre().getId() : null
                ))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/candidatos/{id}/cv")
    @Transactional(readOnly = true)
    public ResponseEntity<org.springframework.core.io.Resource> verCv(@PathVariable Long id) throws java.io.IOException {
        Oferente oferente = oferenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferente no encontrado"));

        if (oferente.getCvPath() == null || oferente.getCvPath().isBlank())
            return ResponseEntity.notFound().build();

        java.nio.file.Path path = java.nio.file.Paths.get(oferente.getCvPath());
        if (!java.nio.file.Files.exists(path))
            return ResponseEntity.notFound().build();

        org.springframework.core.io.Resource resource =
                new org.springframework.core.io.UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=\"cv_" + oferente.getId() + ".pdf\"")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping("/puestos/crear")
    @Transactional
    public ResponseEntity<?> crearPuesto(Authentication auth,
                                         @RequestBody CrearPuestoRequest req) {
        Usuario usuario = getUsuario(auth);
        Empresa empresa = empresaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        Puesto p = new Puesto();
        p.setEmpresa(empresa);
        p.setDescripcionGeneral(req.descripcionGeneral());
        p.setSalarioOfrecido(req.salarioOfrecido());
        p.setTipoPublicacion(req.tipoPublicacion());
        p.setActivo(true);
        p.setFechaRegistro(Instant.now());

        puestoEmpresaService.crearPuesto(p);

        if (req.requisitos() != null) {
            for (RequisitoRequest r : req.requisitos()) {
                PuestocaracteristicaId pcId = new PuestocaracteristicaId();
                pcId.setPuestoId(p.getId());
                pcId.setCaracteristicaId(r.caracteristicaId());

                Puestocaracteristica pc = new Puestocaracteristica();
                pc.setId(pcId);
                pc.setPuesto(p);

                Caracteristica c = new Caracteristica();
                c.setId(r.caracteristicaId());
                pc.setCaracteristica(c);
                pc.setNivelDeseado(r.nivelDeseado());

                puestoPublicoService.guardarRequisito(pc);
            }
        }

        return ResponseEntity.ok(Map.of("message", "Puesto creado"));
    }

    record CandidatoDTO(Long id, String nombre, String primerApellido,
                        String identificacion, String nacionalidad,
                        String telefono, String lugarResidencia,
                        boolean tieneCv, int cumplidos, int total, String porcentaje) {}

    @GetMapping("/candidatos/{puestoId}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<CandidatoDTO>> buscarCandidatos(@PathVariable Long puestoId) {
        Puesto puesto = puestoPublicoService.buscarPuestoPorId(puestoId);
        List<Puestocaracteristica> requisitos = puestoPublicoService.requisitosDelPuesto(puestoId);
        List<Oferente> todosOferentes = oferenteRepository.findByEstadoAprobacion("APROBADO");

        List<CandidatoDTO> candidatos = new java.util.ArrayList<>();

        for (Oferente o : todosOferentes) {
            List<Oferentehabilidad> habilidades = habilidadRepository.findByOferente(o);

            int cumplidos = 0;
            for (Puestocaracteristica req : requisitos) {
                for (Oferentehabilidad hab : habilidades) {
                    if (hab.getCaracteristica().getId().equals(req.getCaracteristica().getId())
                            && hab.getNivel() >= req.getNivelDeseado()) {
                        cumplidos++;
                        break;
                    }
                }
            }

            if (cumplidos > 0 || requisitos.isEmpty()) {
                double porcentaje = requisitos.isEmpty() ? 100.0
                        : (cumplidos * 100.0 / requisitos.size());

                candidatos.add(new CandidatoDTO(
                        o.getId(),
                        o.getNombre(),
                        o.getPrimerApellido(),
                        o.getIdentificacion(),
                        o.getNacionalidad(),
                        o.getTelefono(),
                        o.getLugarResidencia(),
                        o.getCvPath() != null && !o.getCvPath().isBlank(),
                        cumplidos,
                        requisitos.size(),
                        String.format("%.0f%%", porcentaje)
                ));
            }
        }

        candidatos.sort((a, b) -> b.porcentaje().compareTo(a.porcentaje()));
        return ResponseEntity.ok(candidatos);
    }

    @PostMapping("/puestos/{id}/desactivar")
    @Transactional
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        puestoEmpresaService.desactivarPuesto(id);
        return ResponseEntity.ok(Map.of("message", "Puesto desactivado"));
    }

    record CrearPuestoRequest(String descripcionGeneral, BigDecimal salarioOfrecido,
                              String tipoPublicacion, List<RequisitoRequest> requisitos) {}
    record RequisitoRequest(Long caracteristicaId, Integer nivelDeseado) {}

    private Usuario getUsuario(Authentication auth) {
        return ((com.proyecto1.proyecto1progra4.Security.UserDetailsImp) auth.getPrincipal()).getUsuario();
    }
}
