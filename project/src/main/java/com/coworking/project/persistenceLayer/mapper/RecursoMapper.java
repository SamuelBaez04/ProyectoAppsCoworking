package com.coworking.project.persistenceLayer.mapper;


import java.util.List;

import com.coworking.project.businessLayer.dto.RecursoCreateDTO;
import com.coworking.project.businessLayer.dto.RecursoUpdateDTO;
import org.mapstruct.*;

import com.coworking.project.businessLayer.dto.RecursoDTO;
import com.coworking.project.persistenceLayer.entity.RecursoEntity;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RecursoMapper {

    RecursoDTO toDTO(RecursoEntity recursoEntity);

    List<RecursoDTO> toDTOList(List<RecursoEntity> entities);

    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "idRecurso", ignore = true)
    @Mapping(source = "recursoEstado", target = "estado")
    RecursoEntity toEntity(RecursoCreateDTO createDTO);

    @Mapping(target = "idRecurso",  ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "tipoRecurso", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RecursoUpdateDTO updateDTO, @MappingTarget RecursoEntity recursoEntity);



}
