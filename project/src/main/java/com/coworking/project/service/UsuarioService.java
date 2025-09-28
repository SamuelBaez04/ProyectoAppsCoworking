package com.coworking.project.service;

import com.coworking.project.dto.request.UsuarioRequestDto;
import com.coworking.project.dto.response.UsuarioResponseDto;

import java.util.List;

/**
 * Interfaz del servicio de Usuario
 * Define el contrato de operaciones de negocio para usuarios
 */
public interface UsuarioService {
    
    /**
     * Crear un nuevo usuario
     * @param request Datos del usuario a crear
     * @return Usuario creado
     */
    UsuarioResponseDto crearUsuario(UsuarioRequestDto request);
    
    /**
     * Obtener usuario por cédula
     * @param cedula Cédula del usuario
     * @return Usuario encontrado
     */
    UsuarioResponseDto obtenerUsuarioPorCedula(Integer cedula);
    
    /**
     * Listar todos los usuarios
     * @return Lista de usuarios
     */
    List<UsuarioResponseDto> listarUsuarios();
    
    /**
     * Actualizar usuario existente
     * @param cedula Cédula del usuario a actualizar
     * @param request Nuevos datos del usuario
     * @return Usuario actualizado
     */
    UsuarioResponseDto actualizarUsuario(Integer cedula, UsuarioRequestDto request);
    
    /**
     * Eliminar usuario por cédula
     * @param cedula Cédula del usuario a eliminar
     */
    void eliminarUsuario(Integer cedula);
    
    /**
     * Buscar usuarios por email
     * @param email Email a buscar
     * @return Usuario encontrado
     */
    UsuarioResponseDto obtenerUsuarioPorEmail(String email);
}