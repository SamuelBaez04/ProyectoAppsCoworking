package com.coworking.project.persistenceLayer.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import com.coworking.project.businessLayer.dto.ReservaCreateDTO;
import com.coworking.project.businessLayer.dto.ReservaDTO;
import com.coworking.project.businessLayer.dto.ReservaUpdateDTO;
import com.coworking.project.persistenceLayer.entity.ReservaEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)

public interface ReservaMapper {

    @Mapping(source = "usuarioReserva.cedula", target = "usuarioReserva")
    @Mapping(source = "recursoEntity.idRecurso", target = "idRecurso")
    ReservaDTO toDTO(ReservaEntity reservaEntity);

    List<ReservaDTO> toDTOList(List<ReservaEntity> entities);

    @Mapping(target = "idReserva", ignore = true)
    @Mapping(target = "pagos", ignore = true)
    @Mapping(target = "recursoEntity", ignore = true)
    @Mapping(target = "usuarioReserva", ignore = true)
    ReservaEntity toEntity(ReservaCreateDTO createDTO);

    @Mapping(target = "idReserva", ignore = true)
    @Mapping(target = "pagos", ignore = true)
    @Mapping(target = "recursoEntity", ignore = true)
    @Mapping(target = "usuarioReserva", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ReservaUpdateDTO updateDTO, @MappingTarget ReservaEntity reservaEntity);
}
