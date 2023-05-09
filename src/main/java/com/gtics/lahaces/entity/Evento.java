package com.gtics.lahaces.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private int id_evento;
//    @Column(nullable = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha")
    private String fecha;

    @ManyToOne
    @JoinColumn(name = "id_local")
    private Local local;

//    @Column(name = "id_local")
//    private int id_local;
}
