package com.coworking.project.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.coworking.project.util.ReservaEstado;

@Entity
@Table(name = "Reservas") // Mapea a CREATE TABLE Reservas
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private int idReserva;

    // LocalDate y LocalTime son los tipos correctos para DATE y TIME en SQL
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    // *** MODIFICACIÓN SUGERIDA: Usar un Enum para la columna ENUM de SQL ***
    @Enumerated(EnumType.STRING) // Almacena el nombre del Enum (pendiente, confirmada, cancelada)
    @Column(name = "estado", length = 20)
    private ReservaEstado estado;

    // Relación Many-to-One: FK id_recurso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recurso", nullable = false)
    private RecursoEntity recursoEntity;

    // Relación Many-to-One: FK usuario_reserva
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_reserva", nullable = false)
    private UsuarioEntity usuarioReserva; // El nombre del campo usuarioReserva es consistente.

    // Relación One-to-Many: Una reserva puede tener muchos pagos.
    @OneToMany(mappedBy = "reservaEntity", fetch = FetchType.LAZY)
    private List<PagoEntity> pagos;

}