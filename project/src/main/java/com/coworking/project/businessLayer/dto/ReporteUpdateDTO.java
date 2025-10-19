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
@Schema(description = "Datos necesarios para actualizar un Reporte")
public class ReporteUpdateDTO {
    @Schema(description = "Título del reporte", example = "Reporte de Ingresos Actualizado", required = false)
    private String titulo;

    @Schema(description = "Descripción del reporte", example = "Informe actualizado de los ingresos del mes", required = false)
    private String descripcion;

    @Schema(description = "Fecha de generación del reporte", example = "2025-10-18", required = false)
    private LocalDate fechaGeneracion;

    @Schema(description = "Tipo del reporte", example = "INGRESOS", required = false)
    private TipoReporte tipoReporte;

    @Schema(description = "Total de registros analizados en el reporte", example = "48", required = false)
    private int totalRegistros;

    @Schema(description = "Monto total calculado en el reporte", example = "13000.75", required = false)
    private double montoTotal;

}
