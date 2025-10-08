package com.coworking.project.businessLayer.dto;


import java.time.LocalDate;

import com.coworking.project.util.RecursoEstado;
import com.coworking.project.util.TipoRecurso;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear un nuevo Recurso")
public class RecursoCreateDTO {

    @Schema(description = "Tipo del recurso", example = "SALA_REUNIONES", required = true, maxLength = 50)
    private TipoRecurso tipoRecurso;

    @Schema(description = "Nombre del recurso", example = "Sala A - Primer Piso", required = true, maxLength = 50)
    private String nombreRecurso;

    @Schema(description = "Valor por hora del recurso", example = "50.0", required = true)
    private double valorHora;

    @Schema(description = "Descripcion del recurso", example = "Sala de reuniones con capacidad para 10 personas", required = false, maxLength = 500)
    private String descripcion;

    @Schema(description = "Fecha de inicio de la tarifa del recurso", example = "2023-01-01", required = true)
    private LocalDate fechaInicioTarifa;

    @Schema(description = "Fecha de fin de la tarifa del recurso", example = "2023-12-31", required = true)
    private LocalDate fechaFinTarifa;

    @Schema(description = "Estado del recurso", example = "activo", required = false)
    private RecursoEstado  recursoEstado;

}
