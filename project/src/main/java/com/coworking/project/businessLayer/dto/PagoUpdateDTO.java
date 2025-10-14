package com.coworking.project.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para actualizar un pago existente")
public class PagoUpdateDTO {

    @Schema(description = "Monto actualizado del pago", example = "200.0", required = false)
    private Double monto;

    @Schema(description = "Fecha actualizada del pago", example = "2025-11-01", required = false)
    private LocalDate fechaPago;

    @Schema(description = "Método de pago actualizado", example = "Efectivo", required = false, maxLength = 50)
    private String metodoPago;
}
