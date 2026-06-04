package com.proyecto1.proyecto1progra4.Repositories;

import com.proyecto1.proyecto1progra4.Data.Oferente;
import com.proyecto1.proyecto1progra4.Data.Oferentehabilidad;
import com.proyecto1.proyecto1progra4.Data.OferentehabilidadId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

    public interface OferentehabilidadRepository extends JpaRepository<Oferentehabilidad, OferentehabilidadId> {
        List<Oferentehabilidad> findByOferente(Oferente oferente);
        void deleteById(OferentehabilidadId id);
    }
