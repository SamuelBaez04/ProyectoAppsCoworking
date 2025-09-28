package com.coworking.project.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Rol") // Mapea a CREATE TABLE Rol
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolEntity {

    // Clave primaria auto-incrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol") // Corregida la capitalización si es necesario
    private Long idRol;

    @Column(name = "nombre_rol", nullable = false, length = 50) // Corregida la capitalización si es necesario
    private String nombreRol;

    // Relación One-to-Many: Un rol puede tener muchos usuarios.
    // 'mappedBy' apunta al campo 'rolEntity' en UsuarioEntity.
    @OneToMany(mappedBy = "rolEntity", fetch = FetchType.LAZY)
    private List<UsuarioEntity> usuarios;

}