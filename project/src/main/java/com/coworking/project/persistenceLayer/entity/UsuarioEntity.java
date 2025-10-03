package com.coworking.project.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor; // Añadido para consistencia
import lombok.Data; 
import lombok.NoArgsConstructor; // Añadido para consistencia

import java.util.List;

@Entity
@Table(name = "Usuarios") // Mapea a CREATE TABLE Usuarios
@Data
@AllArgsConstructor // Incluir si usas constructores de Lombok
@NoArgsConstructor // Incluir si usas constructores de Lombok
public class UsuarioEntity{

    // Clave primaria no auto-generada (cedula)
    @Id 
    @Column(name = "cedula")
    private int cedula; // int es apropiado para INT en SQL

    @Column(name = "nombre_completo", nullable = false, length = 50)
    private String nombreCompleto;
     
    // Relación Many-to-One: FK id_rol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol", nullable = false)
    private RolEntity rol;

    @Column(name = "direccion", length = 150)
    private String direccion;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "telefono", length = 20)
    private String telefono;

    // `unique = true` es vital para mapear la restricción UNIQUE del SQL
    @Column(name = "email", unique = true, length = 100)
    private String email;

    // Relación One-to-Many: Un usuario puede tener muchas reservas.
    // 'mappedBy' apunta al campo 'usuarioReserva' en ReservaEntity.
    @OneToMany(mappedBy = "usuarioReserva", fetch = FetchType.LAZY)
    private List<ReservaEntity> reservas;
}