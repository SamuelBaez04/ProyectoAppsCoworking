package com.coworking.project.businessLayer.service;

import java.util.List;

import com.coworking.project.businessLayer.dto.UsuarioCreateDTO;
import com.coworking.project.businessLayer.dto.UsuarioDTO;
import com.coworking.project.businessLayer.dto.UsuarioUpdateDTO;

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
    UsuarioDTO crearUsuario(UsuarioCreateDTO createDTO);
    
    /**
     * Obtener usuario por cédula
     * @param cedula Cédula del usuario
     * @return Usuario encontrado
     */
    UsuarioDTO obtenerUsuarioPorCedula(int cedula);
    
    /**
     * Listar todos los usuarios
     * @return Lista de usuarios
     */
    List<UsuarioDTO> listarUsuarios();
    
    /**
     * Actualizar usuario existente
     * @param cedula Cédula del usuario a actualizar
     * @param request Nuevos datos del usuario
     * @return Usuario actualizado
     */
    UsuarioDTO actualizarUsuario(int cedula, UsuarioUpdateDTO updateDTO);
    
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
    UsuarioDTO obtenerUsuarioPorEmail(String email);
}