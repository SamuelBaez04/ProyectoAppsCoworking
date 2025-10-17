package com.coworking.project.presentationLayer.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coworking.project.businessLayer.dto.ReporteCreateDTO;
import com.coworking.project.businessLayer.dto.ReporteDTO;
import com.coworking.project.businessLayer.dto.ReporteUpdateDTO;
import com.coworking.project.businessLayer.service.ReporteService;
import com.coworking.project.util.TipoReporte;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reportes", description = "Operaciones CRUD y consultas específicas para la gestión de reportes generados en el sistema")

public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping
    @Operation(summary = "Crear un nuevo reporte", description = "Permite crear un nuevo reporte en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReporteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReporteDTO> crearReporte(
            @Parameter(description = "Datos del reporte a crear", required = true) @RequestBody ReporteCreateDTO createDTO) {
        log.info("POST /api/reportes - Creando nuevo reporte de tipo: {}", createDTO.getTipoReporte());
        try {
            ReporteDTO created = reporteService.crearReporte(createDTO);
            log.info("Reporte creado exitosamente con ID: {}", created.getIdReporte());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear reporte: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{idReporte}")
    @Operation(summary = "Actualizar un reporte existente", description = "Permite actualizar los datos de un reporte existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReporteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    public ResponseEntity<ReporteDTO> actualizarReporte(
            @Parameter(description = "ID del reporte a actualizar", required = true, example = "1") @PathVariable Integer idReporte,
            @Parameter(description = "Datos actualizados del reporte", required = true) @Valid @RequestBody ReporteUpdateDTO updateDTO) {
        log.info("PUT /api/reportes/{} - Actualizando reporte", idReporte);
        try {
            ReporteDTO updated = reporteService.actualizarReporte(idReporte, updateDTO);
            log.info("Reporte actualizado exitosamente con ID: {}", idReporte);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Reporte no encontrado para actualizar ID: {}", idReporte);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar reporte ID {}: {}", idReporte, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{idReporte}")
    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reporte eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    public ResponseEntity<Void> eliminarReporte(
            @Parameter(description = "ID del reporte a eliminar", required = true, example = "1") @PathVariable Integer idReporte) {
        log.info("DELETE /api/reportes/{} - Eliminando reporte", idReporte);
        try {
            reporteService.eliminarReporte(idReporte);
            log.info("Reporte eliminado exitosamente con ID: {}", idReporte);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Reporte no encontrado para eliminar ID: {}", idReporte);
                return ResponseEntity.notFound().build();
            }
            log.error("Error al eliminar reporte ID {}: {}", idReporte, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{idReporte}")
    @Operation(summary = "Buscar reporte por ID", description = "Obtiene un reporte específico mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReporteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    public ResponseEntity<ReporteDTO> obtenerReportePorId(
            @Parameter(description = "ID del reporte", required = true, example = "1") @PathVariable Integer idReporte) {
        log.debug("GET /api/reportes/{} - Buscando reporte por ID", idReporte);
        try {
            ReporteDTO reporte = reporteService.obtenerReportePorId(idReporte);
            log.info("Reporte encontrado con ID: {}", idReporte);
            return ResponseEntity.ok(reporte);
        } catch (RuntimeException e) {
            log.warn("Reporte no encontrado con ID: {}", idReporte);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar todos los reportes", description = "Obtiene una lista de todos los reportes registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reportes obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReporteDTO.class)))
    })
    public ResponseEntity<List<ReporteDTO>> listarReportes() {
        log.debug("GET /api/reportes - Listando todos los reportes");
        List<ReporteDTO> reportes = reporteService.listarReportes();
        log.debug("Total de reportes encontrados: {}", reportes.size());
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/tipo/{tipoReporte}")
    @Operation(summary = "Buscar reportes por tipo", description = "Obtiene todos los reportes filtrados por tipo")
    public ResponseEntity<List<ReporteDTO>> obtenerPorTipo(
            @Parameter(description = "Tipo de reporte", required = true, example = "FINANCIERO") @PathVariable TipoReporte tipoReporte) {
        log.debug("GET /api/reportes/tipo/{} - Buscando reportes por tipo", tipoReporte);
        List<ReporteDTO> reportes = reporteService.obtenerReportesPorTipo(tipoReporte);
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/fecha")
    @Operation(summary = "Buscar reportes por fecha", description = "Obtiene todos los reportes generados en una fecha específica")
    public ResponseEntity<List<ReporteDTO>> obtenerPorFecha(
            @Parameter(description = "Fecha de generación", example = "2025-10-17") @RequestParam LocalDate fechaGeneracion) {
        log.debug("GET /api/reportes/fecha?fechaGeneracion={} - Buscando reportes por fecha", fechaGeneracion);
        List<ReporteDTO> reportes = reporteService.listarReportesPorFecha(fechaGeneracion);
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/rango-fechas")
    @Operation(summary = "Buscar reportes por rango de fechas", description = "Obtiene todos los reportes generados entre dos fechas dadas")
    public ResponseEntity<List<ReporteDTO>> obtenerPorRangoDeFechas(
            @Parameter(description = "Fecha de inicio del rango", example = "2025-10-01") @RequestParam LocalDate fechaInicio,
            @Parameter(description = "Fecha de fin del rango", example = "2025-10-17") @RequestParam LocalDate fechaFin) {
        log.debug("GET /api/reportes/rango-fechas?fechaInicio={}&fechaFin={} - Buscando reportes por rango",
                fechaInicio, fechaFin);
        List<ReporteDTO> reportes = reporteService.listarReportesPorRangoDeFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(reportes);
    }
}
