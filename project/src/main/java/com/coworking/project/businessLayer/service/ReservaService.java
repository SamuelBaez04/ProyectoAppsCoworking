package com.coworking.project.businessLayer.service;

import java.time.LocalDate;
import java.util.List;

import com.coworking.project.businessLayer.dto.ReservaCreateDTO;
import com.coworking.project.businessLayer.dto.ReservaDTO;
import com.coworking.project.businessLayer.dto.ReservaUpdateDTO;
import com.coworking.project.util.ReservaEstado;

public interface ReservaService {

    ReservaDTO crearReserva(ReservaCreateDTO createDTO);

    ReservaDTO obtenerReservaPorId(int idReserva);

    List<ReservaDTO> listarReservas();

    ReservaDTO actualizarReserva(int idReserva, ReservaUpdateDTO updateDTO);

    boolean eliminarReserva(int idReserva);

    List<ReservaDTO> buscarPorEstado(ReservaEstado estado);

    List<ReservaDTO> buscarPorUsuario(int cedula);

    List<ReservaDTO> buscarPorRecurso(Long idRecurso);

    List<ReservaDTO> buscarPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin);

}
