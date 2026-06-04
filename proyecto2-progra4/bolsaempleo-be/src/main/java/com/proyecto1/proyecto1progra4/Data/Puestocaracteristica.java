package com.proyecto1.proyecto1progra4.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "puestocaracteristica")
public class Puestocaracteristica {
    @EmbeddedId
    private PuestocaracteristicaId id;

    @MapsId("puestoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "puesto_id", nullable = false)
    private Puesto puesto;

    @MapsId("caracteristicaId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caracteristica_id", nullable = false)
    private Caracteristica caracteristica;

    @NotNull
    @Column(name = "nivel_deseado", nullable = false)
    private Integer nivelDeseado;

    public PuestocaracteristicaId getId() {
        return id;
    }

    public void setId(PuestocaracteristicaId id) {
        this.id = id;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
    }

    public Integer getNivelDeseado() {
        return nivelDeseado;
    }

    public void setNivelDeseado(Integer nivelDeseado) {
        this.nivelDeseado = nivelDeseado;
    }

}