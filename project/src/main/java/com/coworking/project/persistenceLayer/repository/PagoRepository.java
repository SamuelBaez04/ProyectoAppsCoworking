package com.coworking.project.persistenceLayer.repository;

import com.coworking.project.persistenceLayer.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PagoRepository extends JpaRepository<PagoEntity, Integer> {

    List<PagoEntity> findByMetodoPago(String metodoPago);

    List<PagoEntity> findByReservaEntityIdReserva(int idReserva);

    List<PagoEntity> findByFechaPago(LocalDate fechaPago);

    @Query("SELECT SUM(p.monto) FROM PagoEntity p WHERE p.reservaEntity.idReserva = :idReserva")
    Double calcularTotalPagosPorReserva(int idReserva);

}
