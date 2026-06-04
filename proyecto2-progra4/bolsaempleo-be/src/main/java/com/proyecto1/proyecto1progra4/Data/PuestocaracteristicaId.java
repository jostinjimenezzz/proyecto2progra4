package com.proyecto1.proyecto1progra4.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PuestocaracteristicaId implements Serializable {
    private static final long serialVersionUID = -5474660181619954545L;
    @NotNull
    @Column(name = "puesto_id", nullable = false)
    private Long puestoId;

    @NotNull
    @Column(name = "caracteristica_id", nullable = false)
    private Long caracteristicaId;

    public Long getPuestoId() {
        return puestoId;
    }

    public void setPuestoId(Long puestoId) {
        this.puestoId = puestoId;
    }

    public Long getCaracteristicaId() {
        return caracteristicaId;
    }

    public void setCaracteristicaId(Long caracteristicaId) {
        this.caracteristicaId = caracteristicaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PuestocaracteristicaId entity = (PuestocaracteristicaId) o;
        return Objects.equals(this.puestoId, entity.puestoId) &&
                Objects.equals(this.caracteristicaId, entity.caracteristicaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(puestoId, caracteristicaId);
    }

}