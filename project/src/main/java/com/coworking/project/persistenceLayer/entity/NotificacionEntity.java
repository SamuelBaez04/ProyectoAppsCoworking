package com.coworking.project.persistenceLayer.entity;

import com.coworking.project.util.NotificacionEstado;
import com.coworking.project.util.TipoNotificacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private int idNotificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cedula_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "mensaje", nullable = false, length = 500)
    private String mensaje;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacion", nullable = false, length = 50)
    private TipoNotificacion tipoNotificacion; // Ej: "confirmacion", "recordatorio", "alerta_vencimiento"

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private NotificacionEstado estado; // Ej: "pendiente", "enviado", "leido"
}
