package com.proyecto1.proyecto1progra4.Data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroDtos {

    public record RegistroEmpresaRequest(
            @NotBlank @Email String correo,
            @NotBlank @Size(min = 3, max = 100) String clave,
            @NotBlank @Size(max = 120) String nombre,
            @NotBlank @Size(max = 160) String localizacion,
            @NotBlank @Size(max = 30) String telefono,
            String descripcion
    ) {}

    public record RegistroOferenteRequest(
            @NotBlank @Email String correo,
            @NotBlank @Size(min = 3, max = 100) String clave,
            @NotBlank @Size(max = 30) String identificacion,
            @NotBlank @Size(max = 80) String nombre,
            @NotBlank @Size(max = 80) String primerApellido,
            @NotBlank @Size(max = 60) String nacionalidad,
            @NotBlank @Size(max = 30) String telefono,
            @NotBlank @Size(max = 160) String lugarResidencia
    ) {}
}
