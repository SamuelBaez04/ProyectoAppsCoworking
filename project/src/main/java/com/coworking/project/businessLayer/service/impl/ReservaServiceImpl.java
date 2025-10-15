package com.coworking.project.businessLayer.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coworking.project.businessLayer.dto.ReservaCreateDTO;
import com.coworking.project.businessLayer.dto.ReservaDTO;
import com.coworking.project.businessLayer.dto.ReservaUpdateDTO;
import com.coworking.project.businessLayer.service.ReservaService;
import com.coworking.project.persistenceLayer.dao.ReservaDAO;
import com.coworking.project.util.ReservaEstado;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReservaServiceImpl implements ReservaService {
    private final ReservaDAO reservaDAO;

    @Override
    public ReservaDTO crearReserva(ReservaCreateDTO createDTO) {
        log.info("Creando nueva reserva para el recurso ID: {} y usuario con cédula: {}",
                createDTO.getIdRecurso(), createDTO.getUsuarioReserva());

        ReservaDTO creada = reservaDAO.createReserva(createDTO);
        log.info("Reserva creada exitosamente con ID: {}", creada.getIdReserva());
        return creada;
    }

    @Override
    public ReservaDTO obtenerReservaPorId(int idReserva) {
        log.debug("Buscando reserva con ID: {}", idReserva);
        return reservaDAO.findById(idReserva).orElseThrow(() -> {
            log.warn("Reserva no encontrada con ID: {}", idReserva);
            return new RuntimeException("Reserva no encontrada con ID: " + idReserva);
        });
    }

    @Override
    public List<ReservaDTO> listarReservas() {
        log.debug("Obteniendo lista de todas las reservas");
        return reservaDAO.findAll();
    }

    @Override
    public ReservaDTO actualizarReserva(int idReserva, ReservaUpdateDTO updateDTO) {
        log.info("Actualizando reserva con ID: {}", idReserva);
        return reservaDAO.update(idReserva, updateDTO).orElseThrow(() -> {
            log.warn("Intento de actualizar reserva inexistente con ID: {}", idReserva);
            return new RuntimeException("Reserva no encontrada con ID: " + idReserva);
        });
    }

    @Override
    public boolean eliminarReserva(int idReserva) {
        log.info("Eliminando reserva con ID: {}", idReserva);
        if (!reservaDAO.deleteById(idReserva)) {
            log.warn("Intento de eliminar reserva inexistente con ID: {}", idReserva);
            throw new RuntimeException("Reserva no encontrada con ID: " + idReserva);
        }
        log.info("Reserva eliminada correctamente con ID: {}", idReserva);
        return true;
    }

    @Override
    public List<ReservaDTO> buscarPorEstado(ReservaEstado estado) {
        log.debug("Buscando reservas con estado: {}", estado);
        return reservaDAO.findByEstado(estado);
    }

    @Override
    public List<ReservaDTO> buscarPorUsuario(int cedula) {
        log.debug("Buscando reservas realizadas por el usuario con ID: {}", cedula);
        return reservaDAO.findByUsuario(cedula);
    }

    @Override
    public List<ReservaDTO> buscarPorRecurso(Long idRecurso) {
        log.debug("Buscando reservas asociadas al recurso con ID: {}", idRecurso);
        return reservaDAO.findByRecurso(idRecurso);
    }

    @Override
    public List<ReservaDTO> buscarPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        log.debug("Buscando reservas entre las fechas {} y {}", fechaInicio, fechaFin);
        return reservaDAO.findByRangoFechas(fechaInicio, fechaFin);
    }
}
