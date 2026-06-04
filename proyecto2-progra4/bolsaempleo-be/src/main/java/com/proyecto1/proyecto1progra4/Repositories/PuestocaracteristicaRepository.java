package com.proyecto1.proyecto1progra4.Repositories;

import com.proyecto1.proyecto1progra4.Data.Puesto;
import com.proyecto1.proyecto1progra4.Data.Puestocaracteristica;
import com.proyecto1.proyecto1progra4.Data.PuestocaracteristicaId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PuestocaracteristicaRepository
        extends JpaRepository<Puestocaracteristica, PuestocaracteristicaId> {
    List<Puestocaracteristica> findByPuesto(Puesto puesto);
}
