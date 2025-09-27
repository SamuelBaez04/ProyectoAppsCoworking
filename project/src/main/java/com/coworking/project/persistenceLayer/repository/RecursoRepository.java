package com.coworking.project.persistenceLayer.repository;

import com.coworking.project.persistenceLayer.entity.RecursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<RecursoEntity,Long> {

    List<RecursoEntity> findByTipoRecurso(String tipoRecurso);

    List<RecursoEntity> findByNombreRecursoContainingIgnoreCase(String nombreRecurso);

    List<RecursoEntity> findByEstado(String estado);

    @Query("SELECT r FROM RecursoEntity r WHERE :fecha BETWEEN r.fechaInicioTarifa AND r.fechaFinTarifa")
    List<RecursoEntity> findRecursosConTarifaVigente(@Param("fecha") LocalDate fecha);

}
