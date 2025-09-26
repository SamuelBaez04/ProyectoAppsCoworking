package com.coworking.project.persistenceLayer.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "rol")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Rol")
    private Long idRol;

    @Column(name = "nombre_Rol",nullable = false, length = 50)
    private String nombreRol;

    @OneToMany(mappedBy = "rolEntity", fetch = FetchType.LAZY)
    private List<UsuarioEntity> usuarios;

}
