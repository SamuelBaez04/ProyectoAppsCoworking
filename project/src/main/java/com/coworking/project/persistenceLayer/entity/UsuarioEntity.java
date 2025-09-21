package com.coworking.project.persistenceLayer.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
public class UsuarioEntity{

    @Id 
    @Column(name = "cedula")
    private int cedula;

    @Column(name = "nombre_completo",nullable = false, length = 50)
    private String nombre_completo;
     
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private RolEntity rolEntity;

    @Column(name = "direccion", length = 150)
    private String direccion;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", unique = true, length = 100)
    private String eamil;

    @OneToMany(mappedBy = "usuarioReserva", fetch = FetchType.LAZY)
    private List<ReservaEntity> reservas;
}