package com.proyecto1.proyecto1progra4.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 120)
    @Column(nullable = false, unique = true, length = 120)
    private String username;

    @NotNull
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String clave;

    @NotNull
    @Size(max = 10)
    @Column(nullable = false, length = 10)
    private String rol;

    @NotNull
    @Column(nullable = false)
    private Boolean habilitado = true;

    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion = Instant.now();

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}