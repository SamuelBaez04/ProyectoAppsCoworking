package com.coworking.project.businessLayer.service.impl;


import com.coworking.project.businessLayer.dto.UsuarioCreateDTO;
import com.coworking.project.businessLayer.dto.UsuarioDTO;
import com.coworking.project.businessLayer.dto.UsuarioUpdateDTO;
import com.coworking.project.businessLayer.service.UsuarioService;
import com.coworking.project.exception.ResourceNotFoundException;
import com.coworking.project.persistenceLayer.dao.UsuarioDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de Usuario
 * Contiene la lógica de negocio para la gestión de usuarios
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;


    @Override
    public UsuarioDTO crearUsuario(UsuarioCreateDTO createDTO) {
        log.info("Creando Usuario nuevo usuario con el email: {}", createDTO.getEmail());
        if(usuarioDAO.existByEmail(createDTO.getEmail())) {
            log.warn("Intento de crear usuario con email duplicado: {}",createDTO.getEmail());
            throw new IllegalArgumentException("Ya exite un usuario con el email"+ createDTO.getEmail());
        }
        UsuarioDTO createUsuario = usuarioDAO.crearUsuario(createDTO);
        log.info("Usuario creado exitosamente con id: {}", createUsuario.getCedula());
        return createUsuario;
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorCedula(int cedula) {
        log.debug("Creando usuario por cedula: {}", cedula);

        return usuarioDAO.findyById(cedula).orElseThrow(() -> {
            log.warn("Usuario no encontrado con cedula: {}", cedula);
            return new RuntimeException("Vendedor no encontrado con cedula: " + cedula);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        log.debug("Obteniendo todos los usuarios");
        return usuarioDAO.findAll();
    }

    @Override
    public UsuarioDTO actualizarUsuario(int cedula, UsuarioUpdateDTO updateDTO) {
        log.info("Actualizando usuario con cedula: {}", cedula);

        if(!usuarioDAO.findyById(cedula).isPresent()) {
            log.warn("Intento de acutalizar usuario inexistente por cedula: {}", cedula);
            throw new RuntimeException("Usuario no encontrado con cedula: "+ cedula);
        }
        UsuarioDTO updateUsuario = usuarioDAO.update(cedula, updateDTO).orElseThrow(()-> new RuntimeException("Error al actualizar usuario"));
        log.info("Usuario actualziado exitosamente cedula: {}", cedula);
        return updateUsuario;
    }

    @Override
    public void eliminarUsuario(Integer cedula) {
        log.info("Eliminando usuario con cedula: {}", cedula);
        UsuarioDTO usuarioDTO = obtenerUsuarioPorCedula(cedula);

        int contadorReservas = usuarioDAO.countReservasByUsuarioId(cedula);
        if(contadorReservas > 0) {
            log.warn("Intento de eliminar vendedor con reservas. cedula:{}, Reservas: {}", cedula, contadorReservas);
            throw new IllegalStateException(
                    String.format("No se puede elimianr el usuario por que tiene %d reserva(s) activa(s)",  contadorReservas)
            );
        }

        boolean deleted = usuarioDAO.deleteById(cedula);
        if(!deleted) {
            throw new RuntimeException("Error al eliminar vendedor con cedula: " + cedula);
        }
        log.info("Usuario eliminado exitosamente cedula: {}", cedula);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorEmail(String email) {
        log.debug("Obteniendo usuario por email: {}", email);
        return usuarioDAO.findByEmail(email).orElseThrow(() -> {
            log.warn("Usuario no encontrado con email: {}", email);
            return new RuntimeException("Usuario no encontrado con email: " + email);
        });
    }
}