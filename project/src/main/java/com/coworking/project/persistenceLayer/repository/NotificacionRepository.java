package com.coworking.project.persistenceLayer.repository;

import com.coworking.project.persistenceLayer.entity.NotificacionEntity;
import com.coworking.project.util.NotificacionEstado;
import com.coworking.project.util.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<NotificacionEntity, Integer> {


    List<NotificacionEntity> findByUsuarioCedula(int cedulaUsuario);

    List<NotificacionEntity> findByTipoNotificacion(TipoNotificacion tipoNotificacion);

    List<NotificacionEntity> findByEstado(NotificacionEstado estado);

    List<NotificacionEntity> findByTipoNotificacionAndEstado(TipoNotificacion tipoNotificacion, NotificacionEstado estado);

    List<NotificacionEntity> findByUsuarioCedulaAndEstado(int cedulaUsuario, NotificacionEstado estado);

    List<NotificacionEntity> findByFechaEnvioBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT n FROM NotificacionEntity n WHERE n.usuario.cedula = :cedulaUsuario ORDER BY n.fechaEnvio DESC")
    List<NotificacionEntity> findRecientesPorUsuario(int cedulaUsuario);

    @Query("SELECT COUNT(n) FROM NotificacionEntity n WHERE n.usuario.cedula = :cedulaUsuario AND n.estado = 'PENDIENTE'")
    Long contarPendientesPorUsuario(int cedulaUsuario);

    @Query("SELECT n FROM NotificacionEntity n WHERE n.tipoNotificacion = 'RECORDATORIO' AND n.estado = 'PENDIENTE'")
    List<NotificacionEntity> findRecordatoriosPendientes();

    @Query("SELECT n FROM NotificacionEntity n WHERE n.tipoNotificacion = 'ALERTA_VENCIMIENTO' AND n.fechaEnvio <= :fechaLimite AND n.estado = 'PENDIENTE'")
    List<NotificacionEntity> findAlertasProximas(LocalDateTime fechaLimite);

    @Query("SELECT n FROM NotificacionEntity n WHERE n.fechaEnvio >= :fechaLimite ORDER BY n.fechaEnvio DESC")
    List<NotificacionEntity> findRecientes(LocalDateTime fechaLimite);

}
