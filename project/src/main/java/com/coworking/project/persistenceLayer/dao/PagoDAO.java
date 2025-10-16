package com.coworking.project.persistenceLayer.dao;

import com.coworking.project.businessLayer.dto.PagoCreateDTO;
import com.coworking.project.businessLayer.dto.PagoDTO;
import com.coworking.project.businessLayer.dto.PagoUpdateDTO;
import com.coworking.project.persistenceLayer.entity.PagoEntity;
import com.coworking.project.persistenceLayer.mapper.PagoMapper;
import com.coworking.project.persistenceLayer.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PagoDAO {

    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;

    // Crear un nuevo pago
    public PagoDTO createPago(PagoCreateDTO createDTO) {
        PagoEntity pagoEntity = pagoMapper.toEntity(createDTO);
        PagoEntity savedEntity = pagoRepository.save(pagoEntity);
        return pagoMapper.toDTO(savedEntity);
    }

    // Buscar un pago por su ID
    public Optional<PagoDTO> findById(int idPago) {
        return pagoRepository.findById(idPago).map(pagoMapper::toDTO);
    }

    // Listar todos los pagos
    public List<PagoDTO> findAll() {
        List<PagoEntity> entities = pagoRepository.findAll();
        return pagoMapper.toDTOList(entities);
    }

    // Actualizar un pago existente
    public Optional<PagoDTO> update(int idPago, PagoUpdateDTO updateDTO) {
        return pagoRepository.findById(idPago).map(existingEntity -> {
            pagoMapper.updateEntityFromDTO(updateDTO, existingEntity);
            PagoEntity updatedEntity = pagoRepository.save(existingEntity);
            return pagoMapper.toDTO(updatedEntity);
        });
    }

    // Eliminar un pago por ID
    public boolean deleteById(int idPago) {
        if (pagoRepository.existsById(idPago)) {
            pagoRepository.deleteById(idPago);
            return true;
        }
        return false;
    }


    public List<PagoDTO> findByMetodoPago(String metodoPago) {
        List<PagoEntity> entities = pagoRepository.findByMetodoPago(metodoPago);
        return pagoMapper.toDTOList(entities);
    }

    // Buscar pagos por fecha de pago
    public List<PagoDTO> findByFechaPago(LocalDate fechaPago) {
        List<PagoEntity> entities = pagoRepository.findByFechaPago(fechaPago);
        return pagoMapper.toDTOList(entities);
    }

    // Buscar todos los pagos relacionados a una reserva específica
    public List<PagoDTO> findByReserva(int idReserva) {
        List<PagoEntity> entities = pagoRepository.findByReservaEntityIdReserva(idReserva);
        return pagoMapper.toDTOList(entities);
    }

    // Calcular el total pagado por una reserva
    public Double calcularTotalPagosPorReserva(int idReserva) {
        Double total = pagoRepository.calcularTotalPagosPorReserva(idReserva);
        return total != null ? total : 0.0;
    }
}
