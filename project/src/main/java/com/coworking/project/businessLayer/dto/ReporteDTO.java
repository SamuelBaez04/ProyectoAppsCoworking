package com.coworking.project.businessLayer.dto;

import java.time.LocalDate;

import com.coworking.project.util.TipoReporte;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del Reporte")
public class ReporteDTO {
    @Schema(description = "Id único del reporte, auto incrementado", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int idReporte;

    @Schema(description = "Título del reporte", example = "Reporte de Ingresos Mensual", required = true, maxLength = 100)
    private String titulo;

    @Schema(description = "Descripción del reporte", example = "Informe detallado de los ingresos generados durante el mes", required = false, maxLength = 500)
    private String descripcion;

    @Schema(description = "Fecha de generación del reporte", example = "2025-10-17", required = true)
    private LocalDate fechaGeneracion;

    @Schema(description = "Tipo del reporte", example = "INGRESOS", required = true, maxLength = 50)
    private TipoReporte tipoReporte;

    @Schema(description = "Total de registros analizados en el reporte", example = "45", required = false)
    private int totalRegistros;

    @Schema(description = "Monto total calculado en el reporte", example = "12500.50", required = false)
    private double montoTotal;
}
