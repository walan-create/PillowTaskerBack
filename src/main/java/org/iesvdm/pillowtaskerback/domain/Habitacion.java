package org.iesvdm.pillowtaskerback.domain;

import jakarta.persistence.*;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.EstadoHabitacionEnum;
import org.iesvdm.pillowtaskerback.enums.TipoHabitacionEnum;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String numero;
    private Integer capacidad;
    private Integer cuartos;
    private boolean cocina;
    private TipoHabitacionEnum tipo;
    private EstadoHabitacionEnum estado;

    @ManyToOne
    @ToString.Exclude
    private Hotel hotel;

    @ManyToMany
    private Set<Reserva> reservas = new HashSet<>();

}
