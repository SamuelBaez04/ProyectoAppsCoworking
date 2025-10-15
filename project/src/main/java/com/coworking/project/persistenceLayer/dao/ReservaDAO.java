package com.coworking.project.persistenceLayer.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.coworking.project.businessLayer.dto.ReservaCreateDTO;
import com.coworking.project.businessLayer.dto.ReservaDTO;
import com.coworking.project.businessLayer.dto.ReservaUpdateDTO;
import com.coworking.project.persistenceLayer.entity.RecursoEntity;
import com.coworking.project.persistenceLayer.entity.ReservaEntity;
import com.coworking.project.persistenceLayer.entity.UsuarioEntity;
import com.coworking.project.persistenceLayer.mapper.ReservaMapper;
import com.coworking.project.persistenceLayer.repository.ReservaRepository;
import com.coworking.project.persistenceLayer.repository.RecursoRepository;
import com.coworking.project.persistenceLayer.repository.UsuarioRepository;
import com.coworking.project.util.ReservaEstado;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservaDAO {

    private final ReservaRepository reservaRepository;
    private final RecursoRepository recursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaMapper reservaMapper;


public ReservaDTO createReserva(ReservaCreateDTO createDTO) {
    // 1️⃣ Buscar el recurso en BD
    RecursoEntity recurso = recursoRepository.findById(createDTO.getIdRecurso())
            .orElseThrow(() -> new RuntimeException("Recurso no encontrado con ID: " + createDTO.getIdRecurso()));

    // 2️⃣ Buscar el usuario (según cómo se identifique en tu BD)
    UsuarioEntity usuario = usuarioRepository.findById(createDTO.getUsuarioReserva())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con cédula: " + createDTO.getUsuarioReserva()));

    // 3️⃣ Mapear el DTO a entidad base
    ReservaEntity reservaEntity = reservaMapper.toEntity(createDTO);

    // 4️⃣ Asignar relaciones (con entidades administradas)
    reservaEntity.setRecursoEntity(recurso);
    reservaEntity.setUsuarioReserva(usuario);

    // 5️⃣ Guardar la reserva
    ReservaEntity saved = reservaRepository.save(reservaEntity);

    // 6️⃣ Retornar DTO
    return reservaMapper.toDTO(saved);
}


    public Optional<ReservaDTO> findById(Integer idReserva) {
        return reservaRepository.findById(idReserva)
                .map(reservaMapper::toDTO);
    }

    public List<ReservaDTO> findAll() {
        List<ReservaEntity> entities = reservaRepository.findAll();
        return reservaMapper.toDTOList(entities);
    }

    public Optional<ReservaDTO> update(Integer idReserva, ReservaUpdateDTO updateDTO) {
        return reservaRepository.findById(idReserva)
                .map(existingEntity -> {
                    reservaMapper.updateEntityFromDTO(updateDTO, existingEntity);
                    ReservaEntity updatedEntity = reservaRepository.save(existingEntity);
                    return reservaMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer idReserva) {
        if (reservaRepository.existsById(idReserva)) {
            reservaRepository.deleteById(idReserva);
            return true;
        }
        return false;
    }

    public List<ReservaDTO> findByUsuario(int cedula) {
        List<ReservaEntity> entities = reservaRepository.findByUsuarioReservaCedula(cedula);
        return reservaMapper.toDTOList(entities);
    }

    public List<ReservaDTO> findByRecurso(Long idRecurso) {
        List<ReservaEntity> entities = reservaRepository.findByRecursoEntityIdRecurso(idRecurso);
        return reservaMapper.toDTOList(entities);
    }

    public List<ReservaDTO> findByEstado(ReservaEstado estado) {
        List<ReservaEntity> entities = reservaRepository.findByEstado(estado);
        return reservaMapper.toDTOList(entities);
    }

    public List<ReservaDTO> findByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<ReservaEntity> entities = reservaRepository.findByFechaInicioBetween(fechaInicio, fechaFin);
        return reservaMapper.toDTOList(entities);
    }

    public List<ReservaDTO> findActivasPorUsuario(int cedula) {
        List<ReservaEntity> activas = reservaRepository.findReservasActivasPorUsuario(cedula);
        return reservaMapper.toDTOList(activas);
    }

}
