package com.gtics.lahaces.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "local")
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_local")
    private int id_local;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "latitud")
    private Float latitud;
    @Column(name = "longitud")
    private Float longitud;
}
