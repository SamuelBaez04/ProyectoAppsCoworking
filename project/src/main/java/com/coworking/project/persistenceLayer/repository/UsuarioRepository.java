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

    Optional<UsuarioEntity> findById(int id);

    List<UsuarioEntity> findByNombreCompletoContainingIgnoreCase(String nombreCompleto);


    List<UsuarioEntity> findByRolIdRol(Long idRol);

    boolean existsById(int id);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM UsuarioEntity u JOIN u.reservas r WHERE r.estado = 'ACTIVA'")
    List<UsuarioEntity> findUsuariosConReservasActivas();

    @Query("SELECT u FROM UsuarioEntity u WHERE u.email = :email AND u.password = :password")
    Optional<UsuarioEntity> validarLogin(@Param("email") String email, @Param("password") String password);

    @Query("SELECT COUNT(r) FROM ReservaEntity r WHERE r.usuarioReserva.cedula = :cedula")
    int countReservasByUsuarioId(@Param("cedula") int cedula);

}