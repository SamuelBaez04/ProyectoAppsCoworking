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
@Schema(description = "Datos para crear una nueva Reserva")

public class ReservaCreateDTO {
    @Schema(description = "Fecha de inicio de la reserva", example = "2025-10-15", required = true)
    private LocalDate fechaInicio;

    @Schema(description = "Hora de inicio de la reserva", example = "09:00", required = true)
    private LocalTime horaInicio;

    @Schema(description = "Fecha de fin de la reserva", example = "2025-10-15", required = true)
    private LocalDate fechaFin;

    @Schema(description = "Hora de fin de la reserva", example = "11:00", required = true)
    private LocalTime horaFin;

    @Schema(description = "Estado de la reserva", example = "pendiente", required = false)
    private ReservaEstado estado;

    @Schema(description = "ID del recurso que se desea reservar", example = "3", required = true)
    private Long idRecurso;

    @Schema(description = "Cédula del usuario que realiza la reserva", example = "123456789", required = true)
    private Integer usuarioReserva;

}
