package com.coworking.project.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del pago")
public class PagoDTO {

    @Schema(description = "Id único del pago, auto incrementado", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idPago;

    @Schema(description = "Id de la reserva asociada al pago", example = "10", required = true)
    private Integer idReserva;

    @Schema(description = "Monto del pago realizado", example = "150.0", required = true)
    private double monto;

    @Schema(description = "Fecha en la que se realizó el pago", example = "2025-10-10", required = true)
    private LocalDate fechaPago;

    @Schema(description = "Método de pago utilizado", example = "Tarjeta de crédito", required = true, maxLength = 50)
    private String metodoPago;
}
