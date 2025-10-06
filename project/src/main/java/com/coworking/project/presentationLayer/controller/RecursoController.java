package com.coworking.project.presentationLayer.controller;

import com.coworking.project.businessLayer.dto.RecursoCreateDTO;
import com.coworking.project.businessLayer.dto.RecursoDTO;
import com.coworking.project.businessLayer.dto.RecursoUpdateDTO;
import com.coworking.project.businessLayer.service.RecursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recursos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recursos", description = "Operaciones Crud para gestion de los Recursos")
public class RecursoController {

    private final RecursoService recursoService;

    @PostMapping
    @Operation(
            summary = "Crear un nuevo Recurso",
            description = "Crear un nuevo recurso en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Recurso creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos invalidos",
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
    public ResponseEntity<RecursoDTO> crearUsuario(
            @Parameter(description = "Datos del recurso a Crear",required = true)
            @RequestBody RecursoCreateDTO createDTO
            ){
        log.info("POST /api/recursos - Creando nuevo Recurso: {}", createDTO.getNombreRecurso());
        try{
            RecursoDTO createdRecurso = recursoService.crearRecurso(createDTO);
            log.info("Recurso creado exitosamente con id: {}", createdRecurso.getIdRecurso());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecurso);
        }catch (IllegalArgumentException e){
            log.warn("Error de validacion al crear usuario: {}", e.getMessage());
            return  ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{idRecurso}")
    @Operation(
            summary = "Actualizar un recurso existente",
            description = "Actualizar lso Datos de un Recurso"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Recurso actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos invalidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            )
    })
    public ResponseEntity<RecursoDTO> actualizarRecurso(
            @Parameter(description = "Id del Recurso a actualizar", required = true, example = "1")
            @PathVariable Long idRecurso,
            @Parameter(description = "Datos actualizados del Recurso", required = true)
            @RequestBody RecursoUpdateDTO updateDTO
    ){
        log.info("PUT /api/recursos/{} - Actualizando Recurso", idRecurso);
        try {
            RecursoDTO updatedRecurso = recursoService.actualizarRecurso(idRecurso, updateDTO);
            log.info("Recurso actualizado exitosamente: {}", idRecurso);
            return ResponseEntity.ok(updatedRecurso);
        }catch(RuntimeException e){
            if(e.getMessage().contains("no encontrado")){
                log.warn("Recurso no encontrado para actualizar id: {}", idRecurso);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar recurso con id{}: {}", idRecurso,e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{idRecurso}")
    @Operation(
            summary = "Eliminar Recurso",
            description = "Elimina un Recurso del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Recurso eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            )
    })
    public ResponseEntity<Void> eliminarRecurso(
            @Parameter(description = "Cedula del usuario", required = true, example = "1")
            @PathVariable Long idRecurso
    ){
        log.info("DELETE /api/recursos/{} - Eliminando Recurso", idRecurso);
        try{
            recursoService.eliminarRecurso(idRecurso);
            log.info("Recurso eliminado exitosamente: {}", idRecurso);
            return ResponseEntity.noContent().build();
        }catch(RuntimeException e){
            if(e.getMessage().contains("no encontrado")){
                log.warn("Recurso no encontrado para eliminar id: {}", idRecurso);
                return ResponseEntity.notFound().build();
            }
            log.error("Error al intentar eliminar un recurso id: {}", idRecurso, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }






    
}
