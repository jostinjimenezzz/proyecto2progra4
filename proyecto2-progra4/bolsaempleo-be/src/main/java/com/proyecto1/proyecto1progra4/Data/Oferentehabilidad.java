package com.proyecto1.proyecto1progra4.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "oferentehabilidad")
public class Oferentehabilidad {
    @EmbeddedId
    private OferentehabilidadId id;

    @MapsId("oferenteId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oferente_id", nullable = false)
    private Oferente oferente;

    @MapsId("caracteristicaId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caracteristica_id", nullable = false)
    private Caracteristica caracteristica;

    @NotNull
    @Column(name = "nivel", nullable = false)
    private Integer nivel;

    public OferentehabilidadId getId() {
        return id;
    }

    public void setId(OferentehabilidadId id) {
        this.id = id;
    }

    public Oferente getOferente() {
        return oferente;
    }

    public void setOferente(Oferente oferente) {
        this.oferente = oferente;
    }

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

}