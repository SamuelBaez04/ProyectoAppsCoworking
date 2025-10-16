package com.coworking.project.persistenceLayer.mapper;

import com.coworking.project.businessLayer.dto.NotificacionCreateDTO;
import com.coworking.project.businessLayer.dto.NotificacionDTO;
import com.coworking.project.businessLayer.dto.NotificacionUpdateDTO;
import com.coworking.project.persistenceLayer.entity.NotificacionEntity;
import com.coworking.project.persistenceLayer.entity.UsuarioEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface NotificacionMapper {

    // Convertir Entity -> DTO
    @Mapping(source = "usuario.cedula", target = "usuarioCedula")
    NotificacionDTO toDTO(NotificacionEntity entity);

    List<NotificacionDTO> toDTOList(List<NotificacionEntity> entities);

    // Convertir CreateDTO -> Entity
    @Mapping(target = "idNotificacion", ignore = true)
    @Mapping(target = "usuario", source = "usuarioCedula")
    @Mapping(target = "estado", expression = "java(com.coworking.project.util.NotificacionEstado.PENDIENTE)")
    NotificacionEntity toEntity(NotificacionCreateDTO createDTO);

    // Convertir UpdateDTO -> actualizar entidad existente
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idNotificacion", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "tipoNotificacion", ignore = true)
    @Mapping(target = "fechaEnvio", ignore = true)
    void updateEntityFromDTO(NotificacionUpdateDTO updateDTO, @MappingTarget NotificacionEntity entity);

    default UsuarioEntity map(Integer cedulaUsuario) {
        if (cedulaUsuario == null) {
            return null;
        }
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCedula(cedulaUsuario);
        return usuario;
    }

}
