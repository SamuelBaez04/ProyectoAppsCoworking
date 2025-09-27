package com.coworking.project.persistenceLayer.repository;

import com.coworking.project.persistenceLayer.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    Optional<UsuarioEntity> findByEmail(String email);

    List<UsuarioEntity> findByNombreCompletoContainingIgnoreCase(String nombreCompleto);

    List<UsuarioEntity> findByRolEntityIdRol(Long idRol);

    @Query("SELECT u FROM UsuarioEntity u JOIN u.reservas r WHERE r.estado = 'ACTIVA'")
    List<UsuarioEntity> findUsuariosConReservasActivas();

    @Query("SELECT u FROM UsuarioEntity u WHERE u.email = :email AND u.password = :password")
    Optional<UsuarioEntity> validarLogin(@Param("email") String email, @Param("password") String password);

}