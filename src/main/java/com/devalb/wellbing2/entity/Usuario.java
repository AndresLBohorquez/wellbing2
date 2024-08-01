package com.devalb.wellbing2.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Usuario {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String celular;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String direccion;

    private LocalDate fechaRegistro;

    @Column(nullable = false, unique = true)
    private String codigoUsuario;

    private int inactivo;

    @Column(nullable = false)
    private boolean visible;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "UserRoles", joinColumns = @JoinColumn(name = "usuarioId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "rolId", referencedColumnName = "id"))
    private List<Rol> roles;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private EstadoUsuario estadoUsuario;

    private Double wellPoints;
}
