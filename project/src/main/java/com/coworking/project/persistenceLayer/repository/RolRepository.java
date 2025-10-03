package com.coworking.project.persistenceLayer.repository;

import com.coworking.project.persistenceLayer.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface RolRepository  extends JpaRepository<RolEntity,Long> {

    Optional<RolEntity> findByNombreRol(String nombreRol);

    @Query("SELECT COUNT(u) FROM UsuarioEntity u WHERE u.rol.idRol = :idRol")
    Long contarUsuariosPorRol(Long idRol);

}