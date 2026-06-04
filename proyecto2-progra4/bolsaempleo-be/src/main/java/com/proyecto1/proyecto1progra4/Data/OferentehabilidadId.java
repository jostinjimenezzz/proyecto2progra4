package com.proyecto1.proyecto1progra4.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OferentehabilidadId implements Serializable {
    private static final long serialVersionUID = -4159222170462583850L;
    @NotNull
    @Column(name = "oferente_id", nullable = false)
    private Long oferenteId;

    @NotNull
    @Column(name = "caracteristica_id", nullable = false)
    private Long caracteristicaId;

    public Long getOferenteId() {
        return oferenteId;
    }

    public void setOferenteId(Long oferenteId) {
        this.oferenteId = oferenteId;
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
        OferentehabilidadId entity = (OferentehabilidadId) o;
        return Objects.equals(this.oferenteId, entity.oferenteId) &&
                Objects.equals(this.caracteristicaId, entity.caracteristicaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oferenteId, caracteristicaId);
    }

}