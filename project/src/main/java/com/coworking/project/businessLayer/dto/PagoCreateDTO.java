package com.coworking.project.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para registrar un nuevo pago")
public class PagoCreateDTO {

    @Schema(description = "Id de la reserva a la que pertenece el pago", example = "10", required = true)
    private Integer idReserva;

    @Schema(description = "Monto del pago", example = "150.0", required = true)
    private double monto;

    @Schema(description = "Fecha en la que se realiza el pago", example = "2025-10-10", required = true)
    private LocalDate fechaPago;

    @Schema(description = "Método de pago utilizado", example = "Transferencia bancaria", required = true, maxLength = 50)
    private String metodoPago;
}
