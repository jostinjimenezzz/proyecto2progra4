package com.proyecto1.proyecto1progra4.Logic;

import com.proyecto1.proyecto1progra4.Data.*;
import com.proyecto1.proyecto1progra4.Services.CaracteristicaService;
import com.proyecto1.proyecto1progra4.Services.PuestoPublicoService;
import com.proyecto1.proyecto1progra4.Services.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiPublicController {

    @Autowired private PuestoPublicoService puestoPublicoService;
    @Autowired private CaracteristicaService caracteristicaService;
    @Autowired private RegistroService registroService;
    @Autowired private PasswordEncoder passwordEncoder;

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

    @GetMapping("/puestos/recientes")
    public ResponseEntity<List<PuestoDTO>> recientes() {
        List<Puesto> lista = puestoPublicoService.top5PuestosPublicosRecientes();
        List<PuestoDTO> result = lista.stream()
                .map(p -> toPuestoDTO(p, puestoPublicoService.requisitosDelPuesto(p.getId())))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/puestos/buscar")
    public ResponseEntity<List<PuestoDTO>> buscar(
            @RequestParam(required = false) List<Long> caracteristicaIds) {
        List<Puesto> resultados = puestoPublicoService.buscarPuestosPublicos(caracteristicaIds);
        List<PuestoDTO> result = resultados.stream()
                .map(p -> toPuestoDTO(p, Collections.emptyList()))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/puestos/detalle/{id}")
    public ResponseEntity<DetalleDTO> detalle(@PathVariable Long id) {
        Puesto puesto = puestoPublicoService.buscarPuestoPorId(id);
        List<Puestocaracteristica> requisitos = puestoPublicoService.requisitosDelPuesto(id);
        List<RequisitoDTO> reqDTOs = requisitos.stream().map(this::toRequisitoDTO).toList();
        PuestoDTO pDTO = toPuestoDTO(puesto, requisitos);
        return ResponseEntity.ok(new DetalleDTO(pDTO, reqDTOs));
    }

    @GetMapping("/caracteristicas")
    public ResponseEntity<List<CaracteristicaDTO>> caracteristicas() {
        List<CaracteristicaDTO> result = caracteristicaService.todasLasCaracteristicas()
                .stream()
                .map(c -> new CaracteristicaDTO(c.getId(), c.getNombre()))
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/registro/empresa")
    public ResponseEntity<String> registrarEmpresa(@RequestBody Map<String, String> body) {
        try {
            Usuario u = new Usuario();
            u.setUsername(body.get("correo"));
            u.setClave(passwordEncoder.encode(body.get("clave")));

            Empresa e = new Empresa();
            e.setNombre(body.get("nombre"));
            e.setLocalizacion(body.get("localizacion"));
            e.setTelefono(body.get("telefono"));
            e.setDescripcion(body.get("descripcion"));

            registroService.registrarEmpresa(u, e);
            return ResponseEntity.ok("OK");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/registro/oferente")
    public ResponseEntity<String> registrarOferente(@RequestBody Map<String, String> body) {
        try {
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
            return ResponseEntity.ok("OK");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
