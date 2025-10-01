package com.coworking.project.businessLayer.service.impl;

import com.coworking.project.businessLayer.dto.request.UsuarioRequestDto;
import com.coworking.project.businessLayer.dto.response.UsuarioResponseDto;
import com.coworking.project.businessLayer.service.UsuarioService;
import com.coworking.project.exception.ResourceNotFoundException;
import com.coworking.project.persistenceLayer.entity.RolEntity;
import com.coworking.project.persistenceLayer.entity.UsuarioEntity;
import com.coworking.project.persistenceLayer.repository.RolRepository;
import com.coworking.project.persistenceLayer.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Usuario
 * Contiene la lógica de negocio para la gestión de usuarios
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    public UsuarioResponseDto crearUsuario(UsuarioRequestDto request) {
        log.info("Creando usuario con cédula: {}", request.getCedula());
        
        // Validar que no exista el usuario
        if (usuarioRepository.existsById(request.getCedula())) {
            throw new RuntimeException("Ya existe un usuario con la cédula: " + request.getCedula());
        }
        
        // Validar que no exista el email
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con el email: " + request.getEmail());
        }
        
        // CORRECCIÓN 1: Convertir el Integer (request.getIdRol()) a Long.
        Long idRolLong = request.getIdRol().longValue();
        
        // Buscar el rol
        RolEntity rol = rolRepository.findById(idRolLong)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", request.getIdRol()));
        
        // Crear la entidad usuario
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCedula(request.getCedula());
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setRolEntity(rol);
        usuario.setDireccion(request.getDireccion());
        usuario.setPassword(request.getPassword()); // TODO: Encriptar password
        usuario.setTelefono(request.getTelefono());
        usuario.setEmail(request.getEmail());
        
        // Guardar
        UsuarioEntity savedUsuario = usuarioRepository.save(usuario);
        
        log.info("Usuario creado exitosamente con cédula: {}", savedUsuario.getCedula());
        return mapToResponseDto(savedUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto obtenerUsuarioPorCedula(Integer cedula) {
        log.info("Buscando usuario con cédula: {}", cedula);
        
        UsuarioEntity usuario = usuarioRepository.findById(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "cédula", cedula));
        
        return mapToResponseDto(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarUsuarios() {
        log.info("Listando todos los usuarios");
        
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
        
        return usuarios.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDto actualizarUsuario(Integer cedula, UsuarioRequestDto request) {
        log.info("Actualizando usuario con cédula: {}", cedula);
        
        UsuarioEntity usuario = usuarioRepository.findById(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "cédula", cedula));
        
        // Validar email único (si cambió)
        if (!usuario.getEmail().equals(request.getEmail()) && 
            usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con el email: " + request.getEmail());
        }
        
        // CORRECCIÓN 2: Usar getRolEntity().getIdRol() y convertir request.getIdRol() a Long.
        Long idRolLong = request.getIdRol().longValue();
        
        // Buscar el rol si cambió
        if (!usuario.getRolEntity().getIdRol().equals(idRolLong)) {
            // CORRECCIÓN 3: Usar el Long casteado.
            RolEntity rol = rolRepository.findById(idRolLong)
                    .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", request.getIdRol()));
            usuario.setRolEntity(rol);
        }
        
        // Actualizar campos
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setDireccion(request.getDireccion());
        usuario.setTelefono(request.getTelefono());
        usuario.setEmail(request.getEmail());
        
        // Solo actualizar password si viene en el request
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(request.getPassword()); // TODO: Encriptar password
        }
        
        UsuarioEntity updatedUsuario = usuarioRepository.save(usuario);
        
        log.info("Usuario actualizado exitosamente con cédula: {}", cedula);
        return mapToResponseDto(updatedUsuario);
    }

    @Override
    public void eliminarUsuario(Integer cedula) {
        log.info("Eliminando usuario con cédula: {}", cedula);
        
        UsuarioEntity usuario = usuarioRepository.findById(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "cédula", cedula));
        
        usuarioRepository.delete(usuario);
        
        log.info("Usuario eliminado exitosamente con cédula: {}", cedula);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto obtenerUsuarioPorEmail(String email) {
        log.info("Buscando usuario con email: {}", email);
        
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
        
        return mapToResponseDto(usuario);
    }

    /**
     * Método privado para mapear UsuarioEntity a UsuarioResponseDto
     */
    private UsuarioResponseDto mapToResponseDto(UsuarioEntity usuario) {
        return new UsuarioResponseDto(
                usuario.getCedula(),
                usuario.getNombreCompleto(),
                // CORRECCIÓN 4: Usar getRolEntity().getNombreRol()
                usuario.getRolEntity().getNombreRol(),
                // CORRECCIÓN 5: Usar getRolEntity().getIdRol()
                usuario.getRolEntity().getIdRol(),
                usuario.getDireccion(),
                usuario.getTelefono(),
                usuario.getEmail()
        );
    }
}