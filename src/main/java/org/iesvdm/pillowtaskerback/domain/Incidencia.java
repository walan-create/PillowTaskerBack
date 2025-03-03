package org.iesvdm.pillowtaskerback.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String titulo;
    private String concepto;
    private LocalDateTime fecha;

    @ManyToOne
    @ToString.Exclude
    private Hotel hotel;

    @ManyToOne
    @ToString.Exclude
    private Empleado empleado;


}
