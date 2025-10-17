package com.coworking.project.persistenceLayer.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.coworking.project.businessLayer.dto.ReporteCreateDTO;
import com.coworking.project.businessLayer.dto.ReporteDTO;
import com.coworking.project.businessLayer.dto.ReporteUpdateDTO;
import com.coworking.project.persistenceLayer.entity.ReporteEntity;
import com.coworking.project.persistenceLayer.mapper.ReporteMapper;
import com.coworking.project.persistenceLayer.repository.ReporteRepository;
import com.coworking.project.util.TipoReporte;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReporteDAO {
    private final ReporteRepository reporteRepository;
    private final ReporteMapper reporteMapper;

    public ReporteDTO createReporte(ReporteCreateDTO createDTO) {
        ReporteEntity reporteEntity = reporteMapper.toEntity(createDTO);
        ReporteEntity savedEntity = reporteRepository.save(reporteEntity);
        return reporteMapper.toDTO(savedEntity);
    }

    public Optional<ReporteDTO> findById(Integer idReporte) {
        return reporteRepository.findByIdReporte(idReporte)
                .map(reporteMapper::toDTO);
    }

    public List<ReporteDTO> findAll() {
        List<ReporteEntity> entities = reporteRepository.findAll();
        return reporteMapper.toDTOList(entities);
    }

    public Optional<ReporteDTO> update(Integer idReporte, ReporteUpdateDTO updateDTO) {
        return reporteRepository.findByIdReporte(idReporte)
                .map(existingEntity -> {
                    reporteMapper.updateEntityFromDTO(updateDTO, existingEntity);
                    ReporteEntity updatedEntity = reporteRepository.save(existingEntity);
                    return reporteMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer idReporte) {
        if (reporteRepository.existsById(idReporte)) {
            reporteRepository.deleteById(idReporte);
            return true;
        }
        return false;
    }

    public List<ReporteDTO> findByTipoReporte(TipoReporte tipoReporte) {
        List<ReporteEntity> entities = reporteRepository.findByTipoReporte(tipoReporte);
        return reporteMapper.toDTOList(entities);
    }

    public List<ReporteDTO> findByFechaGeneracion(LocalDate fechaGeneracion) {
        List<ReporteEntity> entities = reporteRepository.findByFechaGeneracion(fechaGeneracion);
        return reporteMapper.toDTOList(entities);
    }

    public List<ReporteDTO> findReportesPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<ReporteEntity> entities = reporteRepository.findReportesPorRangoDeFechas(fechaInicio, fechaFin);
        return reporteMapper.toDTOList(entities);
    }
}
