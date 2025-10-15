package com.coworking.project.persistenceLayer.dao;

import com.coworking.project.businessLayer.dto.NotificacionCreateDTO;
import com.coworking.project.businessLayer.dto.NotificacionDTO;
import com.coworking.project.businessLayer.dto.NotificacionUpdateDTO;
import com.coworking.project.persistenceLayer.entity.NotificacionEntity;
import com.coworking.project.persistenceLayer.mapper.NotificacionMapper;
import com.coworking.project.persistenceLayer.repository.NotificacionRepository;
import com.coworking.project.util.NotificacionEstado;
import com.coworking.project.util.TipoNotificacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificacionDAO {

    private final NotificacionRepository notificacionRepository;
    private final NotificacionMapper notificacionMapper;

    /**
     * Crear una nueva notificación.
     */
    public NotificacionDTO createNotificacion(NotificacionCreateDTO createDTO) {
        NotificacionEntity entity = notificacionMapper.toEntity(createDTO);
        NotificacionEntity saved = notificacionRepository.save(entity);
        return notificacionMapper.toDTO(saved);
    }

    /**
     * Buscar notificación por ID.
     */
    public Optional<NotificacionDTO> findById(Integer idNotificacion) {
        return notificacionRepository.findById(idNotificacion)
                .map(notificacionMapper::toDTO);
    }

    /**
     * Listar todas las notificaciones.
     */
    public List<NotificacionDTO> findAll() {
        List<NotificacionEntity> entities = notificacionRepository.findAll();
        return notificacionMapper.toDTOList(entities);
    }

    /**
     * Listar notificaciones por usuario.
     */
    public List<NotificacionDTO> findByUsuario(Integer cedulaUsuario) {
        List<NotificacionEntity> entities = notificacionRepository.findByUsuarioCedula(cedulaUsuario);
        return notificacionMapper.toDTOList(entities);
    }

    /**
     * Listar notificaciones por estado (pendiente, enviado, leído).
     */
    public List<NotificacionDTO> findByEstado(NotificacionEstado estado) {
        List<NotificacionEntity> entities = notificacionRepository.findByEstado(estado);
        return notificacionMapper.toDTOList(entities);
    }

    /**
     * Listar notificaciones por tipo (confirmación, recordatorio, alerta de vencimiento...).
     */
    public List<NotificacionDTO> findByTipo(TipoNotificacion tipo) {
        List<NotificacionEntity> entities = notificacionRepository.findByTipoNotificacion(tipo);
        return notificacionMapper.toDTOList(entities);
    }

    /**
     * Actualizar notificación existente.
     */
    public Optional<NotificacionDTO> update(Integer idNotificacion, NotificacionUpdateDTO updateDTO) {
        return notificacionRepository.findById(idNotificacion)
                .map(existingEntity -> {
                    notificacionMapper.updateEntityFromDTO(updateDTO, existingEntity);
                    NotificacionEntity updated = notificacionRepository.save(existingEntity);
                    return notificacionMapper.toDTO(updated);
                });
    }

    /**
     * Eliminar notificación por ID.
     */
    public boolean deleteById(Integer idNotificacion) {
        if (notificacionRepository.existsById(idNotificacion)) {
            notificacionRepository.deleteById(idNotificacion);
            return true;
        }
        return false;
    }

    /**
     * Marcar una notificación como leída.
     */
    public Optional<NotificacionDTO> marcarComoLeida(Integer idNotificacion) {
        return notificacionRepository.findById(idNotificacion)
                .map(entity -> {
                    entity.setEstado(NotificacionEstado.LEIDO);
                    NotificacionEntity updated = notificacionRepository.save(entity);
                    return notificacionMapper.toDTO(updated);
                });
    }
}
