package com.coworking.project.persistenceLayer.repository;

import com.coworking.project.persistenceLayer.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RolRepository  extends JpaRepository<RolEntity,Long> {

    RolEntity findByNombreRol(String nombreRol);

}