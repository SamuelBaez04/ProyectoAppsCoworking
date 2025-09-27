package com.coworking.project.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

/**
 * DTO para solicitudes de creación/actualización de usuario
 */
@Data
public class UsuarioRequestDto {
    
    @NotNull(message = "La cédula es obligatoria")
    @Positive(message = "La cédula debe ser un número positivo")
    private Integer cedula;
    
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombreCompleto;
    
    @NotNull(message = "El rol es obligatorio")
    private Integer idRol;
    
    @Size(max = 150, message = "La dirección no puede exceder 150 caracteres")
    private String direccion;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Pattern(regexp = "^[0-9+\\-\\s]*$", message = "El teléfono solo puede contener números, +, - y espacios")
    private String telefono;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;
}