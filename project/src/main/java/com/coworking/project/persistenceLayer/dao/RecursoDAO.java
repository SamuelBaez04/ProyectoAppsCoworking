package com.coworking.project.persistenceLayer.dao;


import com.coworking.project.businessLayer.dto.RecursoCreateDTO;
import com.coworking.project.businessLayer.dto.RecursoDTO;
import com.coworking.project.businessLayer.dto.RecursoUpdateDTO;
import com.coworking.project.persistenceLayer.entity.RecursoEntity;
import com.coworking.project.persistenceLayer.mapper.RecursoMapper;
import com.coworking.project.persistenceLayer.repository.RecursoRepository;
import com.coworking.project.util.RecursoEstado;
import com.coworking.project.util.TipoRecurso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecursoDAO {

    private final RecursoRepository recursoRepository;
    private final RecursoMapper recursoMapper;

    public RecursoDTO createRecurso(RecursoCreateDTO createDTO){
        RecursoEntity recursoEntity = recursoMapper.toEntity(createDTO);
        RecursoEntity savedEntity = recursoRepository.save(recursoEntity);
        return recursoMapper.toDTO(savedEntity);
    }

    public Optional<RecursoDTO> findById(Long idRecurso){
        return recursoRepository.findByIdRecurso(idRecurso).map(recursoMapper::toDTO);
    }

    public List<RecursoDTO> findAll(){
        List<RecursoEntity> entities = recursoRepository.findAll();
        return recursoMapper.toDTOList(entities);
    }

    public Optional<RecursoDTO> update(Long idRecurso, RecursoUpdateDTO updateDTO){
        return recursoRepository.findByIdRecurso(idRecurso).map(existingEntity -> {
            recursoMapper.updateEntityFromDTO(updateDTO, existingEntity);
            RecursoEntity updatedEntity = recursoRepository.save(existingEntity);
            return recursoMapper.toDTO(updatedEntity);
        });
    }

    public boolean deleteById(Long idRecurso){
        if(recursoRepository.existsById(idRecurso)){
            recursoRepository.deleteById(idRecurso);
            return true;
        }
        return false;
    }

    public List<RecursoDTO> findByTipoRecurso(TipoRecurso tipoRecurso){
        List<RecursoEntity> entities = recursoRepository.findByTipoRecurso(tipoRecurso);
        return recursoMapper.toDTOList(entities);
    }

    public List<RecursoDTO> findByEstado(RecursoEstado estado){
        List<RecursoEntity> entities = recursoRepository.findByEstado(estado);
        return recursoMapper.toDTOList(entities);
    }



    public List<RecursoDTO> findActivos() {
        List<RecursoEntity> activos = recursoRepository.findByEstado(RecursoEstado.activo);
        return recursoMapper.toDTOList(activos);
    }

}
