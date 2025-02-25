package org.iesvdm.pillowtaskerback.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true)
    private String nif;

    private String nombre;
    private String apellido1;
    private String apellido2;
    private LocalDateTime fechaNacimiento;
    private String nacional;
    private String direccion;
    private String codigoPostal;
    private String telefono;

    @ManyToMany
    private Set<Reserva> reservas;

}
