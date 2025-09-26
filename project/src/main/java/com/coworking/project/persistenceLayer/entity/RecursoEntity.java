package com.coworking.project.persistenceLayer.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "recursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso")
    private Long idRecurso;

    @Column(name = "tipo_recurso", nullable = false, length = 50)
    private String tipoRecurso;

    @Column(name = "nombre_recurso", nullable = false, length = 100)
    private String nombreRecurso;

    @Column(name = "valor_hora")
    private double valorHora;

    @Column(name = "fecha_inicio_tarifa")
    private LocalDate fechaInicioTarifa;

    @Column(name = "fecha_fin_tarifa")
    private LocalDate fechaFinTarifa;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @OneToMany(mappedBy = "recursoEntity", fetch = FetchType.LAZY)
    private List<ReservaEntity> reservas;

}
