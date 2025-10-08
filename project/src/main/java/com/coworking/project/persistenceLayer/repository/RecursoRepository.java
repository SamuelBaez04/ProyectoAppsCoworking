package com.coworking.project.persistenceLayer.repository;

import com.coworking.project.persistenceLayer.entity.RecursoEntity;
import com.coworking.project.util.RecursoEstado;
import com.coworking.project.util.TipoRecurso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecursoRepository extends JpaRepository<RecursoEntity,Long> {

    List<RecursoEntity> findByTipoRecurso(TipoRecurso tipoRecurso);

    Optional<RecursoEntity> findByIdRecurso(Long idRecurso);



    List<RecursoEntity> findByEstado(RecursoEstado estado);

    @Query("SELECT r FROM RecursoEntity r WHERE :fecha BETWEEN r.fechaInicioTarifa AND r.fechaFinTarifa")
    List<RecursoEntity> findRecursosConTarifaVigente(@Param("fecha") LocalDate fecha);

    boolean existsByNombreRecurso(String nombreRecurso);



}
