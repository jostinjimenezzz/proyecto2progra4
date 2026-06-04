package com.proyecto1.proyecto1progra4.Repositories;

import com.proyecto1.proyecto1progra4.Data.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {

    @Query("SELECT c FROM Caracteristica c WHERE c.idPadre.id = :idPadre")
    List<Caracteristica> findByIdPadre(Long idPadre);

    @Query("SELECT c FROM Caracteristica c WHERE c.idPadre IS NULL")
    List<Caracteristica> findRaices();
}