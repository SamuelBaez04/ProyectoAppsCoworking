package com.coworking.project.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un usuario Existente")
public class UsuarioUpdateDTO {

    @Schema(description = "Nombre completo del usuario",
            example = "María González Tech",
            maxLength = 100)
    private String nombreCompleto;

    @Schema(description = "Número de teléfono del vendedor",
            example = "+57-300-1234567",
            maxLength = 20)
    private String telefono;

    @Schema(description = "Dirección física del vendedor",
            example = "Carrera 15 #23-45, Bogotá, Colombia",
            maxLength = 255)
    private String direccion;

}
