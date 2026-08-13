package com.bookiin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "libros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Libro {

    @Id
    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "autor")
    private String autor;

    @Column(name = "editorial")
    private String editorial;

    @Column(name = "url_portada", length = 1000)
    private String urlPortada;
}
