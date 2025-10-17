package com.coworking.project.businessLayer.dto;

import java.time.LocalDate;

import com.coworking.project.util.TipoReporte;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear un nuevo Reporte")
public class ReporteCreateDTO {

    @Schema(description = "Título del reporte", example = "Reporte de Ingresos Mensual", required = true)
    private String titulo;
    
    @Schema(description = "Descripción del reporte", example = "Informe detallado...", required = false)
    private String descripcion;
    
    @Schema(description = "Fecha de generación del reporte", example = "2025-10-17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaGeneracion;
    
    @NotNull(message = "tipoReporte es requerido y debe ser uno de: OCUPACION, INGRESOS, USO_POR_USUARIO, RESERVAS, GENERAL")
    @Schema(description = "Tipo del reporte", example = "INGRESOS", required = true)
    private TipoReporte tipoReporte;
    
    @Schema(description = "Total de registros analizados", example = "45", required = false)
    private Integer totalRegistros;
    
    @Schema(description = "Monto total calculado", example = "12500.50", required = false)
    private Double montoTotal;
}
