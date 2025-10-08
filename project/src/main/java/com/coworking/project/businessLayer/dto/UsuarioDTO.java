package com.coworking.project.businessLayer.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informacion del usuario")
public class UsuarioDTO {

    @Schema(description = "Id unico del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int cedula;

    @Schema(description = "Nombre completo del Usuario", example = "Natalia Sabogal Rada", required = true, maxLength = 100)
    private String nombreCompleto;

    @Schema(description = "Coreo Electronico del Usuario", example = "natsa@example.com", required = true, maxLength = 100)
    private String email;

    @Schema(description = "Numero de Telefono del Usuario", example = "3147270020", maxLength = 20)
    private String telefono;

    @Schema(description = "Direccion del usuario", example = "Calle 49B #31-08", maxLength = 150)
    private String direccion;

    @Schema(description = "Id del Rol", example = "1", required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private int idRol;

    

}
