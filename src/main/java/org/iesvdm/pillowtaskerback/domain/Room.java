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
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String NumberRoom;
    private Integer capacity;
    private Integer roomsNumber;
    private boolean kitchen;
    private TipoHabitacionEnum type;
    private EstadoHabitacionEnum state;

    @ManyToOne
    @ToString.Exclude
    private Hotel hotel;

    @ManyToMany
    private Set<Reservation> reservations = new HashSet<>();

}
