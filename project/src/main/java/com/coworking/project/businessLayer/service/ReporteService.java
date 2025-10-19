package com.coworking.project.businessLayer.service;

import java.time.LocalDate;
import java.util.List;

import com.coworking.project.businessLayer.dto.ReporteCreateDTO;
import com.coworking.project.businessLayer.dto.ReporteDTO;
import com.coworking.project.businessLayer.dto.ReporteUpdateDTO;
import com.coworking.project.util.TipoReporte;

public interface ReporteService {

    ReporteDTO crearReporte(ReporteCreateDTO createDTO);

    ReporteDTO obtenerReportePorId(Integer idReporte);

    List<ReporteDTO> listarReportes();

    ReporteDTO actualizarReporte(Integer idReporte, ReporteUpdateDTO updateDTO);

    void eliminarReporte(Integer idReporte);

    List<ReporteDTO> obtenerReportesPorTipo(TipoReporte tipoReporte);

    List<ReporteDTO> listarReportesPorFecha(LocalDate fechaGeneracion);

    List<ReporteDTO> listarReportesPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin);
}

