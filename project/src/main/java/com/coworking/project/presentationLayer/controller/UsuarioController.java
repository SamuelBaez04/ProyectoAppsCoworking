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

    
    @GetMapping("/{cedula}")
    @Operation(
            summary = "Buscar usuario por cedula",
            description = "Obtiene la información completa de un usuario en especifico"
    )       
    @ApiResponses(value = {
        @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = UsuarioDTO.class)
                     )
                            
            ),
             @ApiResponse(
                        responseCode =  "404",
                        description = "Usuario no encontrado"
              )

    })
public ResponseEntity<UsuarioDTO> obtenerUsuarioPorCedula(
        @Parameter(description = "Cedula del usuario a buscar", required = true, example = "1")
        @PathVariable int cedula
){
    log.debug("GET /api/usuarios/{} - Buscando usuario por cedula", cedula);

    try{
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorCedula(cedula);
        log.info("Usuario encontrado: {}", cedula);
        return ResponseEntity.ok(usuario);                 
        } catch (RuntimeException e){
        log.warn("Usuario no encontrado con cedula: {}", cedula);
        return ResponseEntity.notFound().build();
    }
}


        @GetMapping
        @Operation(
                summary = "Listar todos los usuarios",
                description = "Obtiene una lista de todos los usuarios registrados en el sistema"
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Lista de usuarios obtenida exitosamente",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = UsuarioDTO.class)
                        )
                )
        })
        public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
            log.debug("GET /api/usuarios - Listando todos los usuarios");
            List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
            log.debug("Total usuarios encontrados: {}", usuarios.size());
            return ResponseEntity.ok(usuarios); 
        }

        @GetMapping("/email/{email}")
        @Operation(
                summary = "Buscar usuario por email",
                description = "Obtiene la información completa de un usuario en especifico por su email"
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Usuario encontrado",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = UsuarioDTO.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado"
                )
        })
        public ResponseEntity<UsuarioDTO> obtenerUsuarioPorEmail(
                @Parameter(description = "Email del usuario a buscar", required = true, example = "natsab@example.com")
                @PathVariable String email
        ) {
            log.debug("GET /api/usuarios/email/{} - Buscando usuario por email", email);
            try {
                UsuarioDTO usuario = usuarioService.obtenerUsuarioPorEmail(email);
                return ResponseEntity.ok(usuario);
            } catch (RuntimeException e) {
                log.warn("Usuario no encontrado con email: {}", email);
                return ResponseEntity.notFound().build();       
            }
        }        









}