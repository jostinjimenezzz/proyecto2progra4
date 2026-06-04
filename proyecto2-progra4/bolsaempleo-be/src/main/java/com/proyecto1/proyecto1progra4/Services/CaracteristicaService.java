package com.proyecto1.proyecto1progra4.Services;

import com.proyecto1.proyecto1progra4.Repositories.CaracteristicaRepository;
import com.proyecto1.proyecto1progra4.Data.Caracteristica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
public class CaracteristicaService {

    @Autowired private CaracteristicaRepository caracteristicas;

    public List<Caracteristica> todasLasCaracteristicas() {
        return caracteristicas.findAll();
    }

    public List<Caracteristica> caracteristicasRaiz() {
        return caracteristicas.findRaices();
    }

    public List<Caracteristica> caracteristicasHijas(Long idPadre) {
        return caracteristicas.findByIdPadre(idPadre);
    }

    @Transactional
    public void crearCaracteristica(String nombre, Long idPadre) {
        Caracteristica c = new Caracteristica();
        c.setNombre(nombre);

        if (idPadre != null) {
            Caracteristica padre = caracteristicas.findById(idPadre)
                    .orElseThrow(() -> new IllegalArgumentException("Padre no existe"));
            c.setIdPadre(padre);
        }

        caracteristicas.save(c);
    }
}