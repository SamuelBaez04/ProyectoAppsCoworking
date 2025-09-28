package com.coworking.project.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "Pagos") // Mapea a CREATE TABLE Pagos
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoEntity {

    // Clave primaria auto-incrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    // Relación Many-to-One: Muchos pagos pueden pertenecer a una reserva.
    // Mapea la FK id_reserva
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    private ReservaEntity reservaEntity;

    // El tipo Double en Java es apropiado para el DECIMAL(10,2) en SQL.
    @Column(name = "monto", nullable = false)
    private Double monto;

    // LocalDate en Java es el tipo estándar para el DATE en SQL.
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;
}