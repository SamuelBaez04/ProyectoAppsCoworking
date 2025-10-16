package com.coworking.project.persistenceLayer.entity;

import com.coworking.project.util.TipoReporte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private int idReporte;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDate fechaGeneracion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reporte", nullable = false, length = 50)
    private TipoReporte tipoReporte; // Ej: "ocupacion", "ingresos", "uso_por_usuario"

    @Column(name = "total_registros")
    private int totalRegistros;

    @Column(name = "monto_total")
    private double montoTotal;
}
