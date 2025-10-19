package com.coworking.project.persistenceLayer.mapper;

import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import com.coworking.project.businessLayer.dto.ReporteCreateDTO;
import com.coworking.project.businessLayer.dto.ReporteDTO;
import com.coworking.project.businessLayer.dto.ReporteUpdateDTO;
import com.coworking.project.persistenceLayer.entity.ReporteEntity;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ReporteMapper {

    ReporteDTO toDTO(ReporteEntity reporteEntity);

    List<ReporteDTO> toDTOList(List<ReporteEntity> entities);

    @Mapping(target = "idReporte", ignore = true)
    @Mapping(source = "tipoReporte", target = "tipoReporte")
    ReporteEntity toEntity(ReporteCreateDTO createDTO);

    @Mapping(target = "idReporte", ignore = true)
    @Mapping(source = "tipoReporte", target = "tipoReporte")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ReporteUpdateDTO updateDTO, @MappingTarget ReporteEntity reporteEntity);
}