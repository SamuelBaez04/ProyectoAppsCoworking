package com.coworking.project.dto.response;

import lombok.Data;

/**
 * DTO para respuestas de usuario
 */
@Data
public class UsuarioResponseDto {
    
    private Integer cedula;
    private String nombreCompleto;
    private String nombreRol;
    private Integer idRol;
    private String direccion;
    private String telefono;
    private String email;
    
    // Constructor completo
    public UsuarioResponseDto(Integer cedula, String nombreCompleto, String nombreRol, 
                             Integer idRol, String direccion, String telefono, String email) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.nombreRol = nombreRol;
        this.idRol = idRol;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }
    
    // Constructor vacío (requerido por Lombok)
    public UsuarioResponseDto() {
    }
}