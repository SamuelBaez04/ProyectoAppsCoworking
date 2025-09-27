package com.coworking.project.persistenceLayer.repository;


import com.coworking.project.persistenceLayer.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Integer> {

    List<ReservaEntity> findByEstado(String estado);

    List<ReservaEntity> findByUsuarioReservaCedula(int cedula);

    List<ReservaEntity> findByRecursoEntityIdRecurso(Long idRecurso);

    List<ReservaEntity> findByFechaInicioBetween(LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT r FROM ReservaEntity r WHERE r.usuarioReserva.cedula = :cedula AND r.estado = 'ACTIVA'")
    List<ReservaEntity> findReservasActivasPorUsuario(int cedula);


}
