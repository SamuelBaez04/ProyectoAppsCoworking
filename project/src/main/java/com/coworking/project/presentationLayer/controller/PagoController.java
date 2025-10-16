package com.coworking.project.presentationLayer.controller;

import com.coworking.project.businessLayer.dto.PagoCreateDTO;
import com.coworking.project.businessLayer.dto.PagoDTO;
import com.coworking.project.businessLayer.dto.PagoUpdateDTO;
import com.coworking.project.businessLayer.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para la gestión de pagos.
 */
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pagos", description = "Operaciones CRUD y de consulta para la gestión de pagos")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    @Operation(
            summary = "Registrar un nuevo pago",
            description = "Permite registrar un nuevo pago asociado a una reserva"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<PagoDTO> registrarPago(
            @Parameter(description = "Datos del pago a registrar", required = true)
            @RequestBody PagoCreateDTO createDTO
    ) {
        log.info("POST /api/pagos - Registrando nuevo pago para reserva: {}", createDTO.getIdReserva());
        try {
            PagoDTO createdPago = pagoService.crearPago(createDTO);
            log.info("Pago registrado exitosamente con ID: {}", createdPago.getIdPago());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPago);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al registrar pago: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{idPago}")
    @Operation(
            summary = "Actualizar información de un pago",
            description = "Permite modificar los datos de un pago existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<PagoDTO> actualizarPago(
            @Parameter(description = "ID del pago a actualizar", required = true, example = "1")
            @PathVariable int idPago,
            @Parameter(description = "Datos actualizados del pago", required = true)
            @RequestBody PagoUpdateDTO updateDTO
    ) {
        log.info("PUT /api/pagos/{} - Actualizando pago", idPago);
        try {
            PagoDTO updatedPago = pagoService.actualizarPago(idPago, updateDTO);
            log.info("Pago actualizado exitosamente: {}", idPago);
            return ResponseEntity.ok(updatedPago);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Pago no encontrado para actualizar: {}", idPago);
                return ResponseEntity.notFound().build();
            }
            log.error("Error al actualizar pago {}: {}", idPago, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{idPago}")
    @Operation(
            summary = "Eliminar un pago",
            description = "Elimina un pago del sistema según su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> eliminarPago(
            @Parameter(description = "ID del pago a eliminar", required = true, example = "1")
            @PathVariable int idPago
    ) {
        log.info("DELETE /api/pagos/{} - Eliminando pago", idPago);
        try {
            pagoService.eliminarPago(idPago);
            log.info("Pago eliminado correctamente con ID: {}", idPago);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Pago no encontrado para eliminar: {}", idPago);
                return ResponseEntity.notFound().build();
            }
            log.error("Error al eliminar pago {}: {}", idPago, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{idPago}")
    @Operation(
            summary = "Obtener un pago por su ID",
            description = "Consulta la información de un pago específico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<PagoDTO> obtenerPagoPorId(
            @Parameter(description = "ID del pago a consultar", required = true, example = "1")
            @PathVariable int idPago
    ) {
        log.debug("GET /api/pagos/{} - Buscando pago por ID", idPago);
        try {
            PagoDTO pago = pagoService.obtenerPagoPorId(idPago);
            log.info("Pago encontrado: {}", idPago);
            return ResponseEntity.ok(pago);
        } catch (RuntimeException e) {
            log.warn("Pago no encontrado con ID: {}", idPago);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los pagos",
            description = "Obtiene una lista con todos los pagos registrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoDTO.class)))
    })
    public ResponseEntity<List<PagoDTO>> listarPagos() {
        log.debug("GET /api/pagos - Listando todos los pagos");
        List<PagoDTO> pagos = pagoService.listarPagos();
        log.debug("Total de pagos encontrados: {}", pagos.size());
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/metodo/{metodoPago}")
    @Operation(
            summary = "Listar pagos por método de pago",
            description = "Obtiene todos los pagos realizados con un método de pago específico"
    )
    public ResponseEntity<List<PagoDTO>> listarPagosPorMetodo(
            @Parameter(description = "Método de pago (efectivo, tarjeta, etc.)", required = true)
            @PathVariable String metodoPago
    ) {
        log.debug("GET /api/pagos/metodo/{} - Buscando pagos por método", metodoPago);
        List<PagoDTO> pagos = pagoService.obtenerPagosPorMetodo(metodoPago);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/fecha/{fechaPago}")
    @Operation(
            summary = "Listar pagos por fecha",
            description = "Obtiene todos los pagos realizados en una fecha específica"
    )
    public ResponseEntity<List<PagoDTO>> listarPagosPorFecha(
            @Parameter(description = "Fecha del pago (YYYY-MM-DD)", required = true)
            @PathVariable String fechaPago
    ) {
        log.debug("GET /api/pagos/fecha/{} - Buscando pagos por fecha", fechaPago);
        LocalDate fecha = LocalDate.parse(fechaPago);
        List<PagoDTO> pagos = pagoService.obtenerPagosPorFecha(fecha);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/reserva/{idReserva}")
    @Operation(
            summary = "Listar pagos por reserva",
            description = "Obtiene todos los pagos asociados a una reserva específica"
    )
    public ResponseEntity<List<PagoDTO>> listarPagosPorReserva(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable int idReserva
    ) {
        log.debug("GET /api/pagos/reserva/{} - Buscando pagos por reserva", idReserva);
        List<PagoDTO> pagos = pagoService.obtenerPagosPorReserva(idReserva);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/total/reserva/{idReserva}")
    @Operation(
            summary = "Calcular total de pagos por reserva",
            description = "Obtiene la suma total de los pagos realizados para una reserva"
    )
    public ResponseEntity<Double> calcularTotalPagosPorReserva(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable int idReserva
    ) {
        log.debug("GET /api/pagos/total/reserva/{} - Calculando total de pagos", idReserva);
        Double total = pagoService.calcularTotalPagosPorReserva(idReserva);
        return ResponseEntity.ok(total);
    }
}
