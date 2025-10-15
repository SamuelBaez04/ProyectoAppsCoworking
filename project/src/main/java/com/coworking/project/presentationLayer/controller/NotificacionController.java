package com.coworking.project.presentationLayer.controller;

import com.coworking.project.businessLayer.dto.NotificacionCreateDTO;
import com.coworking.project.businessLayer.dto.NotificacionDTO;
import com.coworking.project.businessLayer.dto.NotificacionUpdateDTO;
import com.coworking.project.businessLayer.service.NotificacionService;
import com.coworking.project.util.NotificacionEstado;
import com.coworking.project.util.TipoNotificacion;
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

import java.util.List;

/**
 * Controlador REST para la gestión de notificaciones.
 */
@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notificaciones", description = "Operaciones CRUD para la gestión de notificaciones de usuarios")
public class NotificacionController {

    private final NotificacionService notificacionService;

    // ✅ Crear una nueva notificación
    @PostMapping
    @Operation(
            summary = "Crear una nueva notificación",
            description = "Crea una nueva notificación para un usuario específico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificacionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<NotificacionDTO> crearNotificacion(
            @Parameter(description = "Datos de la notificación a crear", required = true)
            @RequestBody NotificacionCreateDTO createDTO
    ) {
        log.info("POST /api/notificaciones - Creando notificación para usuario: {}", createDTO.getUsuarioCedula());
        try {
            NotificacionDTO creada = notificacionService.crearNotificacion(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (IllegalArgumentException e) {
            log.warn("Error al crear notificación: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ Obtener una notificación por su ID
    @GetMapping("/{idNotificacion}")
    @Operation(summary = "Obtener una notificación por ID", description = "Devuelve los detalles de una notificación específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificacionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<NotificacionDTO> obtenerPorId(
            @Parameter(description = "ID de la notificación", required = true, example = "1")
            @PathVariable Integer idNotificacion
    ) {
        log.debug("GET /api/notificaciones/{} - Buscando notificación", idNotificacion);
        try {
            NotificacionDTO notificacion = notificacionService.obtenerNotificacionPorId(idNotificacion);
            return ResponseEntity.ok(notificacion);
        } catch (RuntimeException e) {
            log.warn("Notificación no encontrada con ID: {}", idNotificacion);
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Listar todas las notificaciones
    @GetMapping
    @Operation(summary = "Listar todas las notificaciones", description = "Obtiene todas las notificaciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificacionDTO.class)))
    })
    public ResponseEntity<List<NotificacionDTO>> listarNotificaciones() {
        log.debug("GET /api/notificaciones - Listando todas las notificaciones");
        List<NotificacionDTO> lista = notificacionService.listarNotificaciones();
        return ResponseEntity.ok(lista);
    }

    // ✅ Actualizar notificación
    @PutMapping("/{idNotificacion}")
    @Operation(summary = "Actualizar notificación", description = "Permite actualizar los datos de una notificación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<NotificacionDTO> actualizarNotificacion(
            @Parameter(description = "ID de la notificación", required = true, example = "1")
            @PathVariable Integer idNotificacion,
            @Parameter(description = "Datos nuevos de la notificación", required = true)
            @RequestBody NotificacionUpdateDTO updateDTO
    ) {
        log.info("PUT /api/notificaciones/{} - Actualizando notificación", idNotificacion);
        try {
            NotificacionDTO actualizada = notificacionService.actualizarNotificacion(idNotificacion, updateDTO);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            log.warn("Error al actualizar notificación con ID: {}", idNotificacion);
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Eliminar notificación
    @DeleteMapping("/{idNotificacion}")
    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación específica del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<Void> eliminarNotificacion(
            @Parameter(description = "ID de la notificación a eliminar", required = true, example = "1")
            @PathVariable Integer idNotificacion
    ) {
        log.info("DELETE /api/notificaciones/{} - Eliminando notificación", idNotificacion);
        try {
            notificacionService.eliminarNotificacion(idNotificacion);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Notificación no encontrada con ID: {}", idNotificacion);
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Listar por tipo
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar notificaciones por tipo", description = "Devuelve todas las notificaciones de un tipo específico (ej: recordatorio, alerta, confirmación)")
    public ResponseEntity<List<NotificacionDTO>> listarPorTipo(
            @Parameter(description = "Tipo de notificación", required = true)
            @PathVariable TipoNotificacion tipo
    ) {
        log.debug("GET /api/notificaciones/tipo/{} - Listando por tipo", tipo);
        List<NotificacionDTO> lista = notificacionService.listarNotificacionesPorTipo(tipo);
        return ResponseEntity.ok(lista);
    }

    // ✅ Listar por estado
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar notificaciones por estado", description = "Devuelve las notificaciones según su estado (pendiente, enviado, leído)")
    public ResponseEntity<List<NotificacionDTO>> listarPorEstado(
            @Parameter(description = "Estado de la notificación", required = true)
            @PathVariable NotificacionEstado estado
    ) {
        log.debug("GET /api/notificaciones/estado/{} - Listando por estado", estado);
        List<NotificacionDTO> lista = notificacionService.listarNotificacionesPorEstado(estado);
        return ResponseEntity.ok(lista);
    }

    // ✅ Listar por usuario
    @GetMapping("/usuario/{cedulaUsuario}")
    @Operation(summary = "Listar notificaciones de un usuario", description = "Obtiene todas las notificaciones asociadas a un usuario")
    public ResponseEntity<List<NotificacionDTO>> listarPorUsuario(
            @Parameter(description = "Cédula del usuario", required = true, example = "123456")
            @PathVariable Integer cedulaUsuario
    ) {
        log.debug("GET /api/notificaciones/usuario/{} - Listando por usuario", cedulaUsuario);
        List<NotificacionDTO> lista = notificacionService.listarNotificacionesPorUsuario(cedulaUsuario);
        return ResponseEntity.ok(lista);
    }

    // ✅ Marcar notificación como leída
    @PutMapping("/{idNotificacion}/leida")
    @Operation(summary = "Marcar notificación como leída", description = "Cambia el estado de la notificación a 'LEIDO'")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(
            @Parameter(description = "ID de la notificación a marcar", required = true, example = "1")
            @PathVariable Integer idNotificacion
    ) {
        log.info("PUT /api/notificaciones/{}/leida - Marcando como leída", idNotificacion);
        try {
            NotificacionDTO actualizada = notificacionService.marcarComoLeida(idNotificacion);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            log.warn("Error al marcar como leída la notificación ID: {}", idNotificacion);
            return ResponseEntity.notFound().build();
        }
    }
}
