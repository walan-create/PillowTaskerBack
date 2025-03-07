package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String NumberRoom;
    @NotNull
    private Integer capacity;
    @NotNull
    private Integer roomsNumber;
    @NotNull
    private boolean kitchen;
    @NotNull
    private TipoHabitacionEnum type;
    @NotNull
    private EstadoHabitacionEnum state;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private Hotel hotel;

    @ManyToMany
    @JsonIgnore
    private Set<Reservation> reservations = new HashSet<>();

}
