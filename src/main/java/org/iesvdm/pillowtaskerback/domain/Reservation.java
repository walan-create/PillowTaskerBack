package org.iesvdm.pillowtaskerback.domain;

import jakarta.persistence.*;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.EstadoReservaEnum;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String reservationsName;
    private LocalDateTime entryDate;
    private LocalDateTime departureDay;
    private EstadoReservaEnum state;
    private boolean earlyDeparture;

    @ManyToMany(mappedBy = "reservations")
    private Set<Room> rooms = new HashSet<>();

    @ManyToMany(mappedBy = "reservations")
    private Set<Client> occupants;

    @OneToOne
    private Reservation previousReservation;


}
