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
@Schema(description = "Datos permitidos para actualizar una Notificación")
public class NotificacionUpdateDTO {

    @Schema(description = "Mensaje actualizado de la notificación", example = "Mensaje actualizado", required = false, maxLength = 500)
    private String mensaje;

    @Schema(description = "Fecha/hora de envío actualizada", example = "2025-10-14T09:30:00", required = false)
    private LocalDateTime fechaEnvio;

    @Schema(description = "Tipo de notificación (puede actualizarse si aplica)", example = "GENERAL", required = false)
    private TipoNotificacion tipoNotificacion;

    @Schema(description = "Estado de la notificación (PENDIENTE, ENVIADO, LEIDO)", example = "ENVIADO", required = false)
    private NotificacionEstado estado;
}
