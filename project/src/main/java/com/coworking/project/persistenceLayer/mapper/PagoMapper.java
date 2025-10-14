package com.coworking.project.persistenceLayer.mapper;

import com.coworking.project.businessLayer.dto.PagoCreateDTO;
import com.coworking.project.businessLayer.dto.PagoDTO;
import com.coworking.project.businessLayer.dto.PagoUpdateDTO;
import com.coworking.project.persistenceLayer.entity.PagoEntity;
import com.coworking.project.persistenceLayer.entity.ReservaEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PagoMapper {

    // --- Convertir Entity a DTO ---
    @Mapping(source = "reservaEntity.idReserva", target = "idReserva")
    PagoDTO toDTO(PagoEntity pagoEntity);

    List<PagoDTO> toDTOList(List<PagoEntity> pagos);

    // --- Crear nuevo Pago ---
    @Mapping(target = "idPago", ignore = true)
    @Mapping(source = "idReserva", target = "reservaEntity.idReserva")
    PagoEntity toEntity(PagoCreateDTO createDTO);

    // --- Actualizar Pago existente ---
    @Mapping(target = "idPago", ignore = true)
    @Mapping(target = "reservaEntity", ignore = true) // No se permite cambiar la reserva del pago
    void updateEntityFromDTO(PagoUpdateDTO updateDTO, @MappingTarget PagoEntity pagoEntity);


}
