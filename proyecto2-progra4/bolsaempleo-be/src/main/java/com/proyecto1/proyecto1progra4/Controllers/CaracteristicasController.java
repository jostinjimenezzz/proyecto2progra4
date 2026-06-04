package com.proyecto1.proyecto1progra4.Controllers;

import com.proyecto1.proyecto1progra4.Services.CaracteristicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/caracteristicas")
public class CaracteristicasController {

    private final CaracteristicaService caracteristicaService;

    public CaracteristicasController(CaracteristicaService caracteristicaService) {
        this.caracteristicaService = caracteristicaService;
    }

    record CaracteristicaDTO(Long id, String nombre) {}

    @GetMapping
    public ResponseEntity<List<CaracteristicaDTO>> listar() {
        List<CaracteristicaDTO> result = caracteristicaService.todasLasCaracteristicas()
                .stream()
                .map(c -> new CaracteristicaDTO(c.getId(), c.getNombre()))
                .toList();
        return ResponseEntity.ok(result);
    }
}
