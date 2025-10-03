package com.coworking.project.presentationLayer.controller;

import com.coworking.project.businessLayer.dto.UsuarioCreateDTO;
import com.coworking.project.businessLayer.dto.UsuarioDTO;
import com.coworking.project.businessLayer.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Usuarios", description = "Operaciones Crud para gestion de vendedores")
public class UsuarioController {

    private final UsuarioService usuarioService; // ← Inyección de la INTERFAZ

    @PostMapping
    @Operation(
            summary = "Crear nuevo usuario",
            description = "Crear un nuevo usuario en el sistema con validacion de email unico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class))
            ),
            @ApiResponse(
                    responseCode =  "400",
                    description = "Datos invalidos o email duplicado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<UsuarioDTO> crearUsuario(
            @Parameter(description = "Datos del usuario a Crear", required = true)
            @RequestBody UsuarioCreateDTO createDTO
            ){
        log.info("POST /api/usuarios - Creando usuario: {}", createDTO.getEmail());
        try{
            UsuarioDTO createdUsuario = usuarioService.crearUsuario(createDTO);
            log.info("Usuario creado exitosamente con id: {}",createdUsuario.getCedula());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
        }catch (IllegalArgumentException e){
            log.warn("Error de validacion al crear usuario: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}