package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.ReservationStateEnum;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    private String reservationsName;
    @NotNull
    private LocalDateTime entryDate; // Formato ISO 8601
    @NotNull
    private LocalDateTime departureDay; // Formato ISO 8601
    @NotNull
    private ReservationStateEnum state;
    @NotNull
    private boolean earlyDeparture;

    @ManyToMany
    @JoinTable(
            name = "reservation_room",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<Room> rooms = new HashSet<>();

    @ManyToMany(mappedBy = "reservations")
    private Set<Client> occupants = new HashSet<>();

    @OneToOne
    private Reservation previousReservation;


}
