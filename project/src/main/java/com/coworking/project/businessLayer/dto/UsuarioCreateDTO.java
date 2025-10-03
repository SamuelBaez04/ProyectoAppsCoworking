package com.coworking.project.businessLayer.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear un nuevo Usuario")
public class UsuarioCreateDTO {

    @Schema(description = "Cedula del Usuario", example = "123456789", required = true)
    private int cedula;

    @Schema(description = "Nombre Completo del Usuario", example = "Samuel Valencia", required = true, maxLength = 100)
    private String nombreCompleto;

    @Schema(description = "Coreo Electronico del Usuario", example = "natsa@example.com", required = true, maxLength = 100)
    private String email;

    @Schema(description = "Numero de Telefono del Usuario", example = "3147270020", maxLength = 20)
    private String telefono;

    @Schema(description = "Direccion del usuario", example = "Calle 49B #31-08", maxLength = 150)
    private String direccion;

    @Schema(description = "Id del Rol", example = "1", required = true)
    private int idRol;

    @Schema(description = "Contraseña del usuario", example = "password", required = true, maxLength = 100)
    private String password;
}
