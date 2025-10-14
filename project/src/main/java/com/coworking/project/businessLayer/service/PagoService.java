package com.coworking.project.businessLayer.service;

import com.coworking.project.businessLayer.dto.PagoCreateDTO;
import com.coworking.project.businessLayer.dto.PagoDTO;
import com.coworking.project.businessLayer.dto.PagoUpdateDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz del servicio para manejar la lógica de negocio de los pagos.
 */
public interface PagoService {

    /**
     * Crear un nuevo pago.
     * @param createDTO Datos del pago a crear.
     * @return Pago creado.
     */
    PagoDTO crearPago(PagoCreateDTO createDTO);

    /**
     * Obtener un pago por su ID.
     * @param idPago ID del pago a buscar.
     * @return Pago encontrado.
     */
    PagoDTO obtenerPagoPorId(int idPago);

    /**
     * Listar todos los pagos registrados.
     * @return Lista de pagos.
     */
    List<PagoDTO> listarPagos();

    /**
     * Actualizar un pago existente.
     * @param idPago ID del pago a actualizar.
     * @param updateDTO Datos nuevos del pago.
     * @return Pago actualizado.
     */
    PagoDTO actualizarPago(int idPago, PagoUpdateDTO updateDTO);

    /**
     * Eliminar un pago por su ID.
     * @param idPago ID del pago a eliminar.
     */
    void eliminarPago(int idPago);


    List<PagoDTO> obtenerPagosPorMetodo(String metodoPago);

    /**
     * Buscar pagos por fecha de pago.
     * @param fechaPago Fecha específica de los pagos.
     * @return Lista de pagos realizados en esa fecha.
     */
    List<PagoDTO> obtenerPagosPorFecha(LocalDate fechaPago);

    /**
     * Buscar pagos por ID de reserva.
     * @param idReserva ID de la reserva.
     * @return Lista de pagos asociados a esa reserva.
     */
    List<PagoDTO> obtenerPagosPorReserva(int idReserva);

    /**
     * Calcular el total de los pagos de una reserva.
     * @param idReserva ID de la reserva.
     * @return Total pagado en la reserva.
     */
    Double calcularTotalPagosPorReserva(int idReserva);
}
