package com.coworking.project.controller;

import com.coworking.project.dto.request.UsuarioRequestDto;
import com.coworking.project.dto.response.UsuarioResponseDto;
import com.coworking.project.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuarios", description = "API para la gestión de usuarios del coworking")
public class UsuarioController {

    private final UsuarioService usuarioService; // ← Inyección de la INTERFAZ

    @Operation(summary = "Crear un nuevo usuario", 
               description = "Crea un nuevo usuario en el sistema de coworking")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "409", description = "Usuario ya existe")
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> crearUsuario(@Valid @RequestBody UsuarioRequestDto request) {
        log.info("REST request para crear usuario: {}", request.getEmail());
        
        UsuarioResponseDto response = usuarioService.crearUsuario(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener usuario por cédula",
               description = "Busca un usuario específico por su cédula")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/{cedula}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuario(
            @Parameter(description = "Cédula del usuario") @PathVariable Integer cedula) {
        log.info("REST request para obtener usuario con cédula: {}", cedula);
        
        UsuarioResponseDto response = usuarioService.obtenerUsuarioPorCedula(cedula);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todos los usuarios",
               description = "Obtiene la lista completa de usuarios registrados")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        log.info("REST request para listar todos los usuarios");
        
        List<UsuarioResponseDto> response = usuarioService.listarUsuarios();
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar usuario existente",
               description = "Actualiza los datos de un usuario existente")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PutMapping("/{cedula}")
    public ResponseEntity<UsuarioResponseDto> actualizarUsuario(
            @Parameter(description = "Cédula del usuario") @PathVariable Integer cedula,
            @Valid @RequestBody UsuarioRequestDto request) {
        log.info("REST request para actualizar usuario con cédula: {}", cedula);
        
        UsuarioResponseDto response = usuarioService.actualizarUsuario(cedula, request);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar usuario",
               description = "Elimina un usuario del sistema")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "Cédula del usuario") @PathVariable Integer cedula) {
        log.info("REST request para eliminar usuario con cédula: {}", cedula);
        
        usuarioService.eliminarUsuario(cedula);
        
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar usuario por email",
               description = "Busca un usuario específico por su email")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorEmail(
            @Parameter(description = "Email del usuario") @PathVariable String email) {
        log.info("REST request para obtener usuario con email: {}", email);
        
        UsuarioResponseDto response = usuarioService.obtenerUsuarioPorEmail(email);
        
        return ResponseEntity.ok(response);
    }
}