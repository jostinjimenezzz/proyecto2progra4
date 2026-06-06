package com.proyecto1.proyecto1progra4.Controllers;

import com.proyecto1.proyecto1progra4.Data.*;
import com.proyecto1.proyecto1progra4.Repositories.*;
import com.proyecto1.proyecto1progra4.Security.UserDetailsImp;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oferente")
public class OferenteController {

    private final OferenteRepository oferenteRepository;
    private final OferentehabilidadRepository habilidadRepository;
    private final CaracteristicaRepository caracteristicaRepository;

    private static final String CARPETA_CVS = "cvs/";

    public OferenteController(OferenteRepository oferenteRepository,
                              OferentehabilidadRepository habilidadRepository,
                              CaracteristicaRepository caracteristicaRepository) {
        this.oferenteRepository = oferenteRepository;
        this.habilidadRepository = habilidadRepository;
        this.caracteristicaRepository = caracteristicaRepository;
    }

    record CaracteristicaDTO(Long id, String nombre, Long idPadre) {}
    record HabilidadDTO(Long caracteristicaId, String nombre, Integer nivel) {}

    @GetMapping("/caracteristicas")
    @Transactional(readOnly = true)
    public ResponseEntity<List<CaracteristicaDTO>> caracteristicas() {
        List<CaracteristicaDTO> lista = caracteristicaRepository.findAll()
                .stream()
                .map(c -> new CaracteristicaDTO(
                        c.getId(),
                        c.getNombre(),
                        c.getIdPadre() != null ? c.getIdPadre().getId() : null
                ))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/habilidades")
    @Transactional(readOnly = true)
    public ResponseEntity<List<HabilidadDTO>> misHabilidades(Authentication auth) {
        Oferente oferente = getOferente(auth);
        List<HabilidadDTO> lista = habilidadRepository.findByOferente(oferente)
                .stream()
                .map(h -> new HabilidadDTO(
                        h.getCaracteristica().getId(),
                        h.getCaracteristica().getNombre(),
                        h.getNivel()
                ))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/habilidades/agregar")
    @Transactional
    public ResponseEntity<?> agregarHabilidad(Authentication auth,
                                              @RequestBody HabilidadRequest req) {
        Oferente oferente = getOferente(auth);

        OferentehabilidadId habId = new OferentehabilidadId();
        habId.setOferenteId(oferente.getId());
        habId.setCaracteristicaId(req.caracteristicaId());

        Oferentehabilidad habilidad = new Oferentehabilidad();
        habilidad.setId(habId);
        habilidad.setOferente(oferente);
        habilidad.setCaracteristica(caracteristicaRepository.findById(req.caracteristicaId())
                .orElseThrow(() -> new RuntimeException("Característica no encontrada")));
        habilidad.setNivel(req.nivel());

        habilidadRepository.save(habilidad);
        return ResponseEntity.ok(Map.of("message", "Habilidad agregada"));
    }

    @DeleteMapping("/habilidades/{caracteristicaId}")
    @Transactional
    public ResponseEntity<?> eliminarHabilidad(Authentication auth,
                                               @PathVariable Long caracteristicaId) {
        Oferente oferente = getOferente(auth);

        OferentehabilidadId habId = new OferentehabilidadId();
        habId.setOferenteId(oferente.getId());
        habId.setCaracteristicaId(caracteristicaId);

        habilidadRepository.deleteById(habId);
        return ResponseEntity.ok(Map.of("message", "Habilidad eliminada"));
    }

    @PostMapping("/cv/subir")
    @Transactional
    public ResponseEntity<?> subirCv(Authentication auth,
                                     @RequestParam("archivo") MultipartFile archivo) throws IOException {
        if (archivo.isEmpty())
            return ResponseEntity.badRequest().body("Archivo vacío");

        if (!archivo.getContentType().equals("application/pdf"))
            return ResponseEntity.badRequest().body("Solo se permiten PDFs");

        Oferente oferente = getOferente(auth);

        String nombreArchivo = "oferente_" + oferente.getId() + ".pdf";
        Path destino = Paths.get(CARPETA_CVS + nombreArchivo);
        Files.createDirectories(destino.getParent());
        Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

        oferente.setCvPath(CARPETA_CVS + nombreArchivo);
        oferente.setCvNombreOriginal(archivo.getOriginalFilename());
        oferente.setCvMime(archivo.getContentType());
        oferente.setCvFechaSubida(Instant.now());
        oferenteRepository.save(oferente);

        return ResponseEntity.ok(Map.of("message", "CV subido correctamente"));
    }

    @GetMapping("/cv/ver")
    @Transactional(readOnly = true)
    public ResponseEntity<org.springframework.core.io.Resource> verMiCv(Authentication auth) throws java.io.IOException {
        Oferente oferente = getOferente(auth);

        if (oferente.getCvPath() == null || oferente.getCvPath().isBlank())
            return ResponseEntity.notFound().build();

        java.nio.file.Path path = java.nio.file.Paths.get(oferente.getCvPath());
        if (!java.nio.file.Files.exists(path))
            return ResponseEntity.notFound().build();

        org.springframework.core.io.Resource resource =
                new org.springframework.core.io.UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=\"mi_cv.pdf\"")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/cv/info")
    @Transactional(readOnly = true)
    public ResponseEntity<?> infoCv(Authentication auth) {
        Oferente oferente = getOferente(auth);
        if (oferente.getCvPath() == null)
            return ResponseEntity.ok(Map.of("tieneCv", false));
        return ResponseEntity.ok(Map.of(
                "tieneCv", true,
                "nombreOriginal", oferente.getCvNombreOriginal()
        ));
    }

    record HabilidadRequest(Long caracteristicaId, Integer nivel) {}

    private Oferente getOferente(Authentication auth) {
        Usuario usuario = ((UserDetailsImp) auth.getPrincipal()).getUsuario();
        return oferenteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Oferente no encontrado"));
    }
}
