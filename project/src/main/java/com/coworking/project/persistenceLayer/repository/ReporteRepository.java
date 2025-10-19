package com.coworking.project.persistenceLayer.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coworking.project.persistenceLayer.entity.ReporteEntity;
import com.coworking.project.util.TipoReporte;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteEntity, Integer> {

    List<ReporteEntity> findByTipoReporte(TipoReporte tipoReporte);

    Optional<ReporteEntity> findByIdReporte(Integer idReporte);

    List<ReporteEntity> findByFechaGeneracion(LocalDate fechaGeneracion);

    boolean existsByTitulo(String titulo);

   @Query("SELECT r FROM ReporteEntity r WHERE r.fechaGeneracion BETWEEN :fechaInicio AND :fechaFin")
    List<ReporteEntity> findReportesPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin);
}
