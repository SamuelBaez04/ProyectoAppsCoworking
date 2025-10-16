package com.coworking.project.businessLayer.dto;

import com.coworking.project.util.NotificacionEstado;
import com.coworking.project.util.TipoNotificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de la Notificación")
public class NotificacionDTO {

    @Schema(description = "Id único de la notificación", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idNotificacion;

    @Schema(description = "Cédula del usuario asociado a la notificación", example = "12345678", required = true)
    private Integer usuarioCedula;

    @Schema(description = "Mensaje de la notificación", example = "Tu reserva ha sido confirmada", required = true, maxLength = 500)
    private String mensaje;

    @Schema(description = "Fecha y hora en que fue (o será) enviada la notificación", example = "2025-10-14T10:00:00", required = true)
    private LocalDateTime fechaEnvio;

    @Schema(description = "Tipo de notificación", example = "CONFIRMACION", required = true)
    private TipoNotificacion tipoNotificacion;

    @Schema(description = "Estado de la notificación", example = "PENDIENTE", required = true)
    private NotificacionEstado estado;
}
