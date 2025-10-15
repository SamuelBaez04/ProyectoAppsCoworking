package com.coworking.project.businessLayer.dto;

import com.coworking.project.util.TipoNotificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear una Notificación")
public class NotificacionCreateDTO {

    @Schema(description = "Cédula del usuario al que se enviará la notificación", example = "12345678", required = true)
    private Integer usuarioCedula;

    @Schema(description = "Mensaje de la notificación", example = "Recordatorio: tu reserva es mañana a las 9:00", required = true, maxLength = 500)
    private String mensaje;

    @Schema(description = "Fecha y hora de envío. Si se omite, el servidor podrá asignar la fecha actual", example = "2025-10-14T09:00:00", required = false)
    private LocalDateTime fechaEnvio;

    @Schema(description = "Tipo de notificación", example = "RECORDATORIO", required = true)
    private TipoNotificacion tipoNotificacion;
}
