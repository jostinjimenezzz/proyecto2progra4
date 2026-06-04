package com.proyecto1.proyecto1progra4.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "oferente")
public class Oferente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "identificacion", nullable = false, length = 30)
    private String identificacion;

    @Size(max = 80)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Size(max = 80)
    @NotNull
    @Column(name = "primer_apellido", nullable = false, length = 80)
    private String primerApellido;

    @Size(max = 60)
    @NotNull
    @Column(name = "nacionalidad", nullable = false, length = 60)
    private String nacionalidad;

    @Size(max = 30)
    @NotNull
    @Column(name = "telefono", nullable = false, length = 30)
    private String telefono;

    @Size(max = 160)
    @NotNull
    @Column(name = "lugar_residencia", nullable = false, length = 160)
    private String lugarResidencia;

    @Size(max = 12)
    @NotNull
    @ColumnDefault("'PENDIENTE'")
    @Column(name = "estado_aprobacion", nullable = false, length = 12)
    private String estadoAprobacion;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

    @Size(max = 255)
    @Column(name = "cv_path")
    private String cvPath;

    @Size(max = 255)
    @Column(name = "cv_nombre_original")
    private String cvNombreOriginal;

    @Size(max = 80)
    @Column(name = "cv_mime", length = 80)
    private String cvMime;

    @Column(name = "cv_fecha_subida")
    private Instant cvFechaSubida;

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

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLugarResidencia() {
        return lugarResidencia;
    }

    public void setLugarResidencia(String lugarResidencia) {
        this.lugarResidencia = lugarResidencia;
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

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public String getCvNombreOriginal() {
        return cvNombreOriginal;
    }

    public void setCvNombreOriginal(String cvNombreOriginal) {
        this.cvNombreOriginal = cvNombreOriginal;
    }

    public String getCvMime() {
        return cvMime;
    }

    public void setCvMime(String cvMime) {
        this.cvMime = cvMime;
    }

    public Instant getCvFechaSubida() {
        return cvFechaSubida;
    }

    public void setCvFechaSubida(Instant cvFechaSubida) {
        this.cvFechaSubida = cvFechaSubida;
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