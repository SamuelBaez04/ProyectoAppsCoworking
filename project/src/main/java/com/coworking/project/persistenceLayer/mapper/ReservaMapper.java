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
import com.coworking.project.persistenceLayer.entity.RecursoEntity;
import com.coworking.project.persistenceLayer.entity.ReservaEntity;
import com.coworking.project.persistenceLayer.entity.UsuarioEntity;

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

    default RecursoEntity mapIdToRecurso(Long id) {
        if (id == null) return null;
        RecursoEntity recurso = new RecursoEntity();
        recurso.setIdRecurso(id);
        return recurso;
    }

    default UsuarioEntity mapCedulaToUsuario(int cedula) {
        if (cedula == 0) return null;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCedula(cedula);
        return usuario;
    }

}
