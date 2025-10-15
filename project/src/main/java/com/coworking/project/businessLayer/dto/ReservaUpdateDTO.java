package com.coworking.project.businessLayer.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.coworking.project.util.ReservaEstado;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar una reserva existente")
public class ReservaUpdateDTO {

    @Schema(description = "Fecha de inicio actualizada de la reserva", example = "2025-10-16", required = true)
    private LocalDate fechaInicio;

    @Schema(description = "Hora de inicio actualizada de la reserva", example = "10:00", required = true)
    private LocalTime horaInicio;

    @Schema(description = "Fecha de fin actualizada de la reserva", example = "2025-10-16", required = true)
    private LocalDate fechaFin;

    @Schema(description = "Hora de fin actualizada de la reserva", example = "12:00", required = true)
    private LocalTime horaFin;

    @Schema(description = "Estado actualizado de la reserva", example = "confirmada", required = true)
    private ReservaEstado estado;
}
