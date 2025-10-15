package com.coworking.project.businessLayer.service.impl;

import com.coworking.project.businessLayer.dto.NotificacionCreateDTO;
import com.coworking.project.businessLayer.dto.NotificacionDTO;
import com.coworking.project.businessLayer.dto.NotificacionUpdateDTO;
import com.coworking.project.businessLayer.service.NotificacionService;
import com.coworking.project.persistenceLayer.dao.NotificacionDAO;
import com.coworking.project.util.NotificacionEstado;
import com.coworking.project.util.TipoNotificacion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio para la gestión de notificaciones.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionDAO notificacionDAO;

    @Override
    public NotificacionDTO crearNotificacion(NotificacionCreateDTO createDTO) {
        log.info("Creando nueva notificación para el usuario con cédula: {}",
                createDTO.getUsuarioCedula());
        NotificacionDTO creada = notificacionDAO.createNotificacion(createDTO);
        log.info("Notificación creada exitosamente con ID: {}", creada.getIdNotificacion());
        return creada;
    }

    @Override
    public NotificacionDTO obtenerNotificacionPorId(Integer idNotificacion) {
        log.debug("Buscando notificación con ID: {}", idNotificacion);
        return notificacionDAO.findById(idNotificacion).orElseThrow(() -> {
            log.warn("Notificación no encontrada con ID: {}", idNotificacion);
            return new RuntimeException("Notificación no encontrada con ID: " + idNotificacion);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarNotificaciones() {
        log.debug("Listando todas las notificaciones");
        return notificacionDAO.findAll();
    }

    @Override
    public NotificacionDTO actualizarNotificacion(Integer idNotificacion, NotificacionUpdateDTO updateDTO) {
        log.info("Actualizando notificación con ID: {}", idNotificacion);
        return notificacionDAO.update(idNotificacion, updateDTO).orElseThrow(() -> {
            log.warn("Intento de actualizar notificación inexistente con ID: {}", idNotificacion);
            return new RuntimeException("Notificación no encontrada con ID: " + idNotificacion);
        });
    }

    @Override
    public void eliminarNotificacion(Integer idNotificacion) {
        log.info("Eliminando notificación con ID: {}", idNotificacion);
        if (!notificacionDAO.deleteById(idNotificacion)) {
            log.warn("Intento de eliminar notificación inexistente con ID: {}", idNotificacion);
            throw new RuntimeException("Notificación no encontrada con ID: " + idNotificacion);
        }
        log.info("Notificación eliminada con éxito con ID: {}", idNotificacion);
    }

    @Override
    public List<NotificacionDTO> listarNotificacionesPorUsuario(Integer cedulaUsuario) {
        log.debug("Listando notificaciones del usuario con cédula: {}", cedulaUsuario);
        return notificacionDAO.findByUsuario(cedulaUsuario);
    }

    @Override
    public List<NotificacionDTO> listarNotificacionesPorTipo(TipoNotificacion tipo) {
        log.debug("Listando notificaciones del tipo: {}", tipo);
        return notificacionDAO.findByTipo(tipo);
    }

    @Override
    public List<NotificacionDTO> listarNotificacionesPorEstado(NotificacionEstado estado) {
        log.debug("Listando notificaciones con estado: {}", estado);
        return notificacionDAO.findByEstado(estado);
    }

    @Override
    public NotificacionDTO marcarComoLeida(Integer idNotificacion) {
        log.info("Marcando notificación como leída con ID: {}", idNotificacion);
        NotificacionDTO notificacion = obtenerNotificacionPorId(idNotificacion);
        NotificacionUpdateDTO updateDTO = new NotificacionUpdateDTO();
        updateDTO.setEstado(NotificacionEstado.LEIDO);
        NotificacionDTO actualizada = actualizarNotificacion(idNotificacion, updateDTO);
        log.info("Notificación marcada como leída: {}", idNotificacion);
        return actualizada;
    }
}
