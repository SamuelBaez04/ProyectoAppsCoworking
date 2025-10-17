package com.coworking.project.businessLayer.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coworking.project.businessLayer.dto.ReporteCreateDTO;
import com.coworking.project.businessLayer.dto.ReporteDTO;
import com.coworking.project.businessLayer.dto.ReporteUpdateDTO;
import com.coworking.project.businessLayer.service.ReporteService;
import com.coworking.project.persistenceLayer.dao.ReporteDAO;
import com.coworking.project.util.TipoReporte;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteDAO reporteDAO;

    @Override
    public ReporteDTO crearReporte(ReporteCreateDTO createDTO) {
        log.info("Creando nuevo reporte del tipo: {}", createDTO.getTipoReporte());
        if (createDTO.getFechaGeneracion() == null) {
            createDTO.setFechaGeneracion(LocalDate.now()); 
        }

        return reporteDAO.createReporte(createDTO);
    }

    @Override
    public ReporteDTO obtenerReportePorId(Integer idReporte) {
        log.debug("Buscando reporte con ID: {}", idReporte);
        return reporteDAO.findById(idReporte).orElseThrow(() -> {
            log.warn("Reporte no encontrado: {}", idReporte);
            return new RuntimeException("Reporte no encontrado con ID: " + idReporte);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTO> listarReportes() {
        log.debug("Obteniendo lista de todos los reportes");
        return reporteDAO.findAll();
    }

    @Override
    public ReporteDTO actualizarReporte(Integer idReporte, ReporteUpdateDTO updateDTO) {
        log.info("Actualizando reporte con ID: {}", idReporte);
        return reporteDAO.update(idReporte, updateDTO).orElseThrow(() -> {
            log.warn("Intento de actualizar reporte inexistente con ID: {}", idReporte);
            return new RuntimeException("Reporte no encontrado con ID: " + idReporte);
        });
    }

    @Override
    public void eliminarReporte(Integer idReporte) {
        log.info("Eliminando reporte con ID: {}", idReporte);
        if (!reporteDAO.deleteById(idReporte)) {
            log.warn("Intento de eliminar reporte inexistente con ID: {}", idReporte);
            throw new RuntimeException("Reporte no encontrado con ID: " + idReporte);
        }
        log.info("Reporte eliminado exitosamente con ID: {}", idReporte);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTO> obtenerReportesPorTipo(TipoReporte tipoReporte) {
        log.debug("Buscando reportes por tipo: {}", tipoReporte);
        return reporteDAO.findByTipoReporte(tipoReporte);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTO> listarReportesPorFecha(LocalDate fechaGeneracion) {
        log.debug("Buscando reportes generados en la fecha: {}", fechaGeneracion);
        return reporteDAO.findByFechaGeneracion(fechaGeneracion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTO> listarReportesPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        log.debug("Buscando reportes entre {} y {}", fechaInicio, fechaFin);
        return reporteDAO.findReportesPorRangoDeFechas(fechaInicio, fechaFin);
    }
}
