package com.proyecto1.proyecto1progra4.Controllers;

import com.proyecto1.proyecto1progra4.Data.Puesto;
import com.proyecto1.proyecto1progra4.Data.Puestocaracteristica;
import com.proyecto1.proyecto1progra4.Services.PuestoPublicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/puestos")
public class PuestosController {

    private final PuestoPublicoService puestoPublicoService;

    public PuestosController(PuestoPublicoService puestoPublicoService) {
        this.puestoPublicoService = puestoPublicoService;
    }

    record CaracteristicaDTO(Long id, String nombre) {}
    record RequisitoDTO(CaracteristicaDTO caracteristica, Integer nivelDeseado) {}
    record EmpresaDTO(Long id, String nombre) {}
    record PuestoDTO(Long id, EmpresaDTO empresa, String descripcionGeneral,
                     BigDecimal salarioOfrecido, String tipoPublicacion,
                     List<RequisitoDTO> requisitos) {}
    record DetalleDTO(PuestoDTO puesto, List<RequisitoDTO> requisitos) {}

    private RequisitoDTO toRequisitoDTO(Puestocaracteristica r) {
        CaracteristicaDTO c = new CaracteristicaDTO(
                r.getCaracteristica().getId(),
                r.getCaracteristica().getNombre()
        );
        return new RequisitoDTO(c, r.getNivelDeseado());
    }

    private PuestoDTO toPuestoDTO(Puesto p, List<Puestocaracteristica> requisitos) {
        EmpresaDTO empresa = new EmpresaDTO(p.getEmpresa().getId(), p.getEmpresa().getNombre());
        List<RequisitoDTO> reqs = requisitos.stream().map(this::toRequisitoDTO).toList();
        return new PuestoDTO(p.getId(), empresa, p.getDescripcionGeneral(),
                p.getSalarioOfrecido(), p.getTipoPublicacion(), reqs);
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<PuestoDTO>> recientes() {
        List<Puesto> lista = puestoPublicoService.top5PuestosPublicosRecientes();
        List<PuestoDTO> result = lista.stream()
                .map(p -> toPuestoDTO(p, puestoPublicoService.requisitosDelPuesto(p.getId())))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PuestoDTO>> buscar(@RequestParam(required = false) List<Long> caracteristicaIds) {
        List<Puesto> resultados = puestoPublicoService.buscarPuestosPublicos(caracteristicaIds);
        List<PuestoDTO> result = resultados.stream()
                .map(p -> toPuestoDTO(p, Collections.emptyList()))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<DetalleDTO> detalle(@PathVariable Long id) {
        Puesto puesto = puestoPublicoService.buscarPuestoPorId(id);
        List<Puestocaracteristica> requisitos = puestoPublicoService.requisitosDelPuesto(id);
        List<RequisitoDTO> reqDTOs = requisitos.stream().map(this::toRequisitoDTO).toList();
        PuestoDTO pDTO = toPuestoDTO(puesto, requisitos);
        return ResponseEntity.ok(new DetalleDTO(pDTO, reqDTOs));
    }
}
