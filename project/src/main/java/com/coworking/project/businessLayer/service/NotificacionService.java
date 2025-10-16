package com.coworking.project.businessLayer.service;

import com.coworking.project.businessLayer.dto.NotificacionCreateDTO;
import com.coworking.project.businessLayer.dto.NotificacionDTO;
import com.coworking.project.businessLayer.dto.NotificacionUpdateDTO;
import com.coworking.project.util.NotificacionEstado;
import com.coworking.project.util.TipoNotificacion;

import java.util.List;

/**
 * Interfaz del servicio para manejar la lógica de negocio de las notificaciones.
 */
public interface NotificacionService {

    /**
     * Crear una nueva notificación.
     * @param createDTO Datos de la notificación a crear.
     * @return Notificación creada.
     */
    NotificacionDTO crearNotificacion(NotificacionCreateDTO createDTO);

    /**
     * Obtener una notificación por su ID.
     * @param idNotificacion ID de la notificación.
     * @return Notificación encontrada.
     */
    NotificacionDTO obtenerNotificacionPorId(Integer idNotificacion);

    /**
     * Listar todas las notificaciones registradas.
     * @return Lista de notificaciones.
     */
    List<NotificacionDTO> listarNotificaciones();

    /**
     * Actualizar una notificación existente.
     * @param idNotificacion ID de la notificación a actualizar.
     * @param updateDTO Datos nuevos de la notificación.
     * @return Notificación actualizada.
     */
    NotificacionDTO actualizarNotificacion(Integer idNotificacion, NotificacionUpdateDTO updateDTO);

    /**
     * Eliminar una notificación por su ID.
     * @param idNotificacion ID de la notificación a eliminar.
     */
    void eliminarNotificacion(Integer idNotificacion);

    /**
     * Listar notificaciones de un usuario.
     * @param cedulaUsuario ID del usuario propietario.
     * @return Lista de notificaciones del usuario.
     */
    List<NotificacionDTO> listarNotificacionesPorUsuario(Integer cedulaUsuario);

    /**
     * Listar notificaciones por tipo (confirmación, recordatorio, alerta de vencimiento, etc.)
     * @param tipo Tipo de notificación.
     * @return Lista de notificaciones del tipo especificado.
     */
    List<NotificacionDTO> listarNotificacionesPorTipo(TipoNotificacion tipo);

    /**
     * Listar notificaciones por estado (pendiente, enviado, leído, etc.)
     * @param estado Estado de la notificación.
     * @return Lista de notificaciones en ese estado.
     */
    List<NotificacionDTO> listarNotificacionesPorEstado(NotificacionEstado estado);

    /**
     * Marcar una notificación como leída.
     * @param idNotificacion ID de la notificación.
     * @return Notificación actualizada con estado LEIDO.
     */
    NotificacionDTO marcarComoLeida(Integer idNotificacion);
}
