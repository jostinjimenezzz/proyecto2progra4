package com.proyecto1.proyecto1progra4.Repositories;

import com.proyecto1.proyecto1progra4.Data.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PuestoRepository extends JpaRepository<Puesto, Long> {

    List<Puesto> findTop5ByTipoPublicacionOrderByFechaRegistroDesc(String tipoPublicacion);

    List<Puesto> findByEmpresaIdOrderByFechaRegistroDesc(Long empresaId);

    @Query
            (value = """
        SELECT MONTH(p.fecha_registro) as mes, COUNT(*) as cantidad
        FROM Puesto p
        WHERE YEAR(p.fecha_registro) = :anio
        GROUP BY MONTH(p.fecha_registro)
        ORDER BY MONTH(p.fecha_registro)
        """, nativeQuery = true)
    List<Object[]> contarPuestosPorMes(@Param("anio") int anio);
}