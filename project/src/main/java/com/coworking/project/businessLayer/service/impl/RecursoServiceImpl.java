package com.coworking.project.businessLayer.service.impl;

import com.coworking.project.businessLayer.dto.RecursoCreateDTO;
import com.coworking.project.businessLayer.dto.RecursoDTO;
import com.coworking.project.businessLayer.dto.RecursoUpdateDTO;
import com.coworking.project.persistenceLayer.dao.RecursoDAO;
import com.coworking.project.persistenceLayer.entity.RecursoEntity;
import com.coworking.project.persistenceLayer.repository.RecursoRepository;
import com.coworking.project.persistenceLayer.mapper.RecursoMapper;
import com.coworking.project.businessLayer.service.RecursoService;
import com.coworking.project.util.TipoRecurso;
import com.coworking.project.util.RecursoEstado;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio para la gestión de recursos.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RecursoServiceImpl implements RecursoService {

    private final RecursoDAO recursoDAO;


    @Override
    public RecursoDTO crearRecurso(RecursoCreateDTO createDTO) {
        log.info("Creando nuevo recurso de tipo: {}", createDTO.getTipoRecurso());
        RecursoDTO creado = recursoDAO.createRecurso(createDTO);
        log.info("Recurso creado exitosamente con ID: {}", creado.getIdRecurso());
        return creado;
    }

    @Override
    public RecursoDTO obtenerRecursoPorId(Long idRecurso) {
        log.debug("Buscando recurso con ID: {}", idRecurso);
        return recursoDAO.findById(idRecurso).orElseThrow(() -> {
            log.warn("Recurso no encontrado: {}", idRecurso);
            return new RuntimeException("Recurso no encontrado con ID:" + idRecurso);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecursoDTO> listarRecursos() {
        log.debug("Obteniendo lista de todos los recursos");
        return recursoDAO.findAll();
    }

    @Override
    public RecursoDTO actualizarRecurso(Long idRecurso, RecursoUpdateDTO updateDTO) {
        log.info("Actualizando recurso con ID: {}", idRecurso);
        return recursoDAO.update(idRecurso, updateDTO).orElseThrow(() -> {
           log.warn("Intento de actualizar recurso inexistente con ID: {}", idRecurso);
           return new RuntimeException("Recurso no encontrado con ID:" + idRecurso);
        });
    }

    @Override
    public void eliminarRecurso(Long idRecurso) {
        log.info("Eliminando recurso con ID: {}", idRecurso);
        if(!recursoDAO.deleteById(idRecurso)){
            log.warn("Intento de eleiminar recurso inexistente con ID: {}", idRecurso);
            throw new RuntimeException("Recurso no encontrado con ID: " + idRecurso);
        }
        log.info("Recurso eliminado con ID: {}", idRecurso);
    }

    @Override
    public List<RecursoDTO> obtenerRecursosPorTipo(TipoRecurso tipoRecurso) {
        log.debug("Buscando recursos por tipo: {}", tipoRecurso);
        return recursoDAO.findByTipoRecurso(tipoRecurso);
    }

    @Override
    public List<RecursoDTO> listarRecursosActivos() {
        log.debug("Obteniendo lista de recursos con estado Activo");
        return recursoDAO.findByEstado(RecursoEstado.activo);
    }

    @Override
    public List<RecursoDTO> buscarPorEstado(RecursoEstado estado) {
        log.debug("Buscando recursos por estado: {}", estado);
        return recursoDAO.findByEstado(estado);
    }

    @Override
    public List<RecursoDTO> buscarPorNombre(String nombreRecurso) {
        log.debug("Buscando recursos por nombre: {}", nombreRecurso);
        return recursoDAO.findByNombreRecurso(nombreRecurso);
    }
}
