package com.coworking.project.businessLayer.service;

import com.coworking.project.businessLayer.dto.RecursoCreateDTO;
import com.coworking.project.businessLayer.dto.RecursoDTO;
import com.coworking.project.businessLayer.dto.RecursoUpdateDTO;
import com.coworking.project.util.RecursoEstado;
import com.coworking.project.util.TipoRecurso;

import java.util.List;

/**
 * Interfaz del servicio para manejar la lógica de negocio de los recursos.
 */
public interface RecursoService {

    /**
     * Crear un nuevo recurso.
     * @param createDTO Datos del recurso a crear.
     * @return Recurso creado.
     */
    RecursoDTO crearRecurso(RecursoCreateDTO createDTO);

    /**
     * Obtener un recurso por su ID.
     * @param idRecurso ID del recurso a buscar.
     * @return Recurso encontrado.
     */
    RecursoDTO obtenerRecursoPorId(Long idRecurso);

    /**
     * Listar todos los recursos registrados.
     * @return Lista de recursos.
     */
    List<RecursoDTO> listarRecursos();

    /**
     * Actualizar un recurso existente.
     * @param idRecurso ID del recurso a actualizar.
     * @param updateDTO Datos nuevos del recurso.
     * @return Recurso actualizado.
     */
    RecursoDTO actualizarRecurso(Long idRecurso, RecursoUpdateDTO updateDTO);

    /**
     * Eliminar un recurso por su ID.
     * @param idRecurso ID del recurso a eliminar.
     */
    void eliminarRecurso(Long idRecurso);

    /**
     * Buscar recursos por tipo.
     * @param tipoRecurso Tipo de recurso (enum).
     * @return Lista de recursos del tipo especificado.
     */
    List<RecursoDTO> obtenerRecursosPorTipo(TipoRecurso tipoRecurso);

    /**
     * Buscar recursos activos (estado = ACTIVO).
     * @return Lista de recursos activos.
     */
    List<RecursoDTO> listarRecursosActivos();

    List<RecursoDTO> buscarPorEstado(RecursoEstado estado);

    List<RecursoDTO> buscarPorNombre(String nombreRecurso);
}
