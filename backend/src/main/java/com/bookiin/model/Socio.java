package com.bookiin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "socios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Socio {

    @Id
    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "email_telefono")
    private String emailTelefono;
}
