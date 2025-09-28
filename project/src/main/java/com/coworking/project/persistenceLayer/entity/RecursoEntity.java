package com.coworking.project.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.coworking.project.util.RecursoEstado;

@Entity
@Table(name = "Recursos") // Mapea a CREATE TABLE Recursos
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso")
    private Long idRecurso; // Long es apropiado para un ID auto-generado

    @Column(name = "tipo_recurso", nullable = false, length = 50)
    private String tipoRecurso;

    @Column(name = "nombre_recurso", nullable = false, length = 100)
    private String nombreRecurso;

    // double es apropiado para DECIMAL(10,2)
    @Column(name = "valor_hora")
    private double valorHora;

    @Column(name = "fecha_inicio_tarifa")
    private LocalDate fechaInicioTarifa;

    @Column(name = "fecha_fin_tarifa")
    private LocalDate fechaFinTarifa;

    // *** MODIFICACIÓN SUGERIDA: Usar un Enum para la columna ENUM de SQL ***
    @Enumerated(EnumType.STRING) // Le dice a JPA que almacene el nombre del Enum (activo/inactivo)
    @Column(name = "estado", length = 20)
    private RecursoEstado estado;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // Relación One-to-Many: Un recurso puede tener muchas reservas.
    // 'mappedBy' indica el campo en la entidad ReservaEntity que tiene la FK.
    @OneToMany(mappedBy = "recursoEntity", fetch = FetchType.LAZY)
    private List<ReservaEntity> reservas;
}