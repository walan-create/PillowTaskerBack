package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.RoomStateEnum;
import org.iesvdm.pillowtaskerback.enums.RoomTypeEnum;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    private String code;
    @NotNull
    private Integer capacity;
    @NotNull
    private Integer numberOfRooms;
    @NotNull
    private boolean kitchen;
    @NotNull
    private RoomTypeEnum type;
    @NotNull
    private RoomStateEnum state;

    @ManyToOne
    @ToString.Exclude
    //@JsonIgnore
    private Hotel hotel;

    @ManyToMany(mappedBy = "rooms")
    @ToString.Exclude
    //@JsonIgnore
    //@JsonManagedReference
    private Set<Reservation> reservations = new HashSet<>();

}
