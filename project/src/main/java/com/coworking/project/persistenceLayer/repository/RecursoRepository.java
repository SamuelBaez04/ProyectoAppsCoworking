package com.coworking.project.persistenceLayer.repository;

import com.coworking.project.persistenceLayer.entity.RecursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<RecursoEntity,Long> {

    List<RecursoEntity>  findByRecursoEntity_Id(Long id);

    List<RecursoEntity> findByTipoRecurso(String tipoRecurso);

    RecursoEntity findByNombreRecurso(String nombreRecurso);

    List<RecursoEntity> findByEstado(String estado);

}
