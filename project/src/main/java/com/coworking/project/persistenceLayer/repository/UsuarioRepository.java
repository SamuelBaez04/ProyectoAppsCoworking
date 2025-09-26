package com.coworking.project.persistenceLayer.repository;

import com.coworking.project.persistenceLayer.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    Optional<UsuarioEntity> findByEmail(String email);

    List<UsuarioEntity> findByNombreCompleto(String nombreCompleto);

    boolean existsEmail(String email);

    List<UsuarioEntity> findByNombreRol(String nombreRol);

    @Query("SELECT u FROM UsuarioEntyity u WHERE SIZE(u.reservas) > 0")
    List<UsuarioEntity> findByReservaActiva();

}