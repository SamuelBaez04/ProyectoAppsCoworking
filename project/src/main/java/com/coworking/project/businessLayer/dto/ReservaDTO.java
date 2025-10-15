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
@Schema(description = "Información de la reserva")
public class ReservaDTO {

    @Schema(description = "ID único de la reserva", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int idReserva;

    @Schema(description = "Fecha de inicio de la reserva", example = "2025-10-15", required = true)
    private LocalDate fechaInicio;

    @Schema(description = "Hora de inicio de la reserva", example = "09:00", required = true)
    private LocalTime horaInicio;

    @Schema(description = "Fecha de fin de la reserva", example = "2025-10-15", required = true)
    private LocalDate fechaFin;

    @Schema(description = "Hora de fin de la reserva", example = "11:00", required = true)
    private LocalTime horaFin;

    @Schema(description = "Estado actual de la reserva", example = "confirmada", required = true)
    private ReservaEstado estado;

    @Schema(description = "ID del recurso reservado", example = "3", required = true)
    private Long idRecurso;

    @Schema(description = "Cédula del usuario que realizó la reserva", example = "123456789", required = true)
    private Integer usuarioReserva;
}
