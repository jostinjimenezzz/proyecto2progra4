package com.proyecto1.proyecto1progra4.Logic;

import com.proyecto1.proyecto1progra4.Data.*;
import com.proyecto1.proyecto1progra4.Repositories.CaracteristicaRepository;
import com.proyecto1.proyecto1progra4.Repositories.OferenteRepository;
import com.proyecto1.proyecto1progra4.Repositories.OferentehabilidadRepository;
import com.proyecto1.proyecto1progra4.Security.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ControllerOferente {

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private OferentehabilidadRepository habilidadRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    private static final String CARPETA_CVS = "src/main/resources/static/cvs/";

    @GetMapping("/Oferente/dashboard")
    public String oferente() { return "Oferente/dashboard"; }

    @GetMapping("/Oferente/mi_cv")
    public String verCV(@AuthenticationPrincipal UserDetailsImp userDetails, Model model) {
        Oferente oferente = oferenteRepository.findByUsuario(userDetails.getUsuario())
                .orElse(null);

        if (oferente != null && oferente.getCvNombreOriginal() != null) {
            model.addAttribute("cvActual", oferente.getCvNombreOriginal());
        } else {
            model.addAttribute("cvActual", null);
        }

        return "Oferente/mi_cv";
    }

    @PostMapping("/Oferente/mi_cv/subir")
    public String subirCV(@AuthenticationPrincipal UserDetailsImp userDetails,
                          @RequestParam("archivo") MultipartFile archivo,
                          Model model) {

        if (archivo.isEmpty()) {
            model.addAttribute("error", "Seleccioná un archivo PDF.");
            return "Oferente/mi_cv";
        }

        if (!archivo.getContentType().equals("application/pdf")) {
            model.addAttribute("error", "Solo se permiten archivos PDF.");
            return "Oferente/mi_cv";
        }

        try {
            Oferente oferente = oferenteRepository.findByUsuario(userDetails.getUsuario())
                    .orElseThrow(() -> new RuntimeException("Oferente no encontrado"));

            if (oferente.getCvPath() != null) {
                File anterior = new File(oferente.getCvPath());
                if (anterior.exists()) anterior.delete();
            }

            String nombreArchivo = "oferente_" + oferente.getId() + ".pdf";
            Path destino = Paths.get(CARPETA_CVS + nombreArchivo);
            Files.createDirectories(destino.getParent());
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            oferente.setCvPath(CARPETA_CVS + nombreArchivo);
            oferente.setCvNombreOriginal(archivo.getOriginalFilename());
            oferente.setCvMime(archivo.getContentType());
            oferente.setCvFechaSubida(Instant.now());
            oferenteRepository.save(oferente);

            model.addAttribute("mensaje", "CV subido correctamente.");
            model.addAttribute("cvActual", archivo.getOriginalFilename());

        } catch (IOException e) {
            model.addAttribute("error", "Error al guardar el archivo.");
        }

        return "Oferente/mi_cv";
    }

    @GetMapping("/Oferente/mi_cv/descargar")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> descargarCV(
            @AuthenticationPrincipal UserDetailsImp userDetails) throws IOException {

        Oferente oferente = oferenteRepository.findByUsuario(userDetails.getUsuario())
                .orElseThrow(() -> new RuntimeException("Oferente no encontrado"));

        Path path = Paths.get(oferente.getCvPath());
        org.springframework.core.io.Resource resource =
                new org.springframework.core.io.UrlResource(path.toUri());

        return org.springframework.http.ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + oferente.getCvNombreOriginal() + "\"")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(resource);
    }


    @GetMapping("/Oferente/habilidades")
    public String verHabilidades(@AuthenticationPrincipal UserDetailsImp userDetails,
                                 @RequestParam(value = "actualId", required = false) Long actualId,
                                 Model model) {

        Oferente oferente = oferenteRepository.findByUsuario(userDetails.getUsuario())
                .orElse(null);

        List<Oferentehabilidad> misHabilidades = habilidadRepository.findByOferente(oferente);
        model.addAttribute("misHabilidades", misHabilidades);

        if (actualId == null) {
            List<Caracteristica> raices = caracteristicaRepository.findRaices();
            model.addAttribute("subcategorias", raices);
            model.addAttribute("ruta", new ArrayList<>());
            model.addAttribute("actualId", null);
        } else {
            List<Caracteristica> hijos = caracteristicaRepository.findByIdPadre(actualId);
            model.addAttribute("subcategorias", hijos);

            List<Caracteristica> ruta = new ArrayList<>();
            Caracteristica actual = caracteristicaRepository.findById(actualId).orElse(null);
            while (actual != null) {
                ruta.add(0, actual);
                actual = actual.getIdPadre();
            }
            model.addAttribute("ruta", ruta);
            model.addAttribute("actualId", actualId);
        }

        model.addAttribute("todasCaracteristicas", caracteristicaRepository.findAll());

        Set<Long> conHijos = caracteristicaRepository.findAll().stream()
                .filter(c -> c.getIdPadre() != null)
                .map(c -> c.getIdPadre().getId())
                .collect(java.util.stream.Collectors.toSet());
        model.addAttribute("conHijos", conHijos);

        return "Oferente/habilidades";
    }

    @PostMapping("/Oferente/habilidades/agregar")
    public String agregarHabilidad(@AuthenticationPrincipal UserDetailsImp userDetails,
                                   @RequestParam("caracteristicaId") Long caracteristicaId,
                                   @RequestParam("nivel") Integer nivel,
                                   @RequestParam(value = "actualId", required = false) Long actualId) {

        Oferente oferente = oferenteRepository.findByUsuario(userDetails.getUsuario())
                .orElseThrow(() -> new RuntimeException("Oferente no encontrado"));

        OferentehabilidadId habId = new OferentehabilidadId();
        habId.setOferenteId(oferente.getId());
        habId.setCaracteristicaId(caracteristicaId);

        Oferentehabilidad habilidad = new Oferentehabilidad();
        habilidad.setId(habId);
        habilidad.setOferente(oferente);
        habilidad.setCaracteristica(caracteristicaRepository.findById(caracteristicaId)
                .orElseThrow(() -> new RuntimeException("Característica no encontrada")));
        habilidad.setNivel(nivel);

        habilidadRepository.save(habilidad);

        return "redirect:/Oferente/habilidades" + (actualId != null ? "?actualId=" + actualId : "");
    }

    @PostMapping("/Oferente/habilidades/eliminar")
    public String eliminarHabilidad(@RequestParam("oferenteId") Long oferenteId,
                                    @RequestParam("caracteristicaId") Long caracteristicaId,
                                    @RequestParam(value = "actualId", required = false) Long actualId) {

        OferentehabilidadId habId = new OferentehabilidadId();
        habId.setOferenteId(oferenteId);
        habId.setCaracteristicaId(caracteristicaId);

        habilidadRepository.deleteById(habId);

        return "redirect:/Oferente/habilidades" + (actualId != null ? "?actualId=" + actualId : "");
    }

}