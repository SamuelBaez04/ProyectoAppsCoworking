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

import com.coworking.project.businessLayer.dto.ReservaCreateDTO;
import com.coworking.project.businessLayer.dto.ReservaDTO;
import com.coworking.project.businessLayer.dto.ReservaUpdateDTO;
import com.coworking.project.businessLayer.service.ReservaService;
import com.coworking.project.util.ReservaEstado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reservas", description = "Operaciones CRUD para gestión de reservas")
public class ReservaController {
    private final ReservaService reservaService;

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Permite registrar una nueva reserva en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReservaDTO> crearReserva(
            @Parameter(description = "Datos de la reserva a crear", required = true) @RequestBody ReservaCreateDTO createDTO) {
        log.info("POST /api/reservas - Creando nueva reserva para el recurso {} y usuario {}",
                createDTO.getIdRecurso(), createDTO.getUsuarioReserva());
        try {
            ReservaDTO createdReserva = reservaService.crearReserva(createDTO);
            log.info("Reserva creada exitosamente con id: {}", createdReserva.getIdReserva());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReserva);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear reserva: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{idReserva}")
    @Operation(summary = "Actualizar una reserva existente", description = "Permite actualizar los datos de una reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaDTO> actualizarReserva(
            @Parameter(description = "ID de la reserva a actualizar", required = true, example = "1") @PathVariable int idReserva,
            @Parameter(description = "Datos actualizados de la reserva", required = true) @RequestBody ReservaUpdateDTO updateDTO) {
        log.info("PUT /api/reservas/{} - Actualizando reserva", idReserva);
        try {
            ReservaDTO updatedReserva = reservaService.actualizarReserva(idReserva, updateDTO);
            log.info("Reserva actualizada exitosamente: {}", idReserva);
            return ResponseEntity.ok(updatedReserva);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                log.warn("Reserva no encontrada para actualizar id: {}", idReserva);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar reserva id {}: {}", idReserva, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{idReserva}")
    @Operation(summary = "Eliminar una reserva", description = "Elimina una reserva del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Void> eliminarReserva(
            @Parameter(description = "ID de la reserva", required = true, example = "1") @PathVariable int idReserva) {
        log.info("DELETE /api/reservas/{} - Eliminando reserva", idReserva);
        try {
            reservaService.eliminarReserva(idReserva);
            log.info("Reserva eliminada exitosamente: {}", idReserva);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                log.warn("Reserva no encontrada para eliminar id: {}", idReserva);
                return ResponseEntity.notFound().build();
            }
            log.error("Error al intentar eliminar la reserva id {}: {}", idReserva, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{idReserva}")
    @Operation(summary = "Buscar reserva por ID", description = "Obtiene una reserva y muestra su información")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaDTO> obtenerReservaPorId(
            @Parameter(description = "ID de la reserva", required = true, example = "1") @PathVariable int idReserva) {
        log.debug("GET /api/reservas/{} - Buscando reserva por id", idReserva);
        try {
            ReservaDTO reserva = reservaService.obtenerReservaPorId(idReserva);
            log.info("Reserva encontrada: {}", idReserva);
            return ResponseEntity.ok(reserva);
        } catch (RuntimeException e) {
            log.warn("Reserva no encontrada con id: {}", idReserva);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas las reservas", description = "Obtiene una lista de todas las reservas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaDTO.class)))
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        log.debug("GET /api/reservas - Listando todas las reservas");
        List<ReservaDTO> reservas = reservaService.listarReservas();
        log.debug("Total de reservas encontradas: {}", reservas.size());
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar reservas por estado", description = "Obtiene las reservas que coinciden con el estado especificado")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaDTO.class)))
    public ResponseEntity<List<ReservaDTO>> listarPorEstado(
            @Parameter(description = "Estado de la reserva", example = "pendiente") @PathVariable ReservaEstado estado) {
        log.debug("GET /api/reservas/estado/{} - Listando reservas por estado", estado);
        List<ReservaDTO> reservas = reservaService.buscarPorEstado(estado);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/rango")
    @Operation(summary = "Buscar reservas por rango de fechas", description = "Filtra reservas entre una fecha de inicio y una fecha fin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas"),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas")
    })
    public ResponseEntity<List<ReservaDTO>> listarPorRangoFechas(
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)", required = true) @RequestParam LocalDate fechaInicio,
            @Parameter(description = "Fecha de fin (YYYY-MM-DD)", required = true) @RequestParam LocalDate fechaFin) {
        log.debug("GET /api/reservas/rango - Buscando reservas entre {} y {}", fechaInicio, fechaFin);
        if (fechaFin.isBefore(fechaInicio)) {
            log.warn("Fechas inválidas: fechaFin antes de fechaInicio");
            return ResponseEntity.badRequest().build();
        }
        List<ReservaDTO> reservas = reservaService.buscarPorRangoDeFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(reservas);
    }

}
