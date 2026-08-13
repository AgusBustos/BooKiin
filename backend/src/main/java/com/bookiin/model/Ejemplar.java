package com.bookiin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ejemplares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ejemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "libro_isbn", nullable = false)
    private Libro libro;

    @Column(name = "estanteria")
    private String estanteria;

    @Column(name = "estante")
    private String estante;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoEjemplar estado;
}
