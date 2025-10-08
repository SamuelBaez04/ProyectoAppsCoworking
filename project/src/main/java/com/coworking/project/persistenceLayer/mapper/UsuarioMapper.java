package com.coworking.project.persistenceLayer.mapper;


import com.coworking.project.businessLayer.dto.UsuarioCreateDTO;
import com.coworking.project.businessLayer.dto.UsuarioDTO;
import com.coworking.project.businessLayer.dto.UsuarioUpdateDTO;
import com.coworking.project.persistenceLayer.entity.RolEntity;
import com.coworking.project.persistenceLayer.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UsuarioMapper {


    @Mapping(source = "rol.idRol", target = "idRol")
    UsuarioDTO toDTO(UsuarioEntity usuarioEntity);


    List<UsuarioDTO> toDTOList(List<UsuarioEntity> entities);


    @Mapping(target = "reservas", ignore = true)
    @Mapping(source = "idRol", target = "rol.idRol")
    UsuarioEntity toEntity(UsuarioCreateDTO createDTO);

    @Mapping(target = "cedula", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "rol", ignore = true)
    void updateEntityFromDTO(UsuarioUpdateDTO updateDTO, @MappingTarget UsuarioEntity entity);

    default RolEntity map(Long idRol){
        RolEntity rol = new RolEntity();
        rol.setIdRol(idRol);
        return rol;
    }
    

}
