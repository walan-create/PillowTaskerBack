package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String postalCode;
    private String address;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @NotNull // Evita que llegue null desde la API
    @JoinColumn(name = "owner_id", nullable = false) // Asegura que en la BD no pueda ser NULL
    private User owner;

    // Relación con empleados, usando CascadeType.ALL
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore // Evita problemas de serialización
    private Set<Employee> employees = new HashSet<>();

    // Relación con incidencias, usando CascadeType.ALL
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Incidence> incidences = new HashSet<>();

    // Relación con habitaciones, usando CascadeType.ALL
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Room> rooms = new HashSet<>();

    // Relación con invitaciones, usando CascadeType.ALL
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Invitation> invitations = new HashSet<>();
}

