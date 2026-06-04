package com.proyecto1.proyecto1progra4.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "empresa")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 120)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Size(max = 160)
    @NotNull
    @Column(name = "localizacion", nullable = false, length = 160)
    private String localizacion;

    @Size(max = 30)
    @NotNull
    @Column(name = "telefono", nullable = false, length = 30)
    private String telefono;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Size(max = 12)
    @NotNull
    @ColumnDefault("'PENDIENTE'")
    @Column(name = "estado_aprobacion", nullable = false, length = 12)
    private String estadoAprobacion;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

    @Column(name = "fecha_aprobacion")
    private Instant fechaAprobacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprobado_por_admin_id")
    private Admin aprobadoPorAdmin;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstadoAprobacion() {
        return estadoAprobacion;
    }

    public void setEstadoAprobacion(String estadoAprobacion) {
        this.estadoAprobacion = estadoAprobacion;
    }

    public Instant getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Instant getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Instant fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Admin getAprobadoPorAdmin() {
        return aprobadoPorAdmin;
    }

    public void setAprobadoPorAdmin(Admin aprobadoPorAdmin) {
        this.aprobadoPorAdmin = aprobadoPorAdmin;
    }

}