package com.coworking.project.businessLayer.service.impl;

import com.coworking.project.businessLayer.dto.PagoCreateDTO;
import com.coworking.project.businessLayer.dto.PagoDTO;
import com.coworking.project.businessLayer.dto.PagoUpdateDTO;
import com.coworking.project.businessLayer.service.PagoService;
import com.coworking.project.persistenceLayer.dao.PagoDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementación del servicio para la gestión de pagos.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PagoServiceImpl implements PagoService {

    private final PagoDAO pagoDAO;

    @Override
    public PagoDTO crearPago(PagoCreateDTO createDTO) {
        log.info("Creando nuevo pago para la reserva con ID: {}", createDTO.getIdReserva());
        PagoDTO creado = pagoDAO.createPago(createDTO);
        log.info("Pago creado exitosamente con ID: {}", creado.getIdPago());
        return creado;
    }

    @Override
    public PagoDTO obtenerPagoPorId(int idPago) {
        log.debug("Buscando pago con ID: {}", idPago);
        return pagoDAO.findById(idPago).orElseThrow(() -> {
            log.warn("Pago no encontrado con ID: {}", idPago);
            return new RuntimeException("Pago no encontrado con ID: " + idPago);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> listarPagos() {
        log.debug("Obteniendo lista de todos los pagos");
        return pagoDAO.findAll();
    }

    @Override
    public PagoDTO actualizarPago(int idPago, PagoUpdateDTO updateDTO) {
        log.info("Actualizando pago con ID: {}", idPago);
        return pagoDAO.update(idPago, updateDTO).orElseThrow(() -> {
            log.warn("Intento de actualizar pago inexistente con ID: {}", idPago);
            return new RuntimeException("Pago no encontrado con ID: " + idPago);
        });
    }

    @Override
    public void eliminarPago(int idPago) {
        log.info("Eliminando pago con ID: {}", idPago);
        if (!pagoDAO.deleteById(idPago)) {
            log.warn("Intento de eliminar pago inexistente con ID: {}", idPago);
            throw new RuntimeException("Pago no encontrado con ID: " + idPago);
        }
        log.info("Pago eliminado correctamente con ID: {}", idPago);
    }

    @Override
    public List<PagoDTO> obtenerPagosPorMetodo(String metodoPago) {
        log.debug("Buscando pagos con método de pago: {}", metodoPago);
        return pagoDAO.findByMetodoPago(metodoPago);
    }

    @Override
    public List<PagoDTO> obtenerPagosPorFecha(LocalDate fechaPago) {
        log.debug("Buscando pagos realizados en la fecha: {}", fechaPago);
        return pagoDAO.findByFechaPago(fechaPago);
    }

    @Override
    public List<PagoDTO> obtenerPagosPorReserva(int idReserva) {
        log.debug("Buscando pagos asociados a la reserva con ID: {}", idReserva);
        return pagoDAO.findByReserva(idReserva);
    }

    @Override
    public Double calcularTotalPagosPorReserva(int idReserva) {
        log.debug("Calculando total de pagos para la reserva con ID: {}", idReserva);
        Double total = pagoDAO.calcularTotalPagosPorReserva(idReserva);
        log.info("Total de pagos para la reserva {}: {}", idReserva, total);
        return total;
    }
}
