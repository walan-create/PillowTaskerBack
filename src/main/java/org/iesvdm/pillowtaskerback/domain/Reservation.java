package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.*;
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

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany
    @JoinTable(
            name = "reservation_room",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    //@JsonManagedReference
    @ToString.Exclude
    private Set<Room> rooms = new HashSet<>();

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany(mappedBy = "reservations")
    //@JsonManagedReference
    @ToString.Exclude
    private Set<Client> occupants = new HashSet<>();

//    @OneToOne
//    private Reservation previousReservation;


}
